package com.tokopedia.tokofood.stub.purchase.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticCommon.domain.usecase.EditPinpointWithAddressIdUseCase
import com.tokopedia.logisticCommon.domain.usecase.KeroGetAddressUseCase
import javax.inject.Inject

class EditPinpointWithAddressIdUseCaseStub @Inject constructor(
    repository: GraphqlRepository,
    keroGetAddressUseCase: KeroGetAddressUseCase
) : EditPinpointWithAddressIdUseCase(repository, keroGetAddressUseCase) {
    // todo
    override suspend fun executeOnBackground(): KeroEditAddressResponse.Data {
        return super.executeOnBackground()
    }
}
