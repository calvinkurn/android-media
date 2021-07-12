package com.tokopedia.otp.stub.verification.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.otp.verification.domain.data.OtpRequestPojo
import com.tokopedia.otp.verification.domain.usecase.SendOtpUseCase

class SendOtpUseCaseStub(
        graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : SendOtpUseCase(graphqlRepository, dispatcher) {

    var response = OtpRequestPojo()

    override suspend fun getData(parameter: Map<String, Any>): OtpRequestPojo {
        return response
    }
}