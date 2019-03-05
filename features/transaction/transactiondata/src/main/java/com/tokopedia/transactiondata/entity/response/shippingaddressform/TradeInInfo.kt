package com.tokopedia.transactiondata.entity.response.shippingaddressform

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 05/03/19.
 */

data class TradeInInfo(
        @SerializedName("is_valid_tradein")
        @Expose
        val isValidTradeIn: Boolean = false,

        @SerializedName("new_device_price")
        @Expose
        val newDevicePrice: Int = 0,

        @SerializedName("new_device_price_fmt")
        @Expose
        val newDevicePriceFmt: String = "",

        @SerializedName("old_device_price")
        @Expose
        val oldDevicePrice: Int = 0,

        @SerializedName("old_device_price_fmt")
        @Expose
        val oldDevicePriceFmt: String = ""
)