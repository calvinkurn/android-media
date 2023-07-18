package com.tokopedia.tokofood.feature.ordertracking.domain.usecase

import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import javax.inject.Inject

open class TokoChatConfigGroupBookingUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {

    fun initGroupBooking(
        orderId: String,
        serviceType: Int = TOKOFOOD_SERVICE_TYPE,
        conversationsGroupBookingListener: ConversationsGroupBookingListener
    ) {
        repository.getConversationRepository()?.initGroupBookingChat(
            orderId,
            serviceType,
            conversationsGroupBookingListener
        )
    }

    companion object {
        const val TOKOFOOD_SERVICE_TYPE = 5
    }
}
