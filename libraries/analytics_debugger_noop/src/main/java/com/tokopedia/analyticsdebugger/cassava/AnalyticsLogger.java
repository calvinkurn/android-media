package com.tokopedia.analyticsdebugger.cassava;

import java.util.Map;

/**
 * @author okasurya on 5/16/18.
 */
public interface AnalyticsLogger {

    void save(Map<String, Object> data, String name, String source);

    void enableNotification(boolean status);

    Boolean isNotificationEnabled();

}
