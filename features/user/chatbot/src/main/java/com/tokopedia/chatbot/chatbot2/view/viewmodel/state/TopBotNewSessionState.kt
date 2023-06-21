package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

import com.tokopedia.chatbot.chatbot2.data.newsession.TopBotNewSessionResponse

sealed class TopBotNewSessionState {
    object HandleFailureNewSession : TopBotNewSessionState()
    data class SuccessTopBotNewSession(val data: TopBotNewSessionResponse.TopBotGetNewSession) : TopBotNewSessionState()
}
