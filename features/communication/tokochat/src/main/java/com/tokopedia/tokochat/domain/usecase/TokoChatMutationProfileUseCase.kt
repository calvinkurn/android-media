package com.tokopedia.tokochat.domain.usecase

import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import com.tokochat.tokochat_config_common.repository.courier.TokoChatBabbleCourierImpl.Companion.SOURCE_APP_INIT
import com.tokochat.tokochat_config_common.util.TokoChatConnection
import javax.inject.Inject

class TokoChatMutationProfileUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    fun initializeConversationProfile() {
        repository.getConversationRepository().initializeConversationsProfile()
        initConnection()
    }

    private fun initConnection() {
        TokoChatConnection.courierConnection?.init(
            source = SOURCE_APP_INIT,
            chatProfileId = getUserId()
        )
    }

    fun getUserId(): String {
        return repository.getConversationRepository().getUserId()?: ""
    }
}
