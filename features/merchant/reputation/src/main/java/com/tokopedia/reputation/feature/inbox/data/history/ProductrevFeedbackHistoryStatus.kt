package com.tokopedia.reputation.feature.inbox.data.history

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevFeedbackHistoryStatus(
        @SerializedName("seen")
        @Expose
        val seen: Boolean = false,
        @SerializedName("editable")
        @Expose
        val editable: Boolean = false
)