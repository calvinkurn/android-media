package com.tokopedia.reputation.feature.inbox.data.pending

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.reputation.feature.inbox.data.pending.ProductrevWaitForFeedbackList

data class ProductrevWaitForFeedbackResponse (
        @SerializedName("list")
        @Expose
        val list: ProductrevWaitForFeedbackList = ProductrevWaitForFeedbackList(),
        @SerializedName("filterBy")
        @Expose
        val filterBy: String = "",
        @SerializedName("sortBy")
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
        val hasNext: Int = 0
)