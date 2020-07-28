package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoFreeShipping
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfoFreeShipping.*
import com.tokopedia.shop.common.data.source.cloud.query.GetShopFreeShippingInfo
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetShopFreeShippingInfoUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<ShopInfoFreeShipping>(graphqlRepository) {

    companion object {
        private const val PARAM_SHOP_ID = "shopIDs"

        fun createRequestParams(shopIds: List<Int>): RequestParams {
            return RequestParams().apply {
                putObject(PARAM_SHOP_ID, shopIds)
            }
        }
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GetShopFreeShippingInfo.QUERY)
        setTypeClass(ShopInfoFreeShipping::class.java)
    }

    suspend fun execute(requestParams: RequestParams): List<FreeShippingInfo> {
        setRequestParams(requestParams.parameters)
        return executeOnBackground().response.result
    }
}