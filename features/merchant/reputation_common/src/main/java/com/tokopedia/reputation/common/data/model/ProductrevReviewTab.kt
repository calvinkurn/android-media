package com.tokopedia.reputation.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevReviewTab(
        @SerializedName("count")
        @Expose
        val count: Int = 0,
        @SerializedName("tabName")
        @Expose
        val tabName: String = ""
)