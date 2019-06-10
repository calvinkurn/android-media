package com.tokopedia.analytics.debugger;

import java.util.Map;

/**
 * @author okasurya on 5/16/18.
 */
public interface AnalyticsLogger {
    void save(String name, Map<String, Object> data);

    void wipe();

    void openActivity();

    void enableNotification(boolean status);

    boolean isNotificationEnabled();
}
