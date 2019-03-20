package com.tokopedia.track.interfaces;

import java.util.Map;

public interface Analytics {
    void sendGeneralEvent(Map<String, Object> value);
    void sendGeneralEvent(String event, String category, String action, String label);
    void sendEnhanceECommerceEvent(Map<String, Object> value);
    void sendScreenAuthenticated(String screenName);
    void sendScreenAuthenticated(String screenName, Map<String, String> customDimension);
    void sendScreenAuthenticated(String screenName, String shopID, String shopType, String pageType, String productId);
    void sendEvent(String eventName, Map<String, Object> eventValue);
    String getId();
}
