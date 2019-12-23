package com.tokopedia.merchantvoucher.analytic;

import com.tokopedia.track.TrackApp;

import java.util.HashMap;

public class MerchantVoucherTracking {
    private static final String EVENT = "event";
    private static final String EVENT_CATEGORY = "eventCategory";
    private static final String EVENT_ACTION = "eventAction";
    private static final String EVENT_LABEL = "eventLabel";

    private static final String CLICK_PDP = "clickPDP";
    private static final String PDP = "product detail page";
    private static final String CLICK_MVC_DETAIL = "click - mvc list - mvc detail";
    private static final String CLICK_MVC_USE_VOUCHER = "click - mvc list - use voucher";
    private static final String CLICK_MVC_SHARE = "click - mvc list - share - facebook";
    private static final String CLICK_USE_VOUCHER = "click - mvc list - mvc detail - use voucher";
    private static final String PROMO_ID = "promoId";

    public MerchantVoucherTracking() {
    }

    protected HashMap<String, Object> createMap(String event, String category, String action, String label) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(EVENT, event);
        eventMap.put(EVENT_CATEGORY, category);
        eventMap.put(EVENT_ACTION, action);
        eventMap.put(EVENT_LABEL, label);
        return eventMap;
    }

    public void clickMvcDetailFromList(String voucherId) {
        HashMap<String, Object> eventMap = createMap(CLICK_PDP, PDP, CLICK_MVC_DETAIL, "");
        eventMap.put(PROMO_ID, voucherId);

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventMap);
    }

    public void clickUseVoucherFromList(String voucherId) {
        HashMap<String, Object> eventMap = createMap(CLICK_PDP, PDP, CLICK_MVC_USE_VOUCHER, "");
        eventMap.put(PROMO_ID, voucherId);

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventMap);
    }


    public void clickUseVoucherFromDetail(String voucherId) {
        HashMap<String, Object> eventMap = createMap(CLICK_PDP, PDP, CLICK_USE_VOUCHER, "");
        eventMap.put(PROMO_ID, voucherId);

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventMap);
    }

    public void clickShare() {
        HashMap<String, Object> eventMap = createMap(CLICK_PDP, PDP, CLICK_MVC_SHARE, "");

        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(eventMap);
    }
}
