package com.tokopedia.tokochat.domain

import com.tokopedia.tokochat.data.repository.TokoChatRepository
import javax.inject.Inject

class MarkAsReadUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    operator fun invoke(channelUrl: String) {
        repository.getConversationRepository().markAllMessagesAsRead(channelUrl)
    }
}
