package com.tokopedia.tokofood.stub.purchase.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticCommon.domain.usecase.GetAddressDetailUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetAddressDetailByIdUseCase
import javax.inject.Inject

class GetAddressDetailByIdUseCaseStub @Inject constructor(
    getAddressDetailUseCase: GetAddressDetailUseCase,
    coroutineDispatchers: CoroutineDispatchers
) : GetAddressDetailByIdUseCase(getAddressDetailUseCase, coroutineDispatchers)
