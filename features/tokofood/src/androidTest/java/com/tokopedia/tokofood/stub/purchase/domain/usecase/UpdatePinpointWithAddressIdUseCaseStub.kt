package com.tokopedia.tokofood.stub.purchase.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticCommon.domain.param.KeroEditAddressParam
import com.tokopedia.logisticCommon.domain.usecase.GetAddressDetailByIdUseCase
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointUseCase
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointWithAddressIdUseCase
import javax.inject.Inject

class UpdatePinpointWithAddressIdUseCaseStub @Inject constructor(
    getAddressDetailByIdUseCase: GetAddressDetailByIdUseCase,
    updatePinpointUseCase: UpdatePinpointUseCase,
    coroutineDispatchers: CoroutineDispatchers
) : UpdatePinpointWithAddressIdUseCase(
    getAddressDetailByIdUseCase,
    updatePinpointUseCase,
    coroutineDispatchers
) {
    // todo
    override suspend fun execute(params: KeroEditAddressParam): KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse {
        return super.execute(params)
    }
}
