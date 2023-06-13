package com.tokopedia.tokochat.domain.response.message_data

import com.google.gson.annotations.SerializedName

data class TokoChatMessageWrapper(
    @SerializedName("type")
    val type: String? = "",

    @SerializedName("data")
    val data: TokoChatMessageData? = TokoChatMessageData()
)
