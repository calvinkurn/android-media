package com.tokopedia.review.feature.inbox.common.data.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevTimestamp(
        @SerializedName("createTime")
        @Expose
        val createTime: String = "",
        @SerializedName("createTimeFormatted")
        @Expose
        val createTimeFormatted: String = ""
)