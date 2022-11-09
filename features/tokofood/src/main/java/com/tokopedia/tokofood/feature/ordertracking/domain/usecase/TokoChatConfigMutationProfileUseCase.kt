package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.network.ConversationsNetworkError
import com.gojek.courier.CourierConnection
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import com.tokochat.tokochat_config_common.repository.courier.TokoChatBabbleCourierImpl.Companion.SOURCE_APP_INIT
import javax.inject.Inject

class TokoChatConfigMutationProfileUseCase @Inject constructor(
    private val courierConnection: CourierConnection,
    private val repository: TokoChatRepository
) {

    fun initializeConversationProfile() {
        repository.getConversationRepository().initializeConversationsProfile()
        initConnection()
    }

    private fun initConnection() {
        courierConnection.init(
            source = SOURCE_APP_INIT,
            chatProfileId = getUserId()
        )
    }

    fun getUserId(): String {
        return repository.getConversationRepository().getUserId() ?: ""
    }

    fun initGroupBooking(
        orderId: String,
        serviceType: Int = TOKOFOOD_SERVICE_TYPE,
        conversationsGroupBookingListener: ConversationsGroupBookingListener
    ) {
        repository.getConversationRepository().initGroupBookingChat(
            orderId, serviceType, conversationsGroupBookingListener)
    }

    companion object {
        const val TOKOFOOD_SERVICE_TYPE = 5
    }
}
