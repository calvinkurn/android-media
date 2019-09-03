package com.tokopedia.purchase_platform.features.cart.data.model.response


import com.google.gson.annotations.SerializedName

data class TickerData(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("page")
    val page: String
)