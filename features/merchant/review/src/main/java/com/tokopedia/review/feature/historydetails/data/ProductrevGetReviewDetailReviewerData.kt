package com.tokopedia.review.feature.historydetails.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetailReviewerData(
        @SerializedName("isAnonym")
        @Expose
        val isAnonym: Boolean = false,
        @SerializedName("fullName")
        @Expose
        val fullName: String = ""
)