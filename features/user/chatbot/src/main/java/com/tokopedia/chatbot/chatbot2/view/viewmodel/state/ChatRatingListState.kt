package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

sealed class ChatRatingListState {
    data class HandleFailureChatRatingList(val errorMessage: String) : ChatRatingListState()
}
