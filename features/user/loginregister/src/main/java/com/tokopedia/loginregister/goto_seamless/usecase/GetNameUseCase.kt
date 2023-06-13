package com.tokopedia.loginregister.goto_seamless.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.loginregister.goto_seamless.model.GetNameParam
import com.tokopedia.loginregister.goto_seamless.model.GetNameResponse
import javax.inject.Inject

class GetNameUseCase @Inject constructor(
    @ApplicationContext
    private val repository: GraphqlRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<GetNameParam, GetNameResponse>(dispatchers.io) {

    override suspend fun execute(params: GetNameParam): GetNameResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = """
       	query getUserName(${'$'}code: String!){
            sso_get_username(code:${'$'}code) {
                fullname
                error
            }
        }""".trimIndent()
}
