package com.tokopedia.gm.subscribe;

import java.util.Map;

public interface GmSubscribeModuleRouter {
    void sendEventTrackingGmSubscribe(Map<String, Object> eventTracking);
}
