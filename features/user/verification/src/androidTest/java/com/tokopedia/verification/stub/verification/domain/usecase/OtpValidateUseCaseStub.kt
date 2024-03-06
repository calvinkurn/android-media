package com.tokopedia.verification.stub.verification.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.verification.verification.domain.data.OtpValidatePojo
import com.tokopedia.verification.verification.domain.usecase.OtpValidateUseCase

class OtpValidateUseCaseStub(
        graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : OtpValidateUseCase(graphqlRepository, dispatcher) {

    var response = OtpValidatePojo()

    override suspend fun getData(parameter: Map<String, Any>): OtpValidatePojo {
        return response
    }
}
