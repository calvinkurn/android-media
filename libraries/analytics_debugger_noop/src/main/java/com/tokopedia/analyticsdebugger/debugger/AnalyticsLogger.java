package com.tokopedia.analyticsdebugger.debugger;

import com.tokopedia.analyticsdebugger.AnalyticsSource;

import java.util.Map;

/**
 * @author okasurya on 5/16/18.
 */
public interface AnalyticsLogger {
    void save(String name, Map<String, Object> data,@AnalyticsSource String source);

    void saveError(String errorData);

    void enableNotification(boolean status);

    boolean isNotificationEnabled();
}
