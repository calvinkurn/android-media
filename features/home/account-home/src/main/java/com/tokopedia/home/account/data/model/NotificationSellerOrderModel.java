package com.tokopedia.home.account.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/26/18.
 */
public class NotificationSellerOrderModel {
    @SerializedName("newOrder")
    @Expose
    private Integer newOrder;
    @SerializedName("readyToShip")
    @Expose
    private Integer readyToShip;
    @SerializedName("shipped")
    @Expose
    private Integer shipped;
    @SerializedName("arriveAtDestination")
    @Expose
    private Integer arriveAtDestination;

    public Integer getNewOrder() {
        return newOrder;
    }

    public void setNewOrder(Integer newOrder) {
        this.newOrder = newOrder;
    }

    public Integer getReadyToShip() {
        return readyToShip;
    }

    public void setReadyToShip(Integer readyToShip) {
        this.readyToShip = readyToShip;
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
