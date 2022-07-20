package com.tokopedia.updateinactivephone.stub.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneSubmitDataModel
import com.tokopedia.updateinactivephone.domain.usecase.SubmitDataModel
import com.tokopedia.updateinactivephone.domain.usecase.SubmitDataUseCase
import javax.inject.Inject

class SubmitDataUseCaseStub @Inject constructor(
    @ApplicationContext repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : SubmitDataUseCase(repository, dispatcher) {

    var response = InactivePhoneSubmitDataModel()

    override suspend fun execute(params: SubmitDataModel): InactivePhoneSubmitDataModel {
        return response
    }
}