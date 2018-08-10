package com.tokopedia.attachproduct.view.tracking;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hendri on 08/08/18.
 */
public class AttachProductEventTracking {
    protected Map<String, Object> eventTracking = new HashMap<>();

    public AttachProductEventTracking() {
    }

    public AttachProductEventTracking(String event, String category, String action, String label) {
        Log.d("GAv4", "EventTracking: " + event + " " + category + " " + action + " " + label
                + " ");
        this.eventTracking.put("event", event);
        this.eventTracking.put("eventCategory", category);
        this.eventTracking.put("eventAction", action);
        this.eventTracking.put("eventLabel", label);
    }

    public void setEvent(String event) {

        eventTracking.put("event", event);
    }

    public void setEventCategory(String eventCategory) {
        eventTracking.put("eventCategory", eventCategory);
    }

    public void setEventAction(String eventAction) {
        eventTracking.put("eventAction", eventAction);
    }

    public void setEventLabel(String eventLabel) {
        eventTracking.put("eventLabel", eventLabel);
    }

    public Map<String, Object> getEvent() {
        return this.eventTracking;
    }

//    public EventTracking setUserId() {
//        this.eventTracking.put(AppEventTracking.CustomDimension.USER_ID, TextUtils.isEmpty
//                (SessionHandler.getLoginID(MainApplication
//                        .getAppContext())) ? "0" : SessionHandler.getLoginID(MainApplication
//                .getAppContext()));
//        return this;
//    }
//
//    public EventTracking setShopType(boolean isGoldMerchant, boolean isOfficialStore) {
//        this.eventTracking.put(AppEventTracking.CustomDimension.SHOP_TYPE,
//                isOfficialStore? AppEventTracking.ShopType.OFFICIAL_STORE :
//                        isGoldMerchant? AppEventTracking.ShopType.GOLD_MERCHANT :
//                                AppEventTracking.ShopType.REGULAR);
//        return this;
//    }
//
//    public EventTracking setShopId(String shopId) {
//        this.eventTracking.put(AppEventTracking.CustomDimension.SHOP_ID, shopId);
//        return this;
//    }
//
//    public EventTracking setCustomEvent(String key, String value) {
//        this.eventTracking.put(key, value);
//        return this;
//    }
//
//    public EventTracking setCustomDimension(HashMap<String, String> customDimension) {
//        this.eventTracking.putAll(customDimension);
//        return this;
//    }
}
