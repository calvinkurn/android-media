package com.tokopedia.sellerfeedback.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SubmitGlobalFeedbackResponseWrapper {
    @SerializedName("submitGlobalFeedback")
    @Expose
    val submitGlobalFeedback: SubmitGlobalFeedback = SubmitGlobalFeedback()
}