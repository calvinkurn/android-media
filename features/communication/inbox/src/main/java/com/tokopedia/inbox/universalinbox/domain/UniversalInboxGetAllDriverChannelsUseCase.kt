package com.tokopedia.inbox.universalinbox.domain

import androidx.lifecycle.LiveData
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import javax.inject.Inject

class UniversalInboxGetAllDriverChannelsUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    fun getAllChannels(): LiveData<List<ConversationsChannel>> {
        return repository.getConversationRepository().getAllChannels(
            listOf(ChannelType.GroupBooking)
        )
    }
}
