package com.tokopedia.chat_service.domain

import com.tokopedia.chat_service.data.repository.TokoChatRepository
import javax.inject.Inject

class RegistrationActiveChannelUseCase @Inject constructor(
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
