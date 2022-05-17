package com.tokopedia.homenav.mainnav.data.pojo.review


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

data class Product(
    @SerializedName("productID")
    @Expose
    val productID: Long = 0L,
    @SerializedName("productImageURL")
    @Expose
    val productImageURL: String = "",
    @SerializedName("productName")
    @Expose
    val productName: String = ""
)