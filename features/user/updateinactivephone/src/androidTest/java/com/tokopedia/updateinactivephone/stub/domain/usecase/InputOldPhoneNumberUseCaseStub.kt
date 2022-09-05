package com.tokopedia.updateinactivephone.stub.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.domain.data.RegisterCheckModel
import com.tokopedia.updateinactivephone.domain.usecase.InputOldPhoneNumberUseCase
import javax.inject.Inject

class InputOldPhoneNumberUseCaseStub @Inject constructor(
    @ApplicationContext repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : InputOldPhoneNumberUseCase(repository, dispatcher) {

    var response = RegisterCheckModel()

    override suspend fun execute(params: String): RegisterCheckModel {
        return response
    }
}