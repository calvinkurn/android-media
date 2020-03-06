package com.tokopedia.analyticsdebugger.debugger;

public interface ApplinkLoggerInterface {
    void save(String applink, String trace);

    void wipe();

    void openActivity();

    void enableNotification(boolean status);

    boolean isNotificationEnabled();
}
