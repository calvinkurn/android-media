package com.tokopedia.chat_service.domain

import androidx.lifecycle.LiveData
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.babble.network.data.OrderDetail
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.channel.GetChannelRequest
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.chat_service.data.repository.TokoChatRepository
import javax.inject.Inject

class GetAllChannelsUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    operator fun invoke(): LiveData<List<ConversationsChannel>> {
        return repository.getConversationRepository().getAllChannels(
                ChannelType.all()
            )
    }

    operator fun invoke(
        getChannelRequest: GetChannelRequest,
        onSuccess: (channels: List<ConversationsChannel>) -> Unit,
        onError: (error: ConversationsNetworkError?) -> Unit
    ) {
        return repository.getConversationRepository().getAllChannels(
            getChannelRequest, onSuccess, onError
        )
    }

    fun getChannelForOrderRemote(
        orderDetail: OrderDetail,
        onSuccess: (String) -> Unit,
        onError: (error: ConversationsNetworkError) -> Unit
    ) {
        repository.getConversationRepository().getChannelForOrderRemote(
            orderDetail, onSuccess, onError
        )
    }
}
