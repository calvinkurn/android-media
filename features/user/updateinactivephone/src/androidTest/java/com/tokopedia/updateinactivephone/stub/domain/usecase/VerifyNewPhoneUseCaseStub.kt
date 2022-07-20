package com.tokopedia.updateinactivephone.stub.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.domain.data.VerifyNewPhoneDataModel
import com.tokopedia.updateinactivephone.domain.usecase.VerifyNewPhoneUseCase
import javax.inject.Inject

class VerifyNewPhoneUseCaseStub @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): VerifyNewPhoneUseCase(repository, dispatcher) {

    var response = VerifyNewPhoneDataModel()

    override suspend fun execute(params: InactivePhoneUserDataModel): VerifyNewPhoneDataModel {
        return response
    }
}