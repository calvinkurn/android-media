package com.tokopedia.review.feature.inbox.data.history

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ProductrevFeedbackHistoryResponseWrapper(
        @SerializedName("productrevFeedbackHistory")
        @Expose
        val productrevFeedbackHistoryResponse: ProductrevFeedbackHistoryResponse = ProductrevFeedbackHistoryResponse()
)