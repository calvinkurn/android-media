package com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by okasurya on 12/8/17.
 */

public class MarketplaceTrackerData {
    @SerializedName("event")
    @Expose
    private String event;
    @SerializedName("payment_id")
    @Expose
    private String paymentId;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("shop_type")
    @Expose
    private String shopType;
    @SerializedName("logistic_type")
    @Expose
    private String logisticType;
    @SerializedName("ecommerce")
    @Expose
    private Ecommerce ecommerce;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getLogisticType() {
        return logisticType;
    }

    public void setLogisticType(String logisticType) {
        this.logisticType = logisticType;
    }

    public Ecommerce getEcommerce() {
        return ecommerce;
    }

    public void setEcommerce(Ecommerce ecommerce) {
        this.ecommerce = ecommerce;
    }
}
