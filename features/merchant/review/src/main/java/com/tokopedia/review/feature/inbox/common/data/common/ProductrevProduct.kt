package com.tokopedia.review.feature.inbox.common.data.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevProduct(
        @SerializedName("productID")
        @Expose
        val productId: Int = 0,
        @SerializedName("productName")
        @Expose
        val productName: String = "",
        @SerializedName("productImageURL")
        @Expose
        val productImageUrl: String = "",
        @SerializedName("productVariantName")
        @Expose
        val productVariantName: String = ""
)