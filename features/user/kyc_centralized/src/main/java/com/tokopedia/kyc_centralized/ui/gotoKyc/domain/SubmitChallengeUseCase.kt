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
              cooldownTimeInSeconds
              attemptsRemaining
              maximumAttemptsAllowed
              message
              errorMessages
            }
          }
        """.trimIndent()

    override suspend fun execute(params: SubmitChallengeParam): SubmitChallengeResult {
        val response : SubmitKYCChallenge = repository
            .request<SubmitChallengeParam, SubmitChallengeResponse>(graphqlQuery(), params)
            .submitKYCChallenge

        return if (response.errorMessages.isNotEmpty()) {
            SubmitChallengeResult.Failed(MessageErrorException(response.errorMessages.first()))
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
                    SubmitChallengeResult.Success()
                }
            }
        }
    }

    companion object {
        private const val KEY_WRONG_ANSWER = "KYC_CHALLENGE_SUBMITTED_WRONG_ANSWERS"
        private const val KEY_EXHAUSTED = "KYC_CHALLENGE_ATTEMPTS_EXHAUSTED"
    }
}
