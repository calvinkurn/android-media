package com.tokopedia.reputation.feature.inbox.data.history

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevFeedbackHistoryUser(
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("isAnonym")
        @Expose
        val isAnonym: Boolean = false
)