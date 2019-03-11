package com.tokopedia.navigation_common.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author okasurya on 7/26/18.
 */
public class NotificationSellerOrderModel {
    @SerializedName("newOrder")
    @Expose
    private Integer newOrder = 0;
    @SerializedName("readyToShip")
    @Expose
    private Integer readyToShip = 0;
    @SerializedName("shipped")
    @Expose
    private Integer shipped = 0;
    @SerializedName("arriveAtDestination")
    @Expose
    private Integer arriveAtDestination = 0;

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
