package com.tokopedia.tokofood.stub.postpurchase.domain.usecase

import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetUnreadChatCountUseCase
import javax.inject.Inject

class GetUnreadChatCountUseCaseStub @Inject constructor(
    repository: TokoChatRepository
) : GetUnreadChatCountUseCase(repository)
