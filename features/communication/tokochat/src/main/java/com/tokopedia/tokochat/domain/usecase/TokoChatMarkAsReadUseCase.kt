package com.tokopedia.tokochat.domain.usecase

import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatMarkAsReadUseCase @Inject constructor(
    @TokoChatQualifier private val repository: TokoChatRepository
) {
    operator fun invoke(channelUrl: String) {
        repository.getConversationRepository()?.markAllMessagesAsRead(channelUrl)
    }
}
