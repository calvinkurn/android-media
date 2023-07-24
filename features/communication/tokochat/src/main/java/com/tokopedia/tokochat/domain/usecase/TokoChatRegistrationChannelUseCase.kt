package com.tokopedia.tokochat.domain.usecase

import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatRegistrationChannelUseCase @Inject constructor(
    @TokoChatQualifier private val repository: TokoChatRepository
) {
    fun registerActiveChannel(channelUrl: String) {
        repository.getConversationRepository()?.softRegisterChannel(channelUrl)
    }

    fun deRegisterActiveChannel(channelUrl: String) {
        repository.getConversationRepository()?.softDeregisterChannel(channelUrl)
    }

    fun getUserId(): String {
        return repository.getConversationRepository()?.getUserId() ?: ""
    }
}
