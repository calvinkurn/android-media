package com.tokopedia.review.feature.inbox.pending.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.review.common.data.ProductrevTimestamp

data class ProductrevWaitForFeedback(
        @SerializedName("reputationIDStr")
        @Expose
        val reputationIDStr: String = "",
        @SerializedName("inboxReviewIDStr")
        @Expose
        val inboxReviewIDStr: String = "",
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