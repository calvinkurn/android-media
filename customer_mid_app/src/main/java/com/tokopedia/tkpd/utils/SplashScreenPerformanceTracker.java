package com.tokopedia.tkpd.utils;

import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceCallback;

public class SplashScreenPerformanceTracker {
    private static PageLoadTimePerformanceCallback instance;
    public static final String SPLASH_DURATION_WARM = "mp_splash_duration_warm";
    public static final String SPLASH_DURATION_COLD = "mp_splash_duration_cold";

    private static boolean isStarted = false;

    public static synchronized PageLoadTimePerformanceCallback getInstance(){
        if(instance == null){
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

    public static void startMonitoring(Boolean isFromApplication) {
        if (getInstance() != null && !isStarted) {
            if (isFromApplication) {
                getInstance().startMonitoring(SPLASH_DURATION_COLD);
            } else {
                getInstance().startMonitoring(SPLASH_DURATION_WARM);
            }
            isStarted = true;
        }
    }

    public static void stopMonitoring() {
        if (getInstance() != null && isStarted) {
            getInstance().stopMonitoring();
        }
    }
}
