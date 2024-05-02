package com.tokopedia.verification.stub.verification.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.verification.otp.domain.data.OtpRequestPojo
import com.tokopedia.verification.otp.domain.usecase.SendOtp2FAUseCase

class SendOtp2FAUseCaseStub(
        graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : SendOtp2FAUseCase(graphqlRepository, dispatcher) {

    var response = OtpRequestPojo()

    override suspend fun getData(parameter: Map<String, Any>): OtpRequestPojo {
        return response
    }
}
