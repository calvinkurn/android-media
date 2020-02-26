package com.tokopedia.analytics.debugger;

import com.tokopedia.analytics.performance.PerformanceLogModel;

import java.util.Map;

public interface PerformanceLogger {
    void save(PerformanceLogModel performanceLogModel);

    void wipe();

    void openActivity();

    void enableNotification(boolean status);

    boolean isNotificationEnabled();
}
