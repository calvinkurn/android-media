package com.tokopedia.graphql.coroutines.domain.interactor

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest

abstract class UseCase {

    /*
    * override this to set the graphql query
    * */
    protected abstract fun graphqlQuery(): String

    /*
    * this is helper function to request network using graphql repository
    * */
    @Suppress("UNCHECKED_CAST")
    protected suspend inline fun <reified T, reified P> request(
        repository: GraphqlRepository,
        params: P
    ): T {
        val parameters = if (params is Map<*, *>) params as Map<String, Any> else mapOf()

        val request = GraphqlRequest(graphqlQuery(), T::class.java, parameters)
        val response = repository.getReseponse(listOf(request))

        return response.getSuccessData()
    }

}