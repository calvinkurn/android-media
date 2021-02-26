package com.tokopedia.review.feature.createreputation.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductData(
    @SerializedName("productID")
    @Expose
    val productID: Long = 0,
    @SerializedName("productImageURL")
    @Expose
    val productImageURL: String = "",
    @SerializedName("productName")
    @Expose
    val productName: String = "",
    @SerializedName("productPageURL")
    @Expose
    val productPageURL: String = "",
    @SerializedName("productVariant")
    @Expose
    val productVariant: ProductVariant = ProductVariant(),
    @SerializedName("productStatus")
    @Expose
    val productStatus: Int = 0
)