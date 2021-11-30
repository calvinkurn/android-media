package com.tokopedia.review.feature.inbox.history.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.review.common.data.ProductrevTimestamp

data class ProductrevFeedbackHistory(
        @SerializedName("product")
        @Expose
        val product: ProductrevFeedbackHistoryProduct = ProductrevFeedbackHistoryProduct(),
        @SerializedName("timestamp")
        @Expose
        val timestamp: ProductrevTimestamp = ProductrevTimestamp(),
        @SerializedName("status")
        @Expose
        val status: ProductrevFeedbackHistoryStatus = ProductrevFeedbackHistoryStatus(),
        @SerializedName("review")
        @Expose
        val review: ProductrevFeedbackHistoryReview = ProductrevFeedbackHistoryReview()
)