package com.tokopedia.track.interfaces;

import android.os.Bundle;

import java.util.Map;

public interface Analytics {
    void sendGeneralEvent(Map<String, Object> value);
    void sendGeneralEvent(String event, String category, String action, String label);
    void sendGeneralEvent(String event, String category, String action, String label, String shopId);

    @Deprecated
    void sendEnhanceEcommerceEvent(Map<String, Object> value);

    void sendEnhanceEcommerceEvent(String eventName, Bundle value);
    void sendScreenAuthenticated(String screenName);
    void sendScreenAuthenticated(String screenName, Map<String, String> customDimension);
    void sendScreenAuthenticated(String screenName, String shopID, String shopType, String pageType, String productId);
    void sendEvent(String eventName, Map<String, Object> eventValue);
    String getDefferedDeeplinkPathIfExists();
}
