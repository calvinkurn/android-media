package com.tokopedia.tokochat.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.tokochat.data.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatChannelUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {

    fun initGroupBookingChat(
        orderId: String,
        serviceType: Int,
        groupBookingListener: ConversationsGroupBookingListener,
        orderChatType: OrderChatType
    ) {
        repository.getConversationRepository().initGroupBookingChat(
            orderId, serviceType, groupBookingListener, orderChatType
        )
    }

    fun isChatConnected(): Boolean {
        return repository.getConversationRepository().isChatConnected()
    }

    fun getRemoteGroupBookingChannel(
        channelId: String,
        onSuccess: (channel: GroupBookingChannelDetails) -> Unit,
        onError: (error: ConversationsNetworkError) -> Unit
    ) {
        repository.getConversationRepository().getRemoteGroupBookingChannelDetails(
            channelId = channelId,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun getMemberLeftLiveData(): MutableLiveData<String> {
        return repository.getConversationRepository().getMemberLeftLiveDataCallback()
    }
}
