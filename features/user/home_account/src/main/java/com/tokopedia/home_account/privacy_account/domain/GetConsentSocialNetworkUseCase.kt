package com.tokopedia.home_account.privacy_account.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.privacy_account.data.GetConsentDataModel
import javax.inject.Inject

class GetConsentSocialNetworkUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Unit, GetConsentDataModel>(dispatcher.io) {

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

    override suspend fun execute(params: Unit): GetConsentDataModel {
        val parameters = mapOf(
            PARAM_RELATION_TYPE to VALUE_RELATION_TYPE
        )
        return graphqlRepository.request(graphqlQuery(), parameters)
    }

    companion object {
        private const val PARAM_RELATION_TYPE = "relationType"
        private const val VALUE_RELATION_TYPE = 1
    }
}
