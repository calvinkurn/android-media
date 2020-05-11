package com.tokopedia.analyticsdebugger.debugger;

import java.util.Map;

/**
 * @author okasurya on 5/16/18.
 */
public interface AnalyticsLogger {
    void save(String name, Map<String, Object> data);

    void saveError(String errorData);

    void wipe();

    void openActivity();
    void openErrorActivity();

    void navigateToValidator();

    void enableNotification(boolean status);

    boolean isNotificationEnabled();
}
