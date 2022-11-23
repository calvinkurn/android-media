package com.tokopedia.privacycenter.dsar.domain

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.dsar.model.DsarCheckEmailResponse
import javax.inject.Inject

class DsarCheckEmailUseCase @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<Map<String, Any>, DsarCheckEmailResponse>(dispatcher.io) {

    override fun graphqlQuery(): String = """
        mutation check_email(${'$'}email: String!) {
            userProfileCompletionValidate(email: ${'$'}email) {
                isValid
                emailMessage
            }
        }
    """.trimIndent()

    override suspend fun execute(params: Map<String, Any>): DsarCheckEmailResponse {
        return repository.request(graphqlQuery(), params)
    }
}
