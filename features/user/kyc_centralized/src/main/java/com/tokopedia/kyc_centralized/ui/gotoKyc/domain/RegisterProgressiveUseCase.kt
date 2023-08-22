package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.RegisterProgressiveKYC
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.RegisterProgressiveResponse
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class RegisterProgressiveUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    @ApplicationContext private val context: Context,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<RegisterProgressiveParam, RegisterProgressiveResult>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            mutation kycRegisterProgressive(${'$'}param: kycRegisterProgressiveRequest!) {
              kycRegisterProgressive(param: ${'$'}param) {
                errorMessages
                errorCode
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
            RegisterProgressiveResult.Failed(
                throwable = mappingErrorMessage(
                    message = response.errorMessages.joinToString(),
                    errorCode = response.errorCode
                )
            )
        } else if (response.data.challengeID.isNotEmpty()) {
            RegisterProgressiveResult.RiskyUser(challengeId = response.data.challengeID)
        } else {
            RegisterProgressiveResult.NotRiskyUser(status = response.data.status, rejectionReason = response.data.rejectionReasonMessage)
        }
    }

    private fun mappingErrorMessage(message: String, errorCode: String): MessageErrorException {
        var keyKnowError = ""
        val messageError: String

        when {
            errorCode == ERROR_CODE_DATA_ALREADY_EXIST -> {
                messageError = context.getString(R.string.goto_kyc_error_data_already_exist)
                keyKnowError = KYCConstant.KEY_KNOW_ERROR_CODE
            }
            LIST_COMMON_ERROR_CODE.contains(errorCode) -> {
                messageError = context.getString(R.string.goto_kyc_error_know_code)
                keyKnowError = KYCConstant.KEY_KNOW_ERROR_CODE
            }
            else -> {
                messageError = message
            }
        }

        val generateErrorCode = context.getString(R.string.error_code, errorCode)

        return MessageErrorException("$messageError $generateErrorCode", keyKnowError)
    }

    companion object {
        private const val KEY_EXHAUSTED = "KYC_CHALLENGE_CREATION_QUOTA_EXCEEDED"
        private const val ERROR_CODE_DATA_ALREADY_EXIST = "30006"
        private val LIST_COMMON_ERROR_CODE = listOf("1508", "1536", "1533", "1513", "1541", "1539", "30009", "30004")
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
    @SuppressLint("Invalid Data Type")
    @SerializedName("projectID")
    val projectID: Int = 0,

    @SerializedName("challengeID")
    val challengeID: String = ""
)
