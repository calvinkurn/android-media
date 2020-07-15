package com.tokopedia.review.feature.historydetails.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetailResponse(
        @SerializedName("responseText")
        @Expose
        val responseText: String = "",
        @SerializedName("responseTime")
        @Expose
        val responseTime: String = "",
        @SerializedName("responseTimeFormatted")
        @Expose
        val responseTimeFormatted: String = "",
        @SerializedName("responderData")
        @Expose
        val responderData: ProductrevGetReviewDetailResponderData = ProductrevGetReviewDetailResponderData()
)