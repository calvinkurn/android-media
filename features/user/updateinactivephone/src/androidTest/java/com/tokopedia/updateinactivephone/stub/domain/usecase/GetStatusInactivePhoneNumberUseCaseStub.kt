package com.tokopedia.updateinactivephone.stub.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.updateinactivephone.domain.data.InactivePhoneUserDataModel
import com.tokopedia.updateinactivephone.domain.data.StatusInactivePhoneNumberDataModel
import com.tokopedia.updateinactivephone.domain.usecase.GetStatusInactivePhoneNumberUseCase
import javax.inject.Inject

class GetStatusInactivePhoneNumberUseCaseStub @Inject constructor(
    @ApplicationContext repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : GetStatusInactivePhoneNumberUseCase(repository, dispatcher) {

    var response = StatusInactivePhoneNumberDataModel()

    override suspend fun execute(params: InactivePhoneUserDataModel): StatusInactivePhoneNumberDataModel {
        return response
    }
}