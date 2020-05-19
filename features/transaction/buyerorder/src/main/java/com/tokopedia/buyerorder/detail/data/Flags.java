package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.SerializedName;

public class Flags {

    @SerializedName("isOrderCOD")
    private boolean isOrderCOD;
    @SerializedName("isOrderNow")
    private boolean isOrderNow;
    @SerializedName("isOrderTradeIn")
    private boolean isOrderTradeIn;
    @SerializedName("warehouseID")
    private int warehouseID;
    @SerializedName("fulfillBy")
    private int fulfillBy;

    public boolean isIsOrderCOD() {
        return isOrderCOD;
    }

    public void setIsOrderCOD(boolean isOrderCOD) {
        this.isOrderCOD = isOrderCOD;
    }

    public boolean isIsOrderNow() {
        return isOrderNow;
    }

    public void setIsOrderNow(boolean isOrderNow) {
        this.isOrderNow = isOrderNow;
    }

    public boolean isIsOrderTradeIn() {
        return isOrderTradeIn;
    }

    public void setIsOrderTradeIn(boolean isOrderTradeIn) {
        this.isOrderTradeIn = isOrderTradeIn;
    }

    public int getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(int warehouseID) {
        this.warehouseID = warehouseID;
    }

    public int getFulfillBy() {
        return fulfillBy;
    }

    public void setFulfillBy(int fulfillBy) {
        this.fulfillBy = fulfillBy;
    }

}