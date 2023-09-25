package com.tokopedia.inbox.universalinbox.stub.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.inbox.universalinbox.domain.usecase.UniversalInboxGetAllDriverChannelsUseCase
import com.tokopedia.inbox.universalinbox.util.Result
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import javax.inject.Inject

class UniversalInboxGetAllDriverChannelsUseCaseStub @Inject constructor(
    repository: TokoChatRepository,
    dispatchers: CoroutineDispatchers
) : UniversalInboxGetAllDriverChannelsUseCase(repository, dispatchers) {

    var isError = false

    override suspend fun observeDriverChannelFlow() {
        if (isError) {
            channelFlow.emit(Result.Error(Throwable("Oops!")))
        } else {
            super.observeDriverChannelFlow()
        }
    }
}
