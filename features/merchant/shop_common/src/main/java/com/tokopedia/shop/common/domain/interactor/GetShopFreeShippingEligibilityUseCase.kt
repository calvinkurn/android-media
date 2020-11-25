package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.shop.common.data.source.cloud.model.ShopFreeShipping
import com.tokopedia.shop.common.data.source.cloud.model.ShopFreeShipping.*
import com.tokopedia.shop.common.data.source.cloud.query.GetShopFreeShippingEligibility
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetShopFreeShippingEligibilityUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository
): GraphqlUseCase<ShopFreeShipping>(graphqlRepository) {

    companion object {
        private const val PARAM_USER_ID = "userID"
        private const val PARAM_SHOP_ID = "shopIDs"

        fun createRequestParams(userId: Int, shopIds: List<Int>): RequestParams {
            return RequestParams().apply {
                putObject(PARAM_USER_ID, userId)
                putObject(PARAM_SHOP_ID, shopIds)
            }
        }
    }

    init {
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build()
        setCacheStrategy(cacheStrategy)

        setGraphqlQuery(GetShopFreeShippingEligibility.QUERY)
        setTypeClass(ShopFreeShipping::class.java)
    }

    suspend fun execute(requestParams: RequestParams): List<Shop> {
        setRequestParams(requestParams.parameters)
        return executeOnBackground().response.shops
    }
}