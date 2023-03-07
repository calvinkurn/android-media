package com.tokopedia.chatbot.data.sessionchange

import com.google.gson.annotations.SerializedName

data class SessionChangeAttributes(
    @SerializedName("session_change")
    val sessionChange: SessionChange
) {
    data class SessionChange(
        @SerializedName("mode")
        val mode: String,
        @SerializedName("session_id")
        val sessionId: String
    )
}