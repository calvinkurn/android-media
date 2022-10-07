package com.tokopedia.tokochat.domain.usecase

import com.gojek.conversations.babble.channel.data.CreateChannelInfo
import com.gojek.conversations.babble.network.data.OrderChatType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.groupbooking.ConversationsGroupBookingListener
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.tokochat.data.repository.TokoChatRepository
import javax.inject.Inject

class CreateChannelUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    operator fun invoke(params: CreateChannelParam) {
        repository.getConversationRepository().createChannel(
            params.createChannelInfo,
            params.onSuccess,
            params.onError
        )
    }

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

    fun getGroupBookingChannel(
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

    data class CreateChannelParam(
        val createChannelInfo: CreateChannelInfo,
        val onSuccess: (channel: ConversationsChannel) -> Unit,
        val onError: (error: ConversationsNetworkError) -> Unit
    )

}
