package com.tokopedia.developer_options.presentation.feedbackpage.domain.response

import com.google.gson.annotations.SerializedName

data class FeedbackFormResponse(
        @SerializedName("data")
        var data: Data = Data(),
        @SerializedName("error")
        var error: String = ""
)

data class Data(
        @SerializedName("feedbackID")
        var feedbackID: Int=0,
        @SerializedName("message")
        var message: String = ""
)