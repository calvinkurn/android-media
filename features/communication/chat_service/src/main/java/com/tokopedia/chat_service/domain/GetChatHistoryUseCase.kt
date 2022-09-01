package com.tokopedia.chat_service.domain

import androidx.lifecycle.LiveData
import com.gojek.conversations.database.chats.ConversationsMessage
import com.tokopedia.chat_service.data.repository.ChatServiceRepository
import javax.inject.Inject

class GetChatHistoryUseCase @Inject constructor(
    private val repository: ChatServiceRepository
) {
    operator fun invoke(channelUrl: String): LiveData<List<ConversationsMessage>> {
        return repository.getConversationRepository().getChatHistory(channelUrl)
    }

    fun loadPreviousMessage() {
        repository.getConversationRepository().loadPreviousMessages()
    }
}