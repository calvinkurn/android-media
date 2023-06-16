package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

import com.tokopedia.chatbot.chatbot2.data.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse

sealed class ChatbotOpenCsatState {
    data class ShowCsat(val response: WebSocketCsatResponse) : ChatbotOpenCsatState()
}
