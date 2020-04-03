package com.tokopedia.analyticsdebugger.debugger;

import java.util.Map;

public interface PerformanceLogger {
    void save(String traceName,
              long startTime,
              long endTime,
              Map<String, String> attributes,
              Map<String, Long> metrics);

    void wipe();

    void openActivity();

    void enableNotification(boolean status);

    boolean isNotificationEnabled();

    void enableAutoLogFile(boolean status);

    boolean isAutoLogFileEnabled();
}
