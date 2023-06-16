package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

sealed class ChatbotSendChatRatingState {
    data class HandleSuccessChatbotSendChatRating(val data: com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo) : ChatbotSendChatRatingState()
    data class HandleErrorSendChatRating(val throwable: Throwable) : ChatbotSendChatRatingState()
}
