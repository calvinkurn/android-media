package com.tokopedia.reputation.feature.inbox.data.pending

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevWaitForFeedbackStatus(
        @SerializedName("seen")
        @Expose
        val seen: Boolean = false
)