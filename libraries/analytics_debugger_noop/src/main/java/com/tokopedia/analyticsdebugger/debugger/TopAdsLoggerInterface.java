package com.tokopedia.analyticsdebugger.debugger;

public interface TopAdsLoggerInterface {

    boolean isNotificationEnabled();

    void save(String url, String eventType, String sourceName, String productId, String productName, String imageUrl);

    void openActivity();

    void enableNotification(boolean status);

}
