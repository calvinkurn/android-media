package com.tokopedia.tokochat.domain.usecase

import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatMarkAsReadUseCase @Inject constructor(
    @TokoChatQualifier private val repository: TokoChatRepository
) {
    operator fun invoke(channelUrl: String) {
        repository.getConversationRepository()?.markAllMessagesAsRead(channelUrl)
    }
}
