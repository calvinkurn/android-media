package com.tokopedia.tokochat.domain.usecase

import androidx.lifecycle.LiveData
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.channel.GetChannelRequest
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatGetAllChannelsUseCase @Inject constructor(
    @TokoChatQualifier private val repository: TokoChatRepository
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

    fun getUserId(): String {
        return repository.getConversationRepository().getUserId()?: ""
    }
}
