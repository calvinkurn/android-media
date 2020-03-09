package com.tokopedia.analyticsdebugger.debugger;

public interface ApplinkLoggerInterface {

    void startTrace(String applink);

    void appendTrace(String trace);

    void save();

    void wipe();

    void openActivity();

    void enableNotification(boolean status);

    boolean isNotificationEnabled();
}
