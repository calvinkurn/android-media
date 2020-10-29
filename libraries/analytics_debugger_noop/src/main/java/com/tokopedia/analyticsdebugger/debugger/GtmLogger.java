package com.tokopedia.analyticsdebugger.debugger;

import android.content.Context;

import java.util.Map;

/**
 * @author okasurya on 5/16/18.
 */
public class GtmLogger {

    private static AnalyticsLogger instance;

    public static AnalyticsLogger getInstance(Context context) {
        if (instance == null) {
            instance = new AnalyticsLogger() {
                @Override
                public void save(String name, Map<String, Object> data, String source) {

                }

                @Override
                public void saveError(String errorData) {

                }

                @Override
                public void wipe() {

                }

                @Override
                public void openActivity() {

                }

                @Override
                public void openErrorActivity() {

                }

                @Override
                public void navigateToValidator() {

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
