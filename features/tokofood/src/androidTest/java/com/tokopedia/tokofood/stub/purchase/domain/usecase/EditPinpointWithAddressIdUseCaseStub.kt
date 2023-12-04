package com.tokopedia.tokofood.stub.purchase.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticCommon.domain.param.KeroEditAddressParam
import com.tokopedia.logisticCommon.domain.usecase.EditPinpointWithAddressIdUseCase
import com.tokopedia.logisticCommon.domain.usecase.KeroGetAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointUseCase
import javax.inject.Inject

class EditPinpointWithAddressIdUseCaseStub @Inject constructor(
    keroGetAddressUseCase: KeroGetAddressUseCase,
    updatePinpointUseCase: UpdatePinpointUseCase,
    coroutineDispatchers: CoroutineDispatchers
) : EditPinpointWithAddressIdUseCase(
    keroGetAddressUseCase,
    updatePinpointUseCase,
    coroutineDispatchers
) {
    // todo
    override suspend fun execute(params: KeroEditAddressParam): KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse {
        return super.execute(params)
    }
}
