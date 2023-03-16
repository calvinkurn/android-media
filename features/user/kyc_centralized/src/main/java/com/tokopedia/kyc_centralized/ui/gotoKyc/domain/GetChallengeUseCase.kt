package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.GetChallengeResponse
import com.tokopedia.kyc_centralized.ui.gotoKyc.data.GetOneKYCChallenge
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class GetChallengeUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<String, GetChallengeResult>(dispatchers.io) {
    override fun graphqlQuery(): String =
        """
            query kycGetGoToChallenge(${'$'}challengeID: String) {
              kycGetGoToChallenge(challengeID: ${'$'}challengeID) {
                isSuccess
                errorMessages
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

        return if (!response.isSuccess) {
            val message = if (response.errorMessages.isNotEmpty()) {
                response.errorMessages.first()
            } else {
                ""
            }
            GetChallengeResult.Failed(MessageErrorException(message))
        } else {
            GetChallengeResult.Success(questionId = response.data.id)
        }
    }

    companion object {
        private const val KEY_CHALLENGE_ID = "challengeID"
    }
}
