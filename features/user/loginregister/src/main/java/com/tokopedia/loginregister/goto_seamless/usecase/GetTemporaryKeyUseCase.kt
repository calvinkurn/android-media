package com.tokopedia.loginregister.goto_seamless.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.goto_seamless.model.GetTemporaryKeyParam
import com.tokopedia.loginregister.goto_seamless.model.TempKeyResponse
import javax.inject.Inject

class GetTemporaryKeyUseCase @Inject constructor(
    @ApplicationContext
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetTemporaryKeyParam, TempKeyResponse>(dispatchers.io) {

    override suspend fun execute(params: GetTemporaryKeyParam): TempKeyResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
       	query getTemporaryKey(${'$'}module: String!, ${'$'}current_token: String!){
            generate_key(module:${'$'}module, current_token:${'$'}current_token) {
                key
            }
        }""".trimIndent()

    companion object {
        const val MODULE_GOTO_SEAMLESS = "goto_seamless"
    }
}