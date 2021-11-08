package com.tokopedia.review.feature.inbox.history.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevFeedbackHistoryStatus(
        @SerializedName("hasResponse")
        @Expose
        val hasResponse: Boolean = false,
        @SerializedName("anonymous")
        @Expose
        val anonymous: Boolean = false
)