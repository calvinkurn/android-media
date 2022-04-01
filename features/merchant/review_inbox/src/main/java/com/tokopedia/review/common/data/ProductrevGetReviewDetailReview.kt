package com.tokopedia.review.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetailReview(
        @SerializedName("feedbackIDStr")
        @Expose
        val feedbackId: String = "",
        @SerializedName("rating")
        @Expose
        val rating: Int = 0,
        @SerializedName("reviewText")
        @Expose
        val reviewText: String = "",
        @SerializedName("reviewTimeFormatted")
        @Expose
        val reviewTimeFormatted: String = "",
        @SerializedName("attachmentsURL")
        @Expose
        val attachments: List<ProductrevReviewAttachment> = emptyList(),
        @SerializedName("editable")
        @Expose
        val editable: Boolean = false,
        @SerializedName("sentAsAnonymous")
        @Expose
        val sentAsAnonymous: Boolean = false,
        @SerializedName("reviewerName")
        @Expose
        val reviewerName: String = "",
        @SerializedName("badRatingReasonFmt")
        @Expose
        val badRatingReasonFmt: String = "",
        @SerializedName("ratingDisclaimer")
        @Expose
        val ratingDisclaimer: String = "",
        @SerializedName("editDisclaimer")
        @Expose
        val editDisclaimer: String = ""
)