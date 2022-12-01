package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.data.DsarCheckEmailResponse
import javax.inject.Inject

class DsarCheckEmailUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, DsarCheckEmailResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        mutation check_email(${'$'}email: String!) {
            userProfileCompletionValidate(email: ${'$'}email) {
                isValid
                emailMessage
            }
        }
    """.trimIndent()

    private fun createParam(param: String): Map<String, Any> {
        return mapOf(PARAM_EMAIL to param)
    }

    override suspend fun execute(params: String): DsarCheckEmailResponse {
        return repository.request(graphqlQuery(), createParam(params))
    }

    companion object {
        const val PARAM_EMAIL = "email"
    }
}
