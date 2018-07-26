package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/26/18.
 */
public class NotificationBuyerOrderModel {
    @SerializedName("confirmed")
    @Expose
    private Integer confirmed;
    @SerializedName("processed")
    @Expose
    private Integer processed;
    @SerializedName("shipped")
    @Expose
    private Integer shipped;
    @SerializedName("arriveAtDestination")
    @Expose
    private Integer arriveAtDestination;

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
