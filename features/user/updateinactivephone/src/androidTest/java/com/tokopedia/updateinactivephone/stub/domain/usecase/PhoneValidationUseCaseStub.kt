package com.tokopedia.updateinactivephone.stub.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.domain.data.PhoneValidationDataModel
import com.tokopedia.updateinactivephone.domain.usecase.PhoneValidationUseCase
import javax.inject.Inject

class PhoneValidationUseCaseStub @Inject constructor(
    @ApplicationContext repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : PhoneValidationUseCase(repository, dispatcher) {

    var response = PhoneValidationDataModel()

    override suspend fun execute(params: InactivePhoneUserDataModel): PhoneValidationDataModel {
        return response
    }
}