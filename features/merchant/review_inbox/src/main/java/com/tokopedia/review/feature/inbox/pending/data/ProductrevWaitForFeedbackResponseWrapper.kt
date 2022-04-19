package com.tokopedia.review.feature.inbox.pending.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductrevWaitForFeedbackResponseWrapper (
    @SerializedName("productrevWaitForFeedback")
    @Expose
    val productrevWaitForFeedbackWaitForFeedback: ProductrevWaitForFeedbackResponse = ProductrevWaitForFeedbackResponse()
)