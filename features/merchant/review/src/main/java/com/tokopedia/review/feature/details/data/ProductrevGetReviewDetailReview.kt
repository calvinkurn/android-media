package com.tokopedia.review.feature.details.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetailReview(
        @SerializedName("feedbackID")
        @Expose
        val feedbackId: Int = 0,
        @SerializedName("rating")
        @Expose
        val rating: Int = 0,
        @SerializedName("reviewText")
        @Expose
        val reviewText: String = "",
        @SerializedName("reviewTime")
        @Expose
        val reviewTime: String = "",
        @SerializedName("reviewTimeFormatted")
        @Expose
        val reviewTimeFormatted: String = "",
        @SerializedName("attachmentsURL")
        @Expose
        val attachments: List<String> = emptyList(),
        @SerializedName("editable")
        @Expose
        val editable: Boolean = false,
        @SerializedName("reviewerData")
        @Expose
        val reviewerData: ProductrevGetReviewDetailReviewerData = ProductrevGetReviewDetailReviewerData()
)