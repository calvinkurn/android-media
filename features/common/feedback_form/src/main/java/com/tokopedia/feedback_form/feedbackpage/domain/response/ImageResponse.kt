package com.tokopedia.feedback_form.feedbackpage.domain.response

import com.google.gson.annotations.SerializedName

class ImageResponse (
        @SerializedName("data")
        var data: DataImage = DataImage(),
        @SerializedName("error")
        var error: String = ""
)

data class DataImage(
        @SerializedName("message")
        var message: String = ""
)