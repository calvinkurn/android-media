package com.tokopedia.transactiondata.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 05/03/19.
 */

public class TradeInInfo {

    @SerializedName("is_valid_trade_in")
    @Expose
    public boolean isValidTradeIn = true;

    @SerializedName("new_device_price")
    @Expose
    public int newDevicePrice = 799;

    @SerializedName("new_device_price_fmt")
    @Expose
    public String newDevicePriceFmt = "";

    @SerializedName("old_device_price")
    @Expose
    public int oldDevicePrice = 100;

    @SerializedName("old_device_price_fmt")
    @Expose
    public String oldDevicePriceFmt = "";

    public boolean isValidTradeIn() {
        return isValidTradeIn;
    }

    public int getNewDevicePrice() {
        return newDevicePrice;
    }

    public String getNewDevicePriceFmt() {
        return newDevicePriceFmt;
    }

    public int getOldDevicePrice() {
        return oldDevicePrice;
    }

    public String getOldDevicePriceFmt() {
        return oldDevicePriceFmt;
    }
}
