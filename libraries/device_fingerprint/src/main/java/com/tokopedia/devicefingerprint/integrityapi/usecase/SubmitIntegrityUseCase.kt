package com.tokopedia.devicefingerprint.integrityapi.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.devicefingerprint.integrityapi.model.IntegrityApiResponse
import com.tokopedia.devicefingerprint.integrityapi.model.IntegrityParam
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class SubmitIntegrityUseCase @Inject constructor(
    val repository: GraphqlRepository,
    dispatcher: CoroutineDispatchers
): CoroutineUseCase<IntegrityParam, IntegrityApiResponse>(dispatcher.io) {

    override suspend fun execute(params: IntegrityParam): IntegrityApiResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String  =
        """
        mutation submit_integrity_api(${'$'}payload: String!, ${'$'}error: String!, ${'$'}error_code: String!, ${'$'}event:String!) {
            subGPlayInt(input: {payload: ${'$'}payload, error: ${'$'}error, error_code: ${'$'}error_code, event: ${'$'}event}) {
                is_error
                data {
                    error_message
                }
            }
        }
        """.trimIndent()
}