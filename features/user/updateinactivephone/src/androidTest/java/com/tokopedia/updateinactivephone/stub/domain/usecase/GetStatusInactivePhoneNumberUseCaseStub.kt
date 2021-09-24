package com.tokopedia.updateinactivephone.stub.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.domain.data.StatusInactivePhoneNumberDataModel
import com.tokopedia.updateinactivephone.domain.usecase.GetStatusInactivePhoneNumberUseCase

class GetStatusInactivePhoneNumberUseCaseStub constructor(
    repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : GetStatusInactivePhoneNumberUseCase(repository, dispatcher) {

    var response = StatusInactivePhoneNumberDataModel()

    override suspend fun execute(params: Map<String, Any>): StatusInactivePhoneNumberDataModel {
        return response
    }
}