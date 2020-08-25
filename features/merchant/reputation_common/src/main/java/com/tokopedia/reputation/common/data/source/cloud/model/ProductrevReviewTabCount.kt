package com.tokopedia.reputation.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevReviewTabCount(
        @SerializedName("count")
        @Expose
        val count: Int = 0
)