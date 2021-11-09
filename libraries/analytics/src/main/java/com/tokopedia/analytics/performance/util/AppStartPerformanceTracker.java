package com.tokopedia.analytics.performance.util;

import android.util.Log;

public class AppStartPerformanceTracker {
    private static PageLoadTimePerformanceCallback instance;
    public static final String APP_START_COLD = "app_start_cold";
    public static final String APP_START_WARM = "app_start_warm";

    private static boolean isStarted = false;

    public static boolean isColdStart = true;

    public static synchronized PageLoadTimePerformanceCallback getInstance(Boolean reinit){
        if(instance == null || reinit){
            instance = new PageLoadTimePerformanceCallback(
                    "",
                    "",
                    "",
                    0L,
                    0L,
                    0L,
                    0L,
                    null
            );
        }
        return instance;
    }

    public static void startMonitoring() {
        if (!isStarted) {
            if (isColdStart) {
                getInstance(true).startMonitoring(APP_START_COLD);
            } else {
                getInstance(true).startMonitoring(APP_START_WARM);
            }
            isStarted = true;
        }
    }

    public static void stopMonitoring() {
        if (getInstance(false) != null && isStarted) {
            getInstance(false).stopMonitoring();
            isStarted = false;
            isColdStart = false;
        }
    }
}
