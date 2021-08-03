package com.tokopedia.common_tradein.model;

import com.google.gson.annotations.SerializedName;

public class DeviceDiagParams {
    @SerializedName("ProductId")
    private int productId;
    @SerializedName("DeviceId")
    private String deviceId;
    @SerializedName("NewPrice")
    private int newPrice;
    @SerializedName("TradeInType")
    private int tradeInType;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(int newPrice) {
        this.newPrice = newPrice;
    }

    public int getTradeInType() {
        return tradeInType;
    }

    public void setTradeInType(int tradeInType) {
        this.tradeInType = tradeInType;
    }
}
