package com.tokopedia.tokochat.domain.usecase

import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatRegistrationChannelUseCase @Inject constructor(
    @TokoChatQualifier private val repository: TokoChatRepository
) {
    fun registerActiveChannel(channelUrl: String) {
        repository.getConversationRepository().softRegisterChannel(channelUrl)
    }

    fun deRegisterActiveChannel(channelUrl: String) {
        repository.getConversationRepository().softDeregisterChannel(channelUrl)
    }

    fun getUserId(): String {
        return repository.getConversationRepository().getUserId() ?: ""
    }
}
