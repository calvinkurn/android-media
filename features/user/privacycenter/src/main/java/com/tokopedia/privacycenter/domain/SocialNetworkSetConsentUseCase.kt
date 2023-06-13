package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.common.PrivacyCenterStateResult
import com.tokopedia.privacycenter.data.SocialNetworkSetConsentResponse
import javax.inject.Inject

class SocialNetworkSetConsentUseCase @Inject constructor(
    @ApplicationContext private val graphqlRepository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Boolean, PrivacyCenterStateResult<Boolean>>(dispatcher.io) {
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

    override suspend fun execute(params: Boolean): PrivacyCenterStateResult<Boolean> {
        val parameters = mapOf(
            PARAM_OPT_IN to params,
            PARAM_RELATION_TYPE to VALUE_RELATION_TYPE
        )

        val response: SocialNetworkSetConsentResponse = graphqlRepository.request(graphqlQuery(), parameters)
        val status = response.socialNetworkSetConsent.data.isSuccess == SET_CONSENT_SUCCESS

        return PrivacyCenterStateResult.Success(status)
    }

    companion object {
        private const val SET_CONSENT_SUCCESS = 1
        private const val PARAM_OPT_IN = "optIn"
        private const val PARAM_RELATION_TYPE = "relationType"
        private const val VALUE_RELATION_TYPE = 1
    }
}
