package com.tokopedia.shop;

import java.util.Map;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopTrackingRouter {

    void sendEventTracking(Map<String, Object> eventTracking);

    void sendScreenName(String screenName);
}
