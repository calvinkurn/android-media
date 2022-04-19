package com.tokopedia.review.feature.inbox.history.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevFeedbackHistoryProduct(
        @SerializedName("productIDStr")
        @Expose
        val productId: String = "",
        @SerializedName("productName")
        @Expose
        val productName: String = "",
        @SerializedName("productVariantName")
        @Expose
        val productVariantName: String = ""
)