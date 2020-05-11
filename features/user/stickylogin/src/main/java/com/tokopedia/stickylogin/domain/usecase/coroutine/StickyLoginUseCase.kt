package com.tokopedia.stickylogin.domain.usecase.coroutine

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.stickylogin.data.StickyLoginTickerPojo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class StickyLoginUseCase(
        private val graphqlUseCase: GraphqlUseCase<StickyLoginTickerPojo.TickerResponse>
): UseCase<StickyLoginTickerPojo.TickerResponse>(){
    private val params: MutableMap<String, Any> = mutableMapOf()

    init {
        graphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        graphqlUseCase.setTypeClass(StickyLoginTickerPojo.TickerResponse::class.java)
    }
    override suspend fun executeOnBackground(): StickyLoginTickerPojo.TickerResponse {
        graphqlUseCase.clearCache()
        graphqlUseCase.setRequestParams(params)
        return graphqlUseCase.executeOnBackground()
    }

    fun setParam(params: RequestParams){
        this.params.run {
            clear()
            putAll(params.parameters)
        }
    }
}