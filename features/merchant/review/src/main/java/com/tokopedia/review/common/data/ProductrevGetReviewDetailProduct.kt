package com.tokopedia.review.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetailProduct(
        @SerializedName("productID")
        @Expose
        val productId: Long = 0,
        @SerializedName("productName")
        @Expose
        val productName: String = "",
        @SerializedName("productPageURL")
        @Expose
        val productPageUrl: String = "",
        @SerializedName("productImageURL")
        @Expose
        val productImageUrl: String = "",
        @SerializedName("productVariantName")
        @Expose
        val productVariantName: String = ""
)