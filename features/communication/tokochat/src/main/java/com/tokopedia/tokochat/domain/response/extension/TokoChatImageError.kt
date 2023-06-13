package com.tokopedia.tokochat.domain.response.extension

import com.google.gson.annotations.SerializedName

data class TokoChatImageError (
    @SerializedName("code")
    var code: String? = "",

    @SerializedName("message")
    var message: String? = "",

    @SerializedName("message_title")
    var messageTitle: String? = ""
)
