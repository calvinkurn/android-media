package com.tokopedia.otp.stub.verification.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListData
import com.tokopedia.otp.verification.domain.pojo.OtpModeListPojo
import com.tokopedia.otp.verification.domain.usecase.GetVerificationMethodUseCase

class GetVerificationMethodUseCaseStub(
        graphqlRepository: GraphqlRepository,
        dispatcher: CoroutineDispatchers
) : GetVerificationMethodUseCase(graphqlRepository, dispatcher) {

    var response = OtpModeListPojo()

    override suspend fun getData(parameter: Map<String, Any>): OtpModeListPojo {
        return response
    }
}