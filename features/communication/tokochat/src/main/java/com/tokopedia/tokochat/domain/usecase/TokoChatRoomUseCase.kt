package com.tokopedia.tokochat.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.channel.GetChannelRequest
import com.gojek.conversations.extensions.ConversationsExtensionProvider
import com.gojek.conversations.groupbooking.GroupBookingChannelDetails
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.tokochat.config.domain.TokoChatChannelUseCase
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokochat.config.util.TokoChatResult
import com.tokopedia.tokochat.config.util.TokoChatServiceType
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

open class TokoChatRoomUseCase @Inject constructor(
    private val repository: TokoChatRepository,
    private val tokoChatChannelUseCase: TokoChatChannelUseCase
) {

    private var lastTimeStamp: Long = 0

    fun initGroupBookingChat(
        orderId: String,
        serviceType: TokoChatServiceType
    ) {
        tokoChatChannelUseCase.initGroupBookingChat(
            orderId = orderId,
            serviceType = serviceType
        )
    }

    fun getGroupBookingFlow(): SharedFlow<TokoChatResult<String>> {
        return tokoChatChannelUseCase.groupBookingResultFlow
    }

    open fun isChatConnected(): Boolean {
        return repository.getConversationRepository()?.isChatConnected() ?: false
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

    fun getMemberLeftLiveData(): MutableLiveData<String>? {
        return repository.getConversationRepository()?.getMemberLeftLiveDataCallback()
    }

    fun resetMemberLeftLiveData() {
        repository.getConversationRepository()?.resetMemberLeftLiveDataCallback()
    }

    fun getLiveChannel(channelId: String): LiveData<ConversationsChannel?>? {
        return repository.getConversationRepository()?.getLiveChannel(channelId)
    }

    fun registerExtensionProvider(extensionProvider: ConversationsExtensionProvider) {
        repository.getConversationRepository()?.registerExtensionProvider(extensionProvider)
    }

    fun unRegisterExtensionProvider(extensionProvider: ConversationsExtensionProvider) {
        repository.getConversationRepository()?.unRegisterExtensionProvider(extensionProvider)
    }

    fun getAllChannel(
        channelTypes: List<ChannelType>,
        batchSize: Int,
        onSuccess: (List<ConversationsChannel>) -> Unit,
        onError: (ConversationsNetworkError?) -> Unit
    ) {
        if (lastTimeStamp < 0L) return
        repository.getConversationRepository()?.getAllChannels(
            getChannelRequest = GetChannelRequest(
                types = channelTypes,
                timestamp = getLastTimeStamp(),
                batchSize = batchSize
            ),
            onSuccess = onSuccess,
            onError = onError
        )
    }

    private fun getLastTimeStamp(): Long {
        return if (lastTimeStamp == 0L) {
            System.currentTimeMillis()
        } else {
            lastTimeStamp
        }
    }

    fun getAllCachedChannels(
        channelTypes: List<ChannelType>
    ): LiveData<List<ConversationsChannel>>? {
        return repository.getConversationRepository()?.getAllCachedChannels(channelTypes)
    }

    fun setLastTimeStamp(timeStamp: Long) {
        this.lastTimeStamp = timeStamp
    }
}
