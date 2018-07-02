package com.tokopedia.analytics.debugger;

import android.content.Context;

import java.util.Map;

/**
 * @author okasurya on 5/16/18.
 */
public class GtmLogger implements AnalyticsLogger {
    private static AnalyticsLogger instance;

    private GtmLogger() {
    }

    public static AnalyticsLogger getInstance() {
        if (instance == null) {
            instance = new GtmLogger();
        }

        return instance;
    }

    @Override
    public void save(Context context, String name, Map<String, Object> mapData) {

    }

    @Override
    public void wipe() {

    }

    @Override
    public void openActivity(Context context) {

    }

    @Override
    public void enableNotification(Context context, boolean isEnabled) {

    }

    @Override
    public boolean isNotificationEnabled(Context context) {
        return false;
    }
}
