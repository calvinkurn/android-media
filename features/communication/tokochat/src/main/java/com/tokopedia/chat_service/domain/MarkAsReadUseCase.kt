package com.tokopedia.chat_service.domain

import com.tokopedia.chat_service.data.repository.TokoChatRepository
import javax.inject.Inject

class MarkAsReadUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    operator fun invoke(channelUrl: String) {
        repository.getConversationRepository().markAllMessagesAsRead(channelUrl)
    }
}
