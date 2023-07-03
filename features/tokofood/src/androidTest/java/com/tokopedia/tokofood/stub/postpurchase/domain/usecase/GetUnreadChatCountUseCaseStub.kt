package com.tokopedia.tokofood.stub.postpurchase.domain.usecase

import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetUnreadChatCountUseCase
import javax.inject.Inject

class GetUnreadChatCountUseCaseStub @Inject constructor(
    repository: TokoChatRepository
) : GetUnreadChatCountUseCase(repository)
