package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/21/18.
 */
public class NotificationPurchaseModel {
    @SerializedName("deliveryConfirm")
    @Expose
    private Integer deliveryConfirm;
    @SerializedName("orderStatus")
    @Expose
    private Integer orderStatus;
    @SerializedName("paymentConfirm")
    @Expose
    private Integer paymentConfirm;
    @SerializedName("reorder")
    @Expose
    private Integer reorder;

    public Integer getDeliveryConfirm() {
        return deliveryConfirm;
    }

    public void setDeliveryConfirm(Integer deliveryConfirm) {
        this.deliveryConfirm = deliveryConfirm;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPaymentConfirm() {
        return paymentConfirm;
    }

    public void setPaymentConfirm(Integer paymentConfirm) {
        this.paymentConfirm = paymentConfirm;
    }

    public Integer getReorder() {
        return reorder;
    }

    public void setReorder(Integer reorder) {
        this.reorder = reorder;
    }
}
