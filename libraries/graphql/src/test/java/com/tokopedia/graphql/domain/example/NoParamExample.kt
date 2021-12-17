package com.tokopedia.graphql.domain.example

import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.data.extensions.requestAsFlow
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineStateUseCase
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.graphql.domain.flow.FlowStateUseCase
import com.tokopedia.graphql.domain.flow.FlowUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNoParamUseCase(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, FooModel>(dispatcher) {

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
        return repository.request(graphqlQuery(), params)
    }

}

class GetNoParamStateUseCase(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineStateUseCase<Unit, FooModel>(dispatcher) {

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
        return repository.request(graphqlQuery(), params)
    }

}

class GetNoParamFlowUseCase(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, FooModel>(dispatcher) {

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

    override suspend fun execute(params: Unit): Flow<FooModel> {
        return repository.requestAsFlow(graphqlQuery(), params)
    }

}

class GetNoParamFlowStateUseCase(
    private val repository: GraphqlRepository,
    dispatcher: CoroutineDispatcher
) : FlowStateUseCase<Unit, FooModel>(dispatcher) {

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

    override suspend fun execute(params: Unit): Flow<Result<FooModel>> {
        return repository
            .requestAsFlow<Unit, FooModel>(graphqlQuery(), params)
            .map { Success(it) }
    }

}