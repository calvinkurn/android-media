package com.tokopedia.review.feature.inbox.history.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevFeedbackHistoryResponse(
        @SerializedName("list")
        @Expose
        val list: List<ProductrevFeedbackHistory> = emptyList(),
        @SerializedName("hasNext")
        @Expose
        val hasNext: Boolean = false
)