package com.tokopedia.tokochat.stub.domain.usecase

import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.tokochat.domain.usecase.TokoChatChannelUseCase
import com.tokopedia.tokochat.stub.repository.TokoChatRepositoryStub
import javax.inject.Inject

class TokoChatChannelUseCaseStub @Inject constructor(
    @ActivityScope tokoChatRepositoryStub: TokoChatRepositoryStub
): TokoChatChannelUseCase(tokoChatRepositoryStub) {
    var isConnected: Boolean = true

    override fun isChatConnected(): Boolean {
        return isConnected
    }
}
