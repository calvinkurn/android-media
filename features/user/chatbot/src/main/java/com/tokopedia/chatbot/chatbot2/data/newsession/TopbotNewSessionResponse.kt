package com.tokopedia.chatbot.chatbot2.data.newsession

import com.google.gson.annotations.SerializedName

data class TopBotNewSessionResponse(
    @SerializedName("topbotGetNewSession")
    val topBotGetNewSession: TopBotGetNewSession
) {
    data class TopBotGetNewSession(
        @SerializedName("isNewSession")
        val isNewSession: Boolean,
        @SerializedName("isTypingBlocked")
        val isTypingBlocked: Boolean,
        @SerializedName("isSlowMode")
        var isSlowMode: Boolean,
        @SerializedName("slowModeDurationInSeconds")
        var slowModeDurationInSeconds: Int
    )
}
