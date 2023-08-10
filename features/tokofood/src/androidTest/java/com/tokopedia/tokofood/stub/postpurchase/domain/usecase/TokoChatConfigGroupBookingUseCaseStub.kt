package com.tokopedia.tokofood.stub.postpurchase.domain.usecase

import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.TokoChatConfigGroupBookingUseCase
import javax.inject.Inject

class TokoChatConfigGroupBookingUseCaseStub @Inject constructor(
    repository: TokoChatRepository
) : TokoChatConfigGroupBookingUseCase(repository)
