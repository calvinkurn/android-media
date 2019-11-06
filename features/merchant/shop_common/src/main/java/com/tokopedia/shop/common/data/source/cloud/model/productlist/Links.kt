package com.tokopedia.shop.common.data.source.cloud.model.productlist


import com.google.gson.annotations.SerializedName

data class Links(
    @SerializedName("next")
    val next: String = "",
    @SerializedName("prev")
    val prev: String = "",
    @SerializedName("self")
    val self: String = ""
)