package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class NotificationSalesModel {
    @SerializedName("newOrder")
    @Expose
    private Integer newOrder;
    @SerializedName("shippingConfirm")
    @Expose
    private Integer shippingConfirm;
    @SerializedName("shippingStatus")
    @Expose
    private Integer shippingStatus;

    public Integer getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(Integer newOrder) {
        this.newOrder = newOrder;
    }

    public Integer getShippingConfirm() {
        return shippingConfirm;
    }

    public void setShippingConfirm(Integer shippingConfirm) {
        this.shippingConfirm = shippingConfirm;
    }

    public Integer getShippingStatus() {
        return shippingStatus;
    }

    public void setShippingStatus(Integer shippingStatus) {
        this.shippingStatus = shippingStatus;
    }
}
