package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;


public class ApplinkLogger {

    private static ApplinkLoggerInterface instance;

    public static ApplinkLoggerInterface getInstance(Context context) {
        if (instance == null) {
            instance = new ApplinkLoggerInterface() {
                @Override
                public void startTrace(String applink) {

                }

                @Override
                public void appendTrace(String trace) {

                }

                @Override
                public void save() {

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

        return instance;
    }

}
