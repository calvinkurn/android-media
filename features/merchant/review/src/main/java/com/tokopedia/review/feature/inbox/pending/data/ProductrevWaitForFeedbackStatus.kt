package com.tokopedia.review.feature.inbox.pending.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevWaitForFeedbackStatus(
        @SerializedName("seen")
        @Expose
        val seen: Boolean = false,
        @SerializedName("isEligible")
        @Expose
        val isEligible: Boolean = false
)