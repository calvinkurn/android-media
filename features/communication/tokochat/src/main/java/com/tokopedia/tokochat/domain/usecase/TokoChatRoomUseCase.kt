package com.tokopedia.tokochat.domain.usecase

import androidx.lifecycle.LiveData
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.extensions.ConversationsExtensionProvider
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import javax.inject.Inject

open class TokoChatRoomUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {

    open fun isChatConnected(): Boolean {
        // Do not intervene with chatroom when get null
        return repository.getConversationRepository()?.isChatConnected() ?: true
    }

    fun getRemoteGroupBookingChannel(
        channelId: String,
        onSuccess: (channel: GroupBookingChannelDetails) -> Unit,
        onError: (error: ConversationsNetworkError) -> Unit
    ) {
        repository.getConversationRepository()?.getRemoteGroupBookingChannelDetails(
            channelId = channelId,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    open fun getLiveChannel(channelId: String): LiveData<ConversationsChannel?>? {
        return repository.getConversationRepository()?.getLiveChannel(channelId)
    }

    fun registerExtensionProvider(extensionProvider: ConversationsExtensionProvider) {
        repository.getConversationRepository()?.registerExtensionProvider(extensionProvider)
    }

    fun unRegisterExtensionProvider(extensionProvider: ConversationsExtensionProvider) {
        repository.getConversationRepository()?.unRegisterExtensionProvider(extensionProvider)
    }
}
