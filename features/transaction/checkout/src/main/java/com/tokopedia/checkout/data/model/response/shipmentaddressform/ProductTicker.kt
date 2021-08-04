package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ProductTicker(
        @SerializedName("show_ticker")
        var isShowTicker: Boolean = false,
        @SerializedName("message")
        var message: String = ""
)