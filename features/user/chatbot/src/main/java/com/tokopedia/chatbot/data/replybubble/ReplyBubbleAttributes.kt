package com.tokopedia.chatbot.data.replybubble

import com.google.gson.annotations.SerializedName

data class ReplyBubbleAttributes(
    @SerializedName("last_mode")
    val lastMode : String,
    @SerializedName("mode")
    val mode : String,
    @SerializedName("session_id")
    val sessionId : String
)