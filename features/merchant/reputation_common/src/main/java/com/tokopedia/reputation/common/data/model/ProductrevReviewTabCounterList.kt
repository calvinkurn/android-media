package com.tokopedia.reputation.common.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevReviewTabCounterList(
        @SerializedName("list")
        @Expose
        val tabs: List<ProductrevReviewTab> = listOf()
)