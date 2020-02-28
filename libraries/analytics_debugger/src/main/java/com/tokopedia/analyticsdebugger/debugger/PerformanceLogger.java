package com.tokopedia.analyticsdebugger.debugger;

import com.tokopedia.analyticsdebugger.debugger.domain.model.PerformanceLogModel;

public interface PerformanceLogger {
    void save(PerformanceLogModel performanceLogModel);

    void wipe();

    void openActivity();

    void enableNotification(boolean status);

    boolean isNotificationEnabled();
}
