package com.tokopedia.updateinactivephone.stub.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.domain.data.SubmitExpeditedInactivePhoneDataModel
import com.tokopedia.updateinactivephone.domain.usecase.SubmitExpeditedInactivePhoneUseCase
import javax.inject.Inject

class SubmitExpeditedInactivePhoneUseCaseStub @Inject constructor(
    @ApplicationContext repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : SubmitExpeditedInactivePhoneUseCase(repository, dispatcher) {

    var response = SubmitExpeditedInactivePhoneDataModel()

    override suspend fun execute(params: Map<String, Any>): SubmitExpeditedInactivePhoneDataModel {
        return response
    }
}