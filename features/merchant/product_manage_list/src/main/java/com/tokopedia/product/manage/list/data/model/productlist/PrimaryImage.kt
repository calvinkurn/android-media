package com.tokopedia.product.manage.list.data.model.productlist


import com.google.gson.annotations.SerializedName

data class PrimaryImage(
    @SerializedName("original")
    val original: String = "",
    @SerializedName("resize300")
    val resize300: String = "",
    @SerializedName("thumbnail")
    val thumbnail: String = ""
)