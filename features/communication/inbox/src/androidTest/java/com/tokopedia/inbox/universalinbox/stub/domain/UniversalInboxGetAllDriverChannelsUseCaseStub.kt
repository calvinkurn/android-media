package com.tokopedia.inbox.universalinbox.stub.domain

import androidx.lifecycle.LiveData
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.inbox.universalinbox.domain.UniversalInboxGetAllDriverChannelsUseCase
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import javax.inject.Inject

class UniversalInboxGetAllDriverChannelsUseCaseStub @Inject constructor(
    repository: TokoChatRepository
) : UniversalInboxGetAllDriverChannelsUseCase(repository) {

    var isError = false

    override fun getAllChannels(): LiveData<List<ConversationsChannel>>? {
        if (isError) {
            throw Throwable("Oops!")
        } else {
            return super.getAllChannels()
        }
    }
}
