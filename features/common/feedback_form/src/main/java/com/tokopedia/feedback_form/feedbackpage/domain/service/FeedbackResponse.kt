package com.tokopedia.feedback_form.feedbackpage.domain.service

import com.google.gson.annotations.SerializedName

class FeedbackResponse {
    @SerializedName("id")
    var id: String = ""
    @SerializedName("key")
    var key: String = ""
    @SerializedName("self")
    var self: String = ""

}