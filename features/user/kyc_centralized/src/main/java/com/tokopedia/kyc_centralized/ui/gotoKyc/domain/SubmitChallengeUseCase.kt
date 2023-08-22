package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

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
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.SubmitChallengeResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.SubmitKYCChallenge
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class SubmitChallengeUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    @ApplicationContext private val context: Context,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<SubmitChallengeParam, SubmitChallengeResult>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
          mutation kycSubmitGoToChallenge(
            ${'$'}param: kycSubmitGoToChallengeRequest!
          ) {
            kycSubmitGoToChallenge(
              param: ${'$'}param
            ) {
              cooldownTimeInSeconds
              attemptsRemaining
              maximumAttemptsAllowed
              message
              errorMessages
              errorCode
            }
          }
        """.trimIndent()

    override suspend fun execute(params: SubmitChallengeParam): SubmitChallengeResult {
        val response : SubmitKYCChallenge = repository
            .request<SubmitChallengeParam, SubmitChallengeResponse>(graphqlQuery(), params)
            .submitKYCChallenge

        return if (response.errorMessages.isNotEmpty()) {
            SubmitChallengeResult.Failed(
                mappingErrorMessage(response.errorMessages.joinToString(), response.errorCode)
            )
        } else {
            when (response.message) {
                KEY_WRONG_ANSWER -> {
                    SubmitChallengeResult.WrongAnswer(attemptsRemaining = response.attemptsRemaining)
                }
                KEY_EXHAUSTED -> {
                    SubmitChallengeResult.Exhausted(
                        cooldownTimeInSeconds = response.cooldownTimeInSeconds,
                        maximumAttemptsAllowed = response.maximumAttemptsAllowed
                    )
                }
                else -> {
                    SubmitChallengeResult.Success
                }
            }
        }
    }

    private fun mappingErrorMessage(message: String, errorCode: String): MessageErrorException {
        var keyKnowError = ""
        val messageError: String

        when {
            LIST_COMMON_ERROR_CODE.contains(errorCode)-> {
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
        private const val KEY_WRONG_ANSWER = "KYC_CHALLENGE_SUBMITTED_WRONG_ANSWERS"
        private const val KEY_EXHAUSTED = "KYC_CHALLENGE_ATTEMPTS_EXHAUSTED"
        private val LIST_COMMON_ERROR_CODE = listOf("1510", "1512", "1535", "1511", "1509", "1541", "1539", "30009", "30015", "30003", "900", "1546")
    }
}

sealed class SubmitChallengeResult {
    object Loading : SubmitChallengeResult()
    object Success : SubmitChallengeResult()
    class WrongAnswer(val attemptsRemaining: String): SubmitChallengeResult()
    class Exhausted(
        val cooldownTimeInSeconds: String,
        val maximumAttemptsAllowed: String
    ): SubmitChallengeResult()
    data class Failed(val throwable: Throwable): SubmitChallengeResult()
}

data class SubmitChallengeParam (
    @SerializedName("param")
    val param: SubmitChallengeData = SubmitChallengeData()
): GqlParam

data class SubmitChallengeData (
    @SerializedName("challengeID")
    val challengeID: String = "",

    @SerializedName("answers")
    val answers: List<KycSubmitGoToChallengeAnswer> = listOf()
)

data class KycSubmitGoToChallengeAnswer (
    @SerializedName("questionId")
    val questionId: String = "",

    @SerializedName("answer")
    val answer: String = ""
): GqlParam
