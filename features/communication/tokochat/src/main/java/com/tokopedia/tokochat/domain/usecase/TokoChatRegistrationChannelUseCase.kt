package com.tokopedia.tokochat.domain.usecase

import com.tokopedia.tokochat.data.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatRegistrationChannelUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    fun registerActiveChannel(channelUrl: String) {
        repository.getConversationRepository().softRegisterChannel(channelUrl)
    }

    fun deRegisterActiveChannel(channelUrl: String) {
        repository.getConversationRepository().softDeregisterChannel(channelUrl)
    }

    fun isChatConnected(): Boolean {
        return repository.getConversationRepository().isChatConnected()
    }
}
