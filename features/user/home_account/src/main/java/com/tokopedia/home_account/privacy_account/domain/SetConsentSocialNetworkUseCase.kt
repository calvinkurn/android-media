package com.tokopedia.home_account.privacy_account.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.home_account.privacy_account.data.SetConsentDataModel
import javax.inject.Inject

class SetConsentSocialNetworkUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Boolean, SetConsentDataModel>(dispatcher.io) {
    override fun graphqlQuery(): String =
        """
            mutation SocialNetworkSetConsent(${'$'}optIn: Boolean!, ${'$'}relationType: Int!) {
              SocialNetworkSetConsent(optIn:${'$'}optIn, relationType:${'$'}relationType) {
                data {
                  is_success
                }
                messages
                error_code
              }
            }
        """.trimIndent()

    override suspend fun execute(params: Boolean): SetConsentDataModel {
        val parameters = mapOf(
            PARAM_OPT_IN to params,
            PARAM_RELATION_TYPE to VALUE_RELATION_TYPE
        )
        return graphqlRepository.request(graphqlQuery(), parameters)
    }

    companion object {
        private const val PARAM_OPT_IN = "optIn"
        private const val PARAM_RELATION_TYPE = "relationType"
        private const val VALUE_RELATION_TYPE = 1
    }
}
