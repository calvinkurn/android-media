package com.tokopedia.review.feature.inbox.pending.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.review.feature.inbox.common.data.common.ProductrevTimestamp

data class ProductrevWaitForFeedback(
        @SerializedName("reputationID")
        @Expose
        val reputationId: Long = 0,
        @SerializedName("inboxReviewID")
        @Expose
        val inboxReviewId: Long = 0,
        @SerializedName("product")
        @Expose
        val product: ProductrevWaitForFeedbackProduct = ProductrevWaitForFeedbackProduct(),
        @SerializedName("timestamp")
        @Expose
        val timestamp: ProductrevTimestamp = ProductrevTimestamp(),
        @SerializedName("status")
        @Expose
        val status: ProductrevWaitForFeedbackStatus = ProductrevWaitForFeedbackStatus()
)