package com.tokopedia.tokochat.domain.response.message_data

import com.google.gson.annotations.SerializedName

data class TokoChatMessageData(
    @SerializedName("message")
    val message: TokoChatMessageLanguage? = TokoChatMessageLanguage(),

    @SerializedName("level")
    val level: String? = ""
)
