package com.tokopedia.review.feature.review.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetFormReview(
        @SerializedName("reviewText")
        @Expose
        val reviewText: String = "",
        @SerializedName("isReviewTextEmpty")
        @Expose
        val isReviewTextEmpty: Boolean = false,
        @SerializedName("createTime")
        @Expose
        val createTime: String = "",
        @SerializedName("createTimeFormatted")
        @Expose
        val createTimeFormatted: String = "",
        @SerializedName("attachments")
        @Expose
        val attachments: ProductrevGetFormReviewAttachments = ProductrevGetFormReviewAttachments()
)