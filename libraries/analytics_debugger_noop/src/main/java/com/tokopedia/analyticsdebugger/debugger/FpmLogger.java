package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;

import java.util.Map;


public class FpmLogger {

    private static PerformanceLogger instance;

    public static PerformanceLogger getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new PerformanceLogger() {
            @Override
            public void save(String traceName, long startTime, long endTime, Map<String, String> attributes, Map<String, Long> metrics) {

            }

            @Override
            public void wipe() {

            }

            @Override
            public void openActivity() {

            }

            @Override
            public void enableAutoLogFile(boolean status) {

            }

            @Override
            public boolean isAutoLogFileEnabled() {
                return false;
            }

            @Override
            public void enableNotification(boolean status) {

            }

            @Override
            public boolean isNotificationEnabled() {
                return false;
            }
        };
    }

}
