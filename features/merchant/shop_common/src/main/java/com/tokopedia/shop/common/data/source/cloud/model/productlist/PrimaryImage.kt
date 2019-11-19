package com.tokopedia.shop.common.data.source.cloud.model.productlist


import com.google.gson.annotations.SerializedName

data class PrimaryImage(
    @SerializedName("original")
    val original: String = "",
    @SerializedName("resize300")
    val resize300: String = "",
    @SerializedName("thumbnail")
    val thumbnail: String = ""
)