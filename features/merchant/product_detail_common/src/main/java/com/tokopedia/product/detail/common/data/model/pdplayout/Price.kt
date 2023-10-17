package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.SerializedName

data class Price(
    @SerializedName("currency")
    val currency: String = "",
    @SerializedName("lastUpdateUnix")
    val lastUpdateUnix: String = "",
    @SerializedName("value")
    val value: Double = 0.0,
    @SerializedName("priceFmt")
    val priceFmt: String = "",
    @SerializedName("slashPriceFmt")
    val slashPriceFmt: String = "",
    @SerializedName("discPercentage")
    val discPercentage: String = ""
)
