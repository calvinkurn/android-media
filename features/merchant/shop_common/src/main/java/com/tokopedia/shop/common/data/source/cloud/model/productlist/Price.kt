package com.tokopedia.shop.common.data.source.cloud.model.productlist


import com.google.gson.annotations.SerializedName

data class Price(
    @SerializedName("currency_id")
    val currencyId: Int = 0,
    @SerializedName("currency_text")
    val currencyText: String = "",
    @SerializedName("identifier")
    val identifier: String = "",
    @SerializedName("text")
    val text: String = "",
    @SerializedName("text_idr")
    val textIdr: String = "",
    @SerializedName("value")
    val value: Int = 0,
    @SerializedName("value_idr")
    val valueIdr: Int = 0
)