package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.SubmitChallengeResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.SubmitKYCChallenge
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class SubmitChallengeUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
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
              isSuccess
              errorMessages
            }
          }
        """.trimIndent()

    override suspend fun execute(params: SubmitChallengeParam): SubmitChallengeResult {
        val response : SubmitKYCChallenge = repository
            .request<SubmitChallengeParam, SubmitChallengeResponse>(graphqlQuery(), params)
            .submitKYCChallenge

        return if (response.isSuccess != VALUE_SUCCESS) {
            val message = if (response.errorMessages.isNotEmpty()) {
                response.errorMessages.first()
            } else {
                ""
            }
            SubmitChallengeResult.Failed(MessageErrorException(message))
        } else {
            when (response.submitStatus) {
                KEY_WRONG_ANSWER -> {
                    //TODO: change this value when BE was ready
                    SubmitChallengeResult.WrongAnswer("Tanggal lahir nggak cocok. Sisa 2 kali coba lagi, ya.")
                }
                KEY_EXHAUSTED -> {
                    SubmitChallengeResult.Exhausted()
                }
                else -> {
                    SubmitChallengeResult.Success()
                }
            }
        }
    }

    companion object {
        private const val VALUE_SUCCESS = 1
        private const val KEY_WRONG_ANSWER = "KYC_CHALLENGE_SUBMITTED_WRONG_ANSWERS"
        private const val KEY_EXHAUSTED = "KYC_CHALLENGE_ATTEMPTS_EXHAUSTED"
    }
}
