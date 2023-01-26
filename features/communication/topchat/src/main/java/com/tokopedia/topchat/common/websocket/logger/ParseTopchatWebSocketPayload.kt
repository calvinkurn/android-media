package com.tokopedia.topchat.common.websocket.logger

import com.google.gson.annotations.SerializedName

data class ParseTopchatWebSocketPayload(
    @SerializedName("code")
    val code: Int,

    @SerializedName("message_id", alternate = ["msg_id"])
    val messageId: String
)
