package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.data.SocialNetworkGetConsentResponse
import javax.inject.Inject

class SocialNetworkGetConsentUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, GetRecommendationFriendState>(dispatcher.io) {

    override fun graphqlQuery(): String =
        """
            mutation SocialNetworkGetConsent(${'$'}relationType: Int!) {
              SocialNetworkGetConsent(relationType:${'$'}relationType){
                data {
                  opt_in
                }
                messages
                error_code
              }
            }
        """.trimIndent()

    override suspend fun execute(params: Unit): GetRecommendationFriendState {
        val parameters = mapOf(
            PARAM_RELATION_TYPE to VALUE_RELATION_TYPE
        )

        val response: SocialNetworkGetConsentResponse = graphqlRepository.request(graphqlQuery(), parameters)
        val status = response.socialNetworkGetConsent.data.optIn

        return GetRecommendationFriendState.Success(status)
    }

    companion object {
        private const val PARAM_RELATION_TYPE = "relationType"
        private const val VALUE_RELATION_TYPE = 1
    }
}
