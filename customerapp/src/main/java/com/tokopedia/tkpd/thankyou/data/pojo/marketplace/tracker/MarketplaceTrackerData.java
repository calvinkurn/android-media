package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by okasurya on 12/8/17.
 */

public class MarketplaceTrackerData extends HashMap<String, Object> {
    private static final String EVENT = "event";
    private static final String PAYMENT_ID = "payment_id";
    private static final String PAYMENT_STATUS = "payment_status";
    private static final String PAYMENT_TYPE = "payment_type";
    private static final String SHOP_ID = "shop_id";
    private static final String SHOP_TYPE = "shopType";
    private static final String LOGISTIC_TYPE = "logistic_type";
    private static final String ECOMMERCE = "ecommerce";
    private static final String USER_ID = "userId";

    public void setEvent(String event) {
        this.put(EVENT, event);
    }

    public void setPaymentId(String paymentId) {
        this.put(PAYMENT_ID, paymentId);
    }

    public void setPaymentStatus(String paymentStatus) {
        this.put(PAYMENT_STATUS, paymentStatus);
    }

    public void setPaymentType(String paymentType) {
        this.put(PAYMENT_TYPE, paymentType);
    }

    public void setShopId(String shopId) {
        this.put(SHOP_ID, shopId);
    }

    public void setShopType(String shopType) {
        this.put(SHOP_TYPE, shopType);
    }

    public void setLogisticType(String logisticType) {
        this.put(LOGISTIC_TYPE, logisticType);
    }

    public void setUserId(String userId) {
        this.put(USER_ID, userId);
    }

    public void setEcommerce(Map<String, Object> ecommerce) {
        this.put(ECOMMERCE, ecommerce);
    }
}
