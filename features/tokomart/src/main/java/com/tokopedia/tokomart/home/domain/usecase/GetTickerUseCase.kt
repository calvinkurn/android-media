package com.tokopedia.tokomart.home.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.tokomart.home.domain.model.TickerResponse
import com.tokopedia.tokomart.home.domain.query.GetTicker.QUERY
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetTickerUseCase @Inject constructor(
        private val graphqlUseCase: GraphqlUseCase<TickerResponse>
): UseCase<TickerResponse>(){

    companion object {
        const val PAGE = "page"
    }

    var params : Map<String, Any> = mapOf()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(TickerResponse::class.java)
    }

    fun createParams(pageSource: String = "pdp"): Map<String, Any> {
        return mutableMapOf<String, Any>().apply {
            put(PAGE, pageSource)
        }
    }

    override suspend fun executeOnBackground(): TickerResponse {
        graphqlUseCase.clearCache()
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setRequestParams(params)
        return graphqlUseCase.executeOnBackground()
    }
}