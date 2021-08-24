package com.tokopedia.graphql.domain.example

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineStateUseCase
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher

class GetNoParamUseCase(repository: GraphqlRepository, dispatcher: CoroutineDispatcher) :
    CoroutineUseCase<Unit, FooModel>(repository, dispatcher) {

    override fun graphqlQuery(): String {
        return """
            query GetNoParam {
                FooResponse {
                    id
                    msg
                }
            }
        """.trimIndent()
    }

    override suspend fun execute(params: Unit): FooModel {
        return request(emptyMap())
    }

}

class GetNoParamStateUseCase(repository: GraphqlRepository, dispatcher: CoroutineDispatcher) :
    CoroutineStateUseCase<Unit, FooModel>(repository, dispatcher) {

    override fun graphqlQuery(): String {
        return """
            query GetNoParam {
                FooResponse {
                    id
                    msg
                }
            }
        """.trimIndent()
    }

    override suspend fun execute(params: Unit): FooModel {
        return request(emptyMap())
    }

}