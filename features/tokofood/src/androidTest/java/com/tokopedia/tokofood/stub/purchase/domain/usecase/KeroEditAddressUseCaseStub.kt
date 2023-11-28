package com.tokopedia.tokofood.stub.purchase.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.common.domain.usecase.KeroEditAddressUseCase
import com.tokopedia.tokofood.common.domain.usecase.KeroGetAddressUseCase
import javax.inject.Inject

class KeroEditAddressUseCaseStub @Inject constructor(
    repository: GraphqlRepository,
    keroGetAddressUseCase: KeroGetAddressUseCase
) : KeroEditAddressUseCase(repository, keroGetAddressUseCase) {

    override suspend fun executeOnBackground(): com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse.Data {
        return super.executeOnBackground()
    }
}
