package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.RegisterProgressiveKYC
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.RegisterProgressiveResponse
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class RegisterProgressiveUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<RegisterProgressiveParam, RegisterProgressiveResult>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            mutation kycRegisterProgressive(${'$'}param: kycRegisterProgressiveRequest!) {
              kycRegisterProgressive(param: ${'$'}param) {
                errorMessages
                data {
                  challengeID
                  status
                  rejectionReasonCode
                  rejectionReasonMessage
                }
              }
            }
        """.trimIndent()

    override suspend fun execute(params: RegisterProgressiveParam): RegisterProgressiveResult {
        val response : RegisterProgressiveKYC =
            repository.request<RegisterProgressiveParam, RegisterProgressiveResponse>(
                graphqlQuery(),
                params
            ).registerProgressiveKYC

        return if (response.errorMessages.isNotEmpty()) {
            RegisterProgressiveResult.Failed(throwable = MessageErrorException(response.errorMessages.first()))
        } else if (response.data.challengeID.isNotEmpty()) {
            RegisterProgressiveResult.RiskyUser(challengeId = response.data.challengeID)
        } else {
            RegisterProgressiveResult.NotRiskyUser(status = response.data.status, rejectionReason = response.data.rejectionReasonMessage)
        }
    }
}
