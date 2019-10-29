package com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 05/03/19.
 */

public class TradeInInfo {

    @SerializedName("is_valid_trade_in")
    @Expose
    public boolean isValidTradeIn;

    @SerializedName("new_device_price")
    @Expose
    public int newDevicePrice;

    @SerializedName("new_device_price_fmt")
    @Expose
    public String newDevicePriceFmt = "";

    @SerializedName("old_device_price")
    @Expose
    public int oldDevicePrice;

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
