package com.tokopedia.home.account.analytics.data.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/30/18.
 */
public class NotificationSellerOrder {
    @SerializedName("new_order")
    @Expose
    private int newOrder = 0;
    @SerializedName("ready_to_ship")
    @Expose
    private int readyToShip = 0;
    @SerializedName("shipped")
    @Expose
    private int shipped = 0;
    @SerializedName("arrive_at_destination")
    @Expose
    private int arriveAtDestination = 0;

    public int getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(int newOrder) {
        this.newOrder = newOrder;
    }

    public int getReadyToShip() {
        return readyToShip;
    }

    public void setReadyToShip(int readyToShip) {
        this.readyToShip = readyToShip;
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
