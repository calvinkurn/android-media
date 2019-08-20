package com.tokopedia.graphql.coroutines.domain.interactor

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.usecase.coroutines.UseCase

class MultiRequestGraphqlUseCase(private val graphqlRepository: GraphqlRepository): UseCase<GraphqlResponse>() {

    private val requests = mutableListOf<GraphqlRequest>()
    private var cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.NONE).build()

    fun addRequest(request: GraphqlRequest){
        requests += request
    }

    fun addRequests(requests: List<GraphqlRequest>){
        this.requests += requests
    }

    fun clearRequest() = requests.clear()

    fun setCacheStrategy(cacheStrategy: GraphqlCacheStrategy){
        this.cacheStrategy = cacheStrategy
    }

    override suspend fun executeOnBackground(): GraphqlResponse {
        if (requests.isEmpty()){
            throw RuntimeException("Please set valid request parameter before executing the use-case");
        }
        return graphqlRepository.getReseponse(requests, cacheStrategy)
    }
}