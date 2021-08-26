package com.tokopedia.graphql.domain

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * This is the base class for the domain layer by using the GraphQL service for Flow Use Case.
 *
 * this class is created in order to achieve flexibility in further development of domain layer.
 * currently, [GqlUseCase] is used in base class for [FlowUseCase], and [FlowStateUseCase].
 *
 * Opened access modifier for repository and graphqlQuery because of
 * reified inline function IllegalAccessError.
 * https://youtrack.jetbrains.com/issue/KT-22625
 */
abstract class GqlFlowUseCase<Input, out Output>(val repository: GraphqlRepository) {

    /*
    * override this to set the graphql query
    * */
    abstract fun graphqlQuery(): String


    /*
    * this is helper function to request network using graphql repository
    * */
    protected suspend inline fun <reified Output> request(params: Map<String, Any>): Flow<Output> {

        return flow {
            val request = GraphqlRequest(graphqlQuery(), Output::class.java, params)
            val response = repository.getReseponse(listOf(request))
            emit(response.getSuccessData())
        }

    }

}