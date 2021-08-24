package com.tokopedia.graphql.domain.example

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher

class GetWithParamUseCase(repository: GraphqlRepository, dispatcher: CoroutineDispatcher) :
    CoroutineUseCase<FooInput, FooModel>(repository, dispatcher) {

    override fun graphqlQuery(): String {
        return """
            query get_no_param {
                id
                msg
            }
        """.trimIndent()
    }

    override suspend fun execute(params: FooInput): FooModel {
        return request(params.toMap())
    }

}