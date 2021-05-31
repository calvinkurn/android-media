package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName

data class Price(
        @SerializedName("currency")
        val currency: String = "",
        @SerializedName("lastUpdateUnix")
        val lastUpdateUnix: String = "",
        @SerializedName("value")
        val value: Int = 0
) {
    var priceFmt: String = ""
}