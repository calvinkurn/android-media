package com.tokopedia.tokochat.domain.usecase

import com.gojek.courier.CourierConnection
import com.tokopedia.tokochat.data.repository.TokoChatRepository
import javax.inject.Inject

class MutationProfileUseCase @Inject constructor(
    private val repository: TokoChatRepository,
    private val courierConnection: CourierConnection
) {
    fun initializeConversationProfile() {
        repository.getConversationRepository().initializeConversationsProfile()
        initConnection()
    }

    private fun initConnection() {
        courierConnection.init(
            source = "",
            chatProfileId = getUserId()
        )
    }

    fun getUserId(): String {
        return repository.getConversationRepository().getUserId()?: ""
    }
}
