package com.tokopedia.merchantvoucher.analytic;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

import java.util.HashMap;

public class MerchantVoucherTracking {
    public static final String EVENT = "event";
    public static final String EVENT_CATEGORY = "eventCategory";
    public static final String EVENT_ACTION = "eventAction";
    public static final String EVENT_LABEL = "eventLabel";

    public static final String CLICK_SHOP_PAGE = "clickShopPage";
    public static final String MVC_LIST = "mvc list";
    public static final String MVC_DETAIL = "mvc detail";
    public static final String CLICK_MVC_DETAIL = "click mvc detail";
    public static final String CLICK_USE_VOUCHER = "click use voucher";
    public static final String CLICK_SHARE = "click share";

    public MerchantVoucherTracking() {
    }

    protected void sendEvent(String event, String category, String action, String label) {
        HashMap<String, Object> eventMap = createMap(event, category, action, label);
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(eventMap);
    }

    protected HashMap<String, Object> createMap(String event, String category, String action, String label) {
        HashMap<String, Object> eventMap = new HashMap<>();
        eventMap.put(EVENT, event);
        eventMap.put(EVENT_CATEGORY, category);
        eventMap.put(EVENT_ACTION, action);
        eventMap.put(EVENT_LABEL, label);
        return eventMap;
    }

    public void clickMvcDetailFromList() {
        sendEvent(CLICK_SHOP_PAGE,
                MVC_LIST,
                CLICK_MVC_DETAIL,
                "");
    }

    public void clickUseVoucherFromList() {
        sendEvent(CLICK_SHOP_PAGE,
                MVC_LIST,
                CLICK_USE_VOUCHER,
                "");
    }


    public void clickUseVoucherFromDetail() {
        sendEvent(CLICK_SHOP_PAGE,
                MVC_DETAIL,
                CLICK_USE_VOUCHER,
                "");
    }

    public void clickShare() {
        sendEvent(CLICK_SHOP_PAGE,
                MVC_LIST,
                CLICK_SHARE,
                "");
    }


}
