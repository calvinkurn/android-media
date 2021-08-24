package com.tokopedia.graphql.domain

import com.tokopedia.graphql.R
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * This is the base class for the domain layer by using the GraphQL service.
 * This class is the result of an improvement from the existing GraphqlUseCase (or related).
 *
 * this class is created in order to achieve flexibility in further development of domain layer.
 * currently, [GqlUseCase] is used in base class for [CoroutineUseCase], [CoroutineStateUseCase],
 * [FlowUseCase], and [FlowStateUseCase].
 *
 * This class has 3 functions in it:
 * - execute()
 *   This function is to determine the target request that will be managed to get data
 *   from certain sources, both network and local sources.
 *
 * - graphqlQuery()
 *   This function defines the query graphql that will be used to determine what data to get.
 *
 * - request()
 *   this function is a helper to request data to the graphql service based on
 *   predefined parameters and queries.
 */
abstract class GqlFlowUseCase<Input, out Output>(protected val repository: GraphqlRepository) {

    /*
    * override this to set the graphql query
    * */
    protected abstract fun graphqlQuery(): String



    /*
    * this is helper function to request network using graphql repository
    * */
    protected suspend inline fun <reified Output> request(params: Map<String, Any>): Flow<Output> {

        val request = GraphqlRequest(graphqlQuery(), Output::class.java, params)
        val response = repository.getReseponse(listOf(request))

        return flowOf(response.getSuccessData())
    }

}