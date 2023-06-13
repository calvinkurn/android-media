package com.tokopedia.tokochat.domain.usecase

import androidx.lifecycle.LiveData
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.database.chats.ConversationsMessage
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatGetChatHistoryUseCase @Inject constructor(
    @TokoChatQualifier private val repository: TokoChatRepository
) {
    operator fun invoke(channelUrl: String): LiveData<List<ConversationsMessage>> {
        return repository.getConversationRepository().getChatHistory(channelUrl)
    }

    fun loadPreviousMessage() {
        repository.getConversationRepository().loadPreviousMessages()
    }

    fun getTotalUnreadCount(types: List<ChannelType>) : LiveData<Int> {
        return repository.getConversationRepository().getUnreadCount(types)
    }
}
