package com.tokopedia.verification.stub.verification.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.verification.otp.domain.data.OtpValidatePojo
import com.tokopedia.verification.otp.domain.usecase.OtpValidateUseCase2FA

class OtpValidateUseCase2FAStub(
        graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : OtpValidateUseCase2FA(graphqlRepository, dispatcher) {

    var response = OtpValidatePojo()

    override suspend fun getData(parameter: Map<String, Any>): OtpValidatePojo {
        return response
    }
}
