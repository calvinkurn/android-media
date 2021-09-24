package com.tokopedia.updateinactivephone.stub.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.domain.usecase.PhoneValidationUseCase

class PhoneValidationUseCaseStub constructor(
    repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : PhoneValidationUseCase(repository, dispatcher) {

    var response = PhoneValidationDataModel()

    override suspend fun execute(params: Map<String, Any>): PhoneValidationDataModel {
        return response
    }
}