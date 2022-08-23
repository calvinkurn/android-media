package com.tokopedia.homenav.mainnav.data.pojo.review


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Product(
    @SerializedName("productIDStr")
    @Expose
    val productIDStr: String = "",
    @SerializedName("productImageURL")
    @Expose
    val productImageURL: String = "",
    @SerializedName("productName")
    @Expose
    val productName: String = ""
)