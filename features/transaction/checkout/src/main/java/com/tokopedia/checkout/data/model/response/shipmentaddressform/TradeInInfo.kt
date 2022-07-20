package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class TradeInInfo(
        @SerializedName("is_valid_trade_in")
        var isValidTradeIn: Boolean = false,
        @SerializedName("new_device_price")
        var newDevicePrice: Int = 0,
        @SerializedName("new_device_price_fmt")
        var newDevicePriceFmt: String = "",
        @SerializedName("old_device_price")
        var oldDevicePrice: Int = 0,
        @SerializedName("old_device_price_fmt")
        var oldDevicePriceFmt: String = "",
        @SerializedName("drop_off_enable")
        var isDropOffEnable: Boolean = false,
        @SerializedName("device_model")
        var deviceModel: String = "",
        @SerializedName("diagnostic_id")
        var diagnosticId: String = "",
)