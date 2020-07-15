package com.tokopedia.review.feature.historydetails.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetailResponderData(
        @SerializedName("shopName")
        @Expose
        val shopName: String = ""
)