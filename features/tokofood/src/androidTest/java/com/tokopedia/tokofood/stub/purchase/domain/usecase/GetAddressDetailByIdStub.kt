package com.tokopedia.tokofood.stub.purchase.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticCommon.domain.usecase.GetAddressDetailUseCase
import com.tokopedia.logisticCommon.domain.usecase.GetAddressDetailById
import javax.inject.Inject

class GetAddressDetailByIdStub @Inject constructor(
    getAddressDetailUseCase: GetAddressDetailUseCase,
    coroutineDispatchers: CoroutineDispatchers
) : GetAddressDetailById(getAddressDetailUseCase, coroutineDispatchers)
