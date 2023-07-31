package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.RegisterProgressiveKYC
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.RegisterProgressiveResponse
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
                  cooldownTimeInSeconds
                  maximumAttemptsAllowed
                  message
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

        return if (response.data.message == KEY_EXHAUSTED) {
            RegisterProgressiveResult.Exhausted(
                cooldownTimeInSeconds = response.data.cooldownTimeInSeconds,
                maximumAttemptsAllowed = response.data.maximumAttemptsAllowed
            )
        } else if (response.errorMessages.isNotEmpty()) {
            RegisterProgressiveResult.Failed(throwable = Throwable(message = response.errorMessages.joinToString()))
        } else if (response.data.challengeID.isNotEmpty()) {
            RegisterProgressiveResult.RiskyUser(challengeId = response.data.challengeID)
        } else {
            RegisterProgressiveResult.NotRiskyUser(status = response.data.status, rejectionReason = response.data.rejectionReasonMessage)
        }
    }

    companion object {
        private const val KEY_EXHAUSTED = "KYC_CHALLENGE_CREATION_QUOTA_EXCEEDED"
    }
}

sealed class RegisterProgressiveResult {
    object Loading : RegisterProgressiveResult()
    data class RiskyUser(val challengeId: String) : RegisterProgressiveResult()
    data class NotRiskyUser(val status: Int, val rejectionReason: String = "") : RegisterProgressiveResult()
    class Exhausted(
        val cooldownTimeInSeconds: String,
        val maximumAttemptsAllowed: String
    ): RegisterProgressiveResult()
    data class Failed(val throwable: Throwable) : RegisterProgressiveResult()
}

data class RegisterProgressiveParam (
    @SerializedName("param")
    val param: RegisterProgressiveData = RegisterProgressiveData()
): GqlParam

data class RegisterProgressiveData (
    @SerializedName("projectID")
    val projectID: Int = 0,

    @SerializedName("challengeID")
    val challengeID: String = ""
)
