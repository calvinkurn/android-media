package com.tokopedia.graphql.coroutines.domain.interactor

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest

abstract class UseCase {

    /*
    * override this to set the graphql query
    * */
    protected abstract fun graphqlQuery(): String

    protected suspend inline fun <reified T> GraphqlRepository.request(
        params: Map<String, Any>?
    ): T {
        val request = GraphqlRequest(graphqlQuery(), T::class.java, params?: mapOf())
        val response = getReseponse(listOf(request))
        return response.getSuccessData()
    }

}