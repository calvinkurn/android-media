package com.tokopedia.analytics.debugger;

import android.content.Context;

import java.util.Map;

/**
 * @author okasurya on 5/16/18.
 */
public interface AnalyticsLogger {
    void save(Context context, String name, Map<String, Object> data);

    void wipe();

    void openActivity(Context context);

    void enableNotification(Context context, boolean status);

    boolean isNotificationEnabled(Context context);
}
