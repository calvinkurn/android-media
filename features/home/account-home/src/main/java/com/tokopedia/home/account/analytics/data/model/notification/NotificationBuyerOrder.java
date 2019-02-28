package com.tokopedia.home.account.analytics.data.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/30/18.
 */
public class NotificationBuyerOrder {
    @SerializedName("confirmed")
    @Expose
    private int confirmed = 0;
    @SerializedName("processed")
    @Expose
    private int processed = 0;
    @SerializedName("shipped")
    @Expose
    private int shipped = 0;
    @SerializedName("arrive_at_destination")
    @Expose
    private int arriveAtDestination = 0;

    public int getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(int confirmed) {
        this.confirmed = confirmed;
    }

    public int getProcessed() {
        return processed;
    }

    public void setProcessed(int processed) {
        this.processed = processed;
    }

    public int getShipped() {
        return shipped;
    }

    public void setShipped(int shipped) {
        this.shipped = shipped;
    }

    public int getArriveAtDestination() {
        return arriveAtDestination;
    }

    public void setArriveAtDestination(int arriveAtDestination) {
        this.arriveAtDestination = arriveAtDestination;
    }
}
