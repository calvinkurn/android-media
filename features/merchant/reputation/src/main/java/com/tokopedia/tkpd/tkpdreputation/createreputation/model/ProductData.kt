package com.tokopedia.tkpd.tkpdreputation.createreputation.model


import com.google.gson.annotations.SerializedName

data class ProductData(
    @SerializedName("productID")
    val productID: Int = 0,
    @SerializedName("productImageURL")
    val productImageURL: String = "",
    @SerializedName("productName")
    val productName: String = "",
    @SerializedName("productPageURL")
    val productPageURL: String = "",
    @SerializedName("productStatus")
    val productStatus: Int = 0
)