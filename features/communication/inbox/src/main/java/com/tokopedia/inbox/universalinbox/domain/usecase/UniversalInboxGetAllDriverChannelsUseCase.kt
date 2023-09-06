package com.tokopedia.inbox.universalinbox.domain.usecase

import androidx.lifecycle.LiveData
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import javax.inject.Inject

open class UniversalInboxGetAllDriverChannelsUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    open fun getAllChannels(): LiveData<List<ConversationsChannel>>? {
        return repository.getConversationRepository()?.getAllChannels(
            listOf(ChannelType.GroupBooking)
        )
    }
}
