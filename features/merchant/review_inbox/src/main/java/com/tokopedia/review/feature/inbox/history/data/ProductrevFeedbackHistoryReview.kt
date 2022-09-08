package com.tokopedia.review.feature.inbox.history.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.review.common.data.ProductrevReviewImageAttachment
import com.tokopedia.review.common.data.ProductrevReviewVideoAttachment

data class ProductrevFeedbackHistoryReview(
        @SerializedName("feedbackIDStr")
        @Expose
        val feedbackId: String = "",
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
        val imageAttachments: List<ProductrevReviewImageAttachment> = emptyList(),
        @SerializedName("video")
        @Expose
        val videoAttachments: List<ProductrevReviewVideoAttachment> = emptyList(),
        @SerializedName("badRatingReasonFmt")
        @Expose
        val badRatingReason: String = ""
)