package com.tokopedia.chatbot.chatbot2.data.sessionchange

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
