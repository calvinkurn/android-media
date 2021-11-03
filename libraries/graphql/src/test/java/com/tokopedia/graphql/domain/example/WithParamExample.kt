package com.tokopedia.graphql.domain.example

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher

data class FooInput(val id: Int, val isSorted: Boolean) {
    fun toMap(): Map<String, Any> = mapOf(
        "id" to id,
        "isSorted" to isSorted
    )
}

class GetWithParamUseCase(private val repository: GraphqlRepository, dispatcher: CoroutineDispatcher) :
    CoroutineUseCase<FooInput, FooModel>(dispatcher) {

    override fun graphqlQuery(): String {
        return """
            query GetWithParam(${'$'}id: String!, ${'$'}isSorted: Boolean!) {
                FooResponse(param1: ${'$'}id, param2: ${'$'}isSorted) {
                    id
                    msg
                }
            }
        """.trimIndent()
    }

    override suspend fun execute(params: FooInput): FooModel {
        return repository.request(graphqlQuery(), params.toMap())
    }

}