package com.tokopedia.review.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetReviewDetailResponse(
        @SerializedName("responseText")
        @Expose
        val responseText: String = "",
        @SerializedName("responseTimeFormatted")
        @Expose
        val responseTimeFormatted: String = "",
        @SerializedName("shopName")
        @Expose
        val shopName: String = "",
        @SerializedName("shopID")
        @Expose
        val shopId: String = ""
)