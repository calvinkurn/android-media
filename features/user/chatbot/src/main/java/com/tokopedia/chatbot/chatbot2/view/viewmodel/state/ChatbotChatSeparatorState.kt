package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

import com.tokopedia.chatbot.chatbot2.view.uimodel.quickreply.QuickReplyUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.seprator.ChatSepratorUiModel

sealed class ChatbotChatSeparatorState {
    data class ChatSeparator(
        val chatSepratorUiModel: ChatSepratorUiModel,
        val quickReplyList: List<QuickReplyUiModel>
    ) : ChatbotChatSeparatorState()
}
