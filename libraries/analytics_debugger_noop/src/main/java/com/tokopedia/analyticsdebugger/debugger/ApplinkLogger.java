package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;


public class ApplinkLogger {

    private static ApplinkLoggerInterface instance;

    public static ApplinkLoggerInterface getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new ApplinkLoggerInterface() {
            @Override
            public void save(String applink, String trace) {

            }

            @Override
            public void wipe() {

            }

            @Override
            public void openActivity() {

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
