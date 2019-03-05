package com.tokopedia.transactiondata.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Irfan Khoirul on 05/03/19.
 */

public class TradeInInfo {

    @SerializedName("is_valid_tradein")
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

}
