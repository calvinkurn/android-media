package com.tokopedia.graphql.coroutines.domain.interactor

import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.UseCase

class GraphqlUseCase<out T: Any>(private val multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase,
                                 private val tClass: Class<T>): UseCase<T>() {

    private var cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.NONE).build()
    private var graphqlQuery: String? = null
    private var requestParams = mapOf<String, Any>()

    fun setCacheStrategy(cacheStrategy: GraphqlCacheStrategy){
        this.cacheStrategy = cacheStrategy
    }

    fun setGraphqlQuery(query: String){
        this.graphqlQuery = query
    }

    fun setRequestParams(params: Map<String, Any>){
        this.requestParams = params
    }

    override suspend fun executeOnBackground(): T {
        if (graphqlQuery == null){
            throw RuntimeException("Please set valid GraphQL query parameter before executing the use-case");
        }

        multiRequestGraphqlUseCase.clearRequest()
        multiRequestGraphqlUseCase.addRequest(GraphqlRequest(graphqlQuery, tClass, requestParams))
        multiRequestGraphqlUseCase.setCacheStrategy(cacheStrategy)
        val response = multiRequestGraphqlUseCase.executeOnBackground()
        val error = response.getError(tClass)

        if (error == null || error.isEmpty()){
            return response.getData(tClass)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }
}