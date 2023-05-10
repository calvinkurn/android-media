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
          mutation submitKYCChallenge(
            ${'$'}challengeID: String!,
            ${'$'}answers: [kycSubmitGoToChallengeAnswer!]!
          ) {
            submitKYCChallenge(
              challengeID: ${'$'}challengeID,
              answers: ${'$'}answers
            ) {
              isSuccess
              errorMessages
              data {
                challengeID
                questions {
                  id
                  questionType
                  displayText
                  hint
                  type
                }
              }
            }
          }
        """.trimIndent()

    override suspend fun execute(params: SubmitChallengeParam): SubmitChallengeResult {
        val response : SubmitKYCChallenge = repository
            .request<SubmitChallengeParam, SubmitChallengeResponse>(graphqlQuery(), params)
            .submitKYCChallenge

        return if (!response.isSuccess) {
            val message = if (response.errorMessages.isNotEmpty()) {
                response.errorMessages.first()
            } else {
                ""
            }
            SubmitChallengeResult.Failed(MessageErrorException(message))
        } else {
            SubmitChallengeResult.Success()
        }
    }
}
