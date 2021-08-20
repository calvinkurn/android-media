package com.tokopedia.sellerapp.anr;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Map;

/**
 * A {@link Exception} to represent an ANR. This {@link Exception}'s
 * stack trace will be the current stack trace of the given
 * {@link Thread}
 */
public class AnrException extends Exception {

    /**
     * Creates a new instance
     *
     * @param thread the {@link Thread} which is not repsonding
     */
    public AnrException(Thread thread) {
        super("ANR detected");

        // Copy the Thread's stack,
        // so the Exception seams to occure there
        this.setStackTrace(thread.getStackTrace());

    }

    /**
     * Logs the current process and all its threads
     */
    public void logProcessMap() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        this.printProcessMap(ps);
        Log.i(this.getClass().getSimpleName(),
                new String(bos.toByteArray()));

    }

    /**
     * Prints the current process and all its threads
     *
     * @param ps the {@link PrintStream} to which the
     *           info is written
     */
    public void printProcessMap(PrintStream ps) {
        // Get all stack traces in the system
        Map<Thread, StackTraceElement[]> stackTraces =
                Thread.getAllStackTraces();

        ps.println("Process map:");

        for (Thread thread : stackTraces.keySet()) {
            if (stackTraces.get(thread).length > 0) {
                this.printThread(ps, Locale.getDefault(),
                        thread, stackTraces.get(thread));
                ps.println();

            }
        }
    }

    /**
     * Prints the given thread
     * @param ps the {@link PrintStream} to which the
     *          info is written
     * @param l the {@link Locale} to use
     * @param thread the {@link Thread} to print
     * @param stack the {@link Thread}'s stack trace
     */
    private void printThread(PrintStream ps, Locale l,
                             Thread thread, StackTraceElement[] stack) {
        ps.println(String.format(l, "\t%s (%s)",
                thread.getName(), thread.getState()));

        for (StackTraceElement element : stack) {
            ps.println(String.format(l, "\t\t%s.%s(%s:%d)",
                    element.getClassName(),
                    element.getMethodName(),
                    element.getFileName(),
                    element.getLineNumber()));

        }
    }
}
