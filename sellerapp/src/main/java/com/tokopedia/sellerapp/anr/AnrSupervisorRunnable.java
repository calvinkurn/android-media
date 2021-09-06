package com.tokopedia.sellerapp.anr;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.newrelic.agent.android.NewRelic;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;

import java.util.HashMap;
import java.util.Map;


/**
 * A {@link Runnable} testing the UI thread every 10s until {@link
 * #stop()} is called
 */
public class AnrSupervisorRunnable implements Runnable {

    /**
     * The {@link Handler} to access the UI threads message queue
     */
    private Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * The stop flag
     */
    private boolean mStopped;

    /**
     * Flag indicating the stop was performed
     */
    private boolean mStopCompleted = true;

    @Override
    public void run() {
        this.mStopCompleted = false;

        // Loop until stop() was called or thread is interrupted
        while (!Thread.interrupted()) {
            try {
                // Log
                Log.d(AnrSupervisor.class.getSimpleName(),
                        "Check for ANR...");

                // Create new callback
                AnrSupervisorCallback callback =
                        new AnrSupervisorCallback();

                // Perform test, Handler should run
                // the callback within 1s
                synchronized (callback) {
                    this.mHandler.post(callback);
                    callback.wait(1000);

                    // Check if called
                    if (!callback.isCalled()) {
                        // Log
                        AnrException e = new AnrException(
                                this.mHandler.getLooper().getThread());
                        Map<String, String> anrMap = new HashMap<>();
                        anrMap.put("type", "anr");
                        anrMap.put("exception" ,e.toString());
                        ServerLogger.log(Priority.P1, "DEV_ANR_NR", anrMap);
                        e.logProcessMap();

                        // Wait until the thread responds again
                        callback.wait();

                    } else {
                        Log.d(AnrSupervisor.class.getSimpleName(),
                                "UI Thread responded within 1s");
                    }
                }

                // Check if stopped
                this.checkStopped();

                // Sleep for next test
                Thread.sleep(5000);

            } catch (InterruptedException e) {
                break;

            }
        }

        // Set stop completed flag
        this.mStopCompleted = true;

        // Log
        Log.d(AnrSupervisor.class.getSimpleName(),
                "ANR supervision stopped");

    }

    private synchronized void checkStopped()
            throws InterruptedException {
        if (this.mStopped) {
            // Wait 1000ms
            Thread.sleep(1000);


            // Break if still stopped
            if (this.mStopped) {
                throw new InterruptedException();

            }
        }
    }

    /**
     * Stops the check
     */
    synchronized void stop() {
        Log.d(AnrSupervisor.class.getSimpleName(), "Stopping...");
        this.mStopped = true;

    }

    /**
     * Stops the check
     */
    synchronized void unstopp() {
        Log.d(AnrSupervisor.class.getSimpleName(),
                "Revert stopping...");
        this.mStopped = false;

    }

    /**
     * Returns whether the stop is completed
     *
     * @return true if stop is completed, false if not
     */
    synchronized boolean isStopped() {
        return this.mStopCompleted;

    }
}
