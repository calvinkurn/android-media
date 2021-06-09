package com.tokopedia.review.feature.inbox.history.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.review.common.data.ProductrevReviewAttachment

data class ProductrevFeedbackHistoryReview(
        @SerializedName("feedbackID")
        @Expose
        val feedbackId: Long = 0,
        @SerializedName("userName")
        @Expose
        val userName: String = "",
        @SerializedName("rating")
        @Expose
        val rating: Int = 0,
        @SerializedName("reviewText")
        @Expose
        val reviewText: String = "",
        @SerializedName("attachmentsURL")
        @Expose
        val attachments: List<ProductrevReviewAttachment> = emptyList()
)