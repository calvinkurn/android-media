package com.tokopedia.review.feature.inbox.pending.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.review.feature.inbox.common.data.common.ProductrevProduct
import com.tokopedia.review.feature.inbox.common.data.common.ProductrevTimestamp

data class ProductrevWaitForFeedbackList(
        @SerializedName("reputationID")
        @Expose
        val reputationId: Int = 0,
        @SerializedName("product")
        @Expose
        val product: ProductrevProduct = ProductrevProduct(),
        @SerializedName("timestamp")
        @Expose
        val timestamp: ProductrevTimestamp = ProductrevTimestamp(),
        @SerializedName("status")
        @Expose
        val status: ProductrevWaitForFeedbackStatus = ProductrevWaitForFeedbackStatus()
)