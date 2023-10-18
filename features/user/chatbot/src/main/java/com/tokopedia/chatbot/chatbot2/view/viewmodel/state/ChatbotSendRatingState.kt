package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

sealed class ChatbotSendRatingState {
    object HandleFailureChatbotSendRating : ChatbotSendRatingState()
    data class SuccessChatbotSendRating(val data: com.tokopedia.chatbot.chatbot2.data.chatrating.SendRatingPojo) : ChatbotSendRatingState()
}
