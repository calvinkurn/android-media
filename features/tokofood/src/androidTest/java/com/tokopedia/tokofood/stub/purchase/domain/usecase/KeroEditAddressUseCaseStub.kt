package com.tokopedia.tokofood.stub.purchase.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticCommon.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.logisticCommon.domain.usecase.KeroGetAddressUseCase
import javax.inject.Inject

class KeroEditAddressUseCaseStub @Inject constructor(
    repository: GraphqlRepository,
    keroGetAddressUseCase: KeroGetAddressUseCase
) : KeroEditAddressUseCase(repository, keroGetAddressUseCase) {
    // todo
    override suspend fun executeOnBackground(): KeroEditAddressResponse.Data {
        return super.executeOnBackground()
    }
}
