package com.tokopedia.tokofood.stub.purchase.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.tokofood.common.domain.usecase.KeroGetAddressUseCase
import javax.inject.Inject

class KeroGetAddressUseCaseStub @Inject constructor(
    graphqlRepository: GraphqlRepository
): KeroGetAddressUseCase(graphqlRepository)
