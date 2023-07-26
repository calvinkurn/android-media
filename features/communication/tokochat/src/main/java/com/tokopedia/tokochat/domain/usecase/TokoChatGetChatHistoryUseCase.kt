package com.tokopedia.tokochat.domain.usecase

import androidx.lifecycle.LiveData
import com.gojek.conversations.database.chats.ConversationsMessage
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatGetChatHistoryUseCase @Inject constructor(
    @TokoChatQualifier private val repository: TokoChatRepository
) {
    operator fun invoke(channelUrl: String): LiveData<List<ConversationsMessage>>? {
        return repository.getConversationRepository()?.getChatHistory(channelUrl)
    }

    fun loadPreviousMessage() {
        repository.getConversationRepository()?.loadPreviousMessages()
    }
}
