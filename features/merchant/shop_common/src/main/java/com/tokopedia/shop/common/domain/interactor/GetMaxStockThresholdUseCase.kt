package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.GraphqlConstant
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop.common.data.source.cloud.model.MaxStockThresholdResponse
import com.tokopedia.shop.common.data.source.cloud.query.GetMaxStockThresholdQuery
import com.tokopedia.shop.common.data.source.cloud.query.GetMaxStockThresholdQuery.PARAM_SHOP_ID
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetMaxStockThresholdUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<MaxStockThresholdResponse>(graphqlRepository) {

    init {
        setCacheStrategy(createCacheStrategy())
        setGraphqlQuery(GetMaxStockThresholdQuery)
        setTypeClass(MaxStockThresholdResponse::class.java)
    }

    private fun createCacheStrategy(): GraphqlCacheStrategy {
        return GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST)
            .setExpiryTime(GraphqlConstant.ExpiryTimes.WEEK.`val`())
            .setSessionIncluded(true)
            .build()
    }

    suspend fun execute(
        shopId: String
    ): MaxStockThresholdResponse {
        setRequestParams(RequestParams.create().apply {
            putString(PARAM_SHOP_ID, shopId)
        }.parameters)
        return executeOnBackground()
    }
}