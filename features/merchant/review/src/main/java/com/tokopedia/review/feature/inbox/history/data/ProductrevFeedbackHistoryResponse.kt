package com.tokopedia.review.feature.inbox.history.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevFeedbackHistoryResponse(
        @SerializedName("list")
        @Expose
        val list: List<ProductrevFeedbackHistory> = emptyList(),
        @SerializedName("searchQuery")
        @Expose
        val sortBy: String = "",
        @SerializedName("page")
        @Expose
        val page: Int = 0,
        @SerializedName("limit")
        @Expose
        val limit: Int = 0,
        @SerializedName("hasNext")
        @Expose
        val hasNext: Boolean = false
)