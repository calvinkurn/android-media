package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/26/18.
 */
public class NotificationBuyerOrderModel {
    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus = "";
    @SerializedName("confirmed")
    @Expose
    private Integer confirmed = 0;
    @SerializedName("processed")
    @Expose
    private Integer processed = 0;
    @SerializedName("shipped")
    @Expose
    private Integer shipped = 0;
    @SerializedName("arriveAtDestination")
    @Expose
    private Integer arriveAtDestination = 0;

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Integer confirmed) {
        this.confirmed = confirmed;
    }

    public Integer getProcessed() {
        return processed;
    }

    public void setProcessed(Integer processed) {
        this.processed = processed;
    }

    public Integer getShipped() {
        return shipped;
    }

    public void setShipped(Integer shipped) {
        this.shipped = shipped;
    }

    public Integer getArriveAtDestination() {
        return arriveAtDestination;
    }

    public void setArriveAtDestination(Integer arriveAtDestination) {
        this.arriveAtDestination = arriveAtDestination;
    }
}
