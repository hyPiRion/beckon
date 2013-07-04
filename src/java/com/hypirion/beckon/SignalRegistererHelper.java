package com.hypirion.beckon;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class SignalRegistererHelper {

    /**
     * A mapping of the signal name to the original signal handler.
     */
    private final static Map<String, SignalHandler> originalHandlers =
        new HashMap<String, SignalHandler>();

    /**
     * Registers the new list of functions to the signal name, and returns the
     * old SignalHandler.
     */
    private static SignalHandler reset_BANG_(String signame, List fns) {
        Signal sig = new Signal(signame);
        SignalFolder folder = new SignalFolder(fns);
        SignalHandler oldHandler = Signal.handle(sig, folder);
        return oldHandler;
    }

    /**
     * Registers the signal name to a List of Callables, where each callable
     * returns an Object. The signal handling is performed as follows: The first
     * callable is called, and if it returns a value equal to <code>false</code>
     * or <code>null</code> it will stop. Otherwise it will repeat on the next
     * callable, until there are no more left.
     *
     * @param signame the signal name to register this list of callables on.
     * @param fns the list of Callables to (potentially) call.
     */
    static synchronized void register(String signame, List fns) {
        SignalHandler old = reset_BANG_(signame, fns);
        if (!originalHandlers.containsKey(signame)) {
            originalHandlers.put(signame, old);
        }
    }
}
