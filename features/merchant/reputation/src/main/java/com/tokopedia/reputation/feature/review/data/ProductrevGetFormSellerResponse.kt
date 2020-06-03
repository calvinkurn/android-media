package com.tokopedia.reputation.feature.review.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevGetFormSellerResponse(
        @SerializedName("responseText")
        @Expose
        val responseText: String = "",
        @SerializedName("createTime")
        @Expose
        val createTime: String = "",
        @SerializedName("createTimeFormatted")
        @Expose
        val createTimeFormatted: String = ""
)