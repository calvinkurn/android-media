package com.tokopedia.chatbot.data.newsession


import com.google.gson.annotations.SerializedName

data class TopBotNewSessionResponse(
    @SerializedName("topbotGetNewSession")
    val topBotGetNewSession: TopBotGetNewSession
) {
    data class TopBotGetNewSession(
        @SerializedName("isNewSession")
        val isNewSession: Boolean
    )
}