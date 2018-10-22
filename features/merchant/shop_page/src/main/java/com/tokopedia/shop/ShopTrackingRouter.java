package com.tokopedia.shop;

import java.util.Map;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopTrackingRouter {

    void sendEventTrackingShopPage(Map<String, Object> eventTracking);

    void sendScreenName(String screenName);
}
