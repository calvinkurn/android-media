package com.tokopedia.review.feature.inbox.history.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevFeedbackHistoryReview(
        @SerializedName("feedbackID")
        @Expose
        val feedbackId: Int = 0,
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
        val attachments: List<String> = emptyList()
)