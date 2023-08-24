package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.R
import com.tokopedia.kyc_centralized.common.KYCConstant
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.GetChallengeResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.GetOneKYCChallenge
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class GetChallengeUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    @ApplicationContext private val context: Context,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, GetChallengeResult>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query kycGetGoToChallenge(${'$'}challengeID: String) {
              kycGetGoToChallenge(challengeID: ${'$'}challengeID) {
                isSuccess
                errorMessages
                errorCode
                data {
                  id
                  questionType
                  displayText
                  hint
                  type
                }
              }
            }
        """.trimIndent()

    override suspend fun execute(params: String): GetChallengeResult {
        val parameter = mapOf(KEY_CHALLENGE_ID to params)
        val response : GetOneKYCChallenge = repository
            .request<Map<String, String>, GetChallengeResponse>(graphqlQuery(), parameter)
            .getOneKYCChallenge

        val dobChallenge = response.data.find { it.questionType == QUESTION_TYPE_DOB }

        return if (response.isSuccess && dobChallenge?.id?.isNotEmpty() == true) {
            GetChallengeResult.Success(questionId = dobChallenge.id)
        } else {
            GetChallengeResult.Failed(
                mappingErrorMessage(response.errorMessages.joinToString(), response.errorCode)
            )
        }
    }

    private fun mappingErrorMessage(message: String, errorCode: String): MessageErrorException {
        var keyKnowError = ""
        val messageError: String

        when {
            LIST_COMMON_ERROR_CODE.contains(errorCode)-> {
                messageError = context.getString(R.string.goto_kyc_error_know_code)
                keyKnowError = KYCConstant.KEY_KNOWN_ERROR_CODE
            }
            else -> {
                messageError = message
            }
        }

        val generateErrorCode = context.getString(R.string.error_code, errorCode)

        return MessageErrorException("$messageError $generateErrorCode", keyKnowError)
    }

    companion object {
        private const val QUESTION_TYPE_DOB = "Date of Birth"
        private const val KEY_CHALLENGE_ID = "challengeID"
        private val LIST_COMMON_ERROR_CODE = listOf("1510", "1511", "1541", "1539", "30009", "30015", "30003", "900", "1546")
    }
}

sealed class GetChallengeResult {
    object Loading : GetChallengeResult()
    data class Success(val questionId: String): GetChallengeResult()
    data class Failed(val throwable: Throwable): GetChallengeResult()
}
