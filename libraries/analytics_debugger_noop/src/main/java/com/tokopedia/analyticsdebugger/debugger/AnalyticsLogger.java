package com.tokopedia.analyticsdebugger.debugger;

import com.tokopedia.analyticsdebugger.AnalyticsSource;

import java.util.Map;

/**
 * @author okasurya on 5/16/18.
 */
public interface AnalyticsLogger {
    void save(Map<String, Object> data, String name,@AnalyticsSource String source);

    void enableNotification(boolean status);

    Boolean isNotificationEnabled();

}
