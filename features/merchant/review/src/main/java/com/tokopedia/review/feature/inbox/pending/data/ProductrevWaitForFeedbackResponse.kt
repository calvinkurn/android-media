package com.tokopedia.review.feature.inbox.pending.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevWaitForFeedbackResponse (
        @SerializedName("list")
        @Expose
        val list: List<ProductrevWaitForFeedback> = listOf(),
        @SerializedName("searchQuery")
        @Expose
        val searchQuery: String = "",
        @SerializedName("page")
        @Expose
        val page: Int = 0,
        @SerializedName("limit")
        @Expose
        val limit: Int = 0,
        @SerializedName("hasNext")
        @Expose
        val hasNext: Int = 0
)