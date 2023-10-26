package com.tokopedia.tokofood.stub.postpurchase.domain.usecase

import com.tokopedia.tokochat.config.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatConfigGroupBookingUseCaseStub @Inject constructor(
    repository: TokoChatRepository
) : TokoChatConfigGroupBookingUseCase(repository)
