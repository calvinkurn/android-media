package com.tokopedia.review.feature.inbox.pending.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevWaitForFeedbackSeenResponse(
        @SerializedName("productrevWaitForFeedbackSeen")
        @Expose
        val productrevWaitForFeedbackSeen: String = ""
)