package com.tokopedia.chat_service.domain

import androidx.lifecycle.LiveData
import com.gojek.conversations.database.chats.ConversationsMessage
import com.tokopedia.chat_service.data.repository.TokoChatRepository
import javax.inject.Inject

class GetChatHistoryUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    operator fun invoke(channelUrl: String): LiveData<List<ConversationsMessage>> {
        return repository.getConversationRepository().getChatHistory(channelUrl)
    }

    fun loadPreviousMessage() {
        repository.getConversationRepository().loadPreviousMessages()
    }

    fun getTotalUnreadCount() : LiveData<Int> {
        return repository.getConversationRepository().getTotalUnreadCountLiveDataCallback()
    }
}
