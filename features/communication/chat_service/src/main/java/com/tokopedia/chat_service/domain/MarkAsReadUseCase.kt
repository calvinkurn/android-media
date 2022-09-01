package com.tokopedia.chat_service.domain

import com.tokopedia.chat_service.data.repository.ChatServiceRepository
import javax.inject.Inject

class MarkAsReadUseCase @Inject constructor(
    private val repository: ChatServiceRepository
) {
    operator fun invoke(channelUrl: String) {
        repository.getConversationRepository().markAllMessagesAsRead(channelUrl)
    }
}