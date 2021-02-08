package com.tokopedia.analytics.performance.util;

public class SplashScreenPerformanceTracker {
    private static PageLoadTimePerformanceCallback instance;
    public static final String SPLASH_DURATION_WARM = "mp_splash_duration_warm";
    public static final String SPLASH_DURATION_COLD = "mp_splash_duration_cold";

    private static boolean isStarted = false;

    public static boolean isColdStart = false;

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
                getInstance(true).startMonitoring(SPLASH_DURATION_COLD);
            } else {
                getInstance(true).startMonitoring(SPLASH_DURATION_WARM);
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
