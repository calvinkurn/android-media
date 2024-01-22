package com.tokopedia.tokofood.stub.purchase.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointUseCase
import javax.inject.Inject

class UpdatePinpointUseCaseStub @Inject constructor(
    repository: GraphqlRepository,
    coroutineDispatchers: CoroutineDispatchers
) : UpdatePinpointUseCase(
    repository,
    coroutineDispatchers
)
