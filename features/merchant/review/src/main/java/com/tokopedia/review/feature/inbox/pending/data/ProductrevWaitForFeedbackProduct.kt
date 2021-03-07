package com.tokopedia.review.feature.inbox.pending.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevWaitForFeedbackProduct(
        @SerializedName("productID")
        @Expose
        val productId: Long = 0,
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