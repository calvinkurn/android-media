package com.tokopedia.review.feature.createreputation.model


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
    @SerializedName("productVariant")
    val productVariant: ProductVariant = ProductVariant()
)