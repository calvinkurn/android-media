package com.tokopedia.shop.info.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.info.data.GQLQueryStringConst
import com.tokopedia.shop.info.data.model.ProductShopPackSpeed
import com.tokopedia.shop.info.data.model.ShopRatingStats
import com.tokopedia.shop.info.data.model.ShopSatisfaction
import com.tokopedia.shop.info.data.model.ShopStatisticsResp
import com.tokopedia.usecase.coroutines.UseCase

class GetShopStatisticUseCase (private val gqlQueries: Map<String, String>,
                                private val gqlUseCase: MultiRequestGraphqlUseCase): UseCase<ShopStatisticsResp>() {

    var params = mapOf<String, Int>()
    var isFromCacheFirst: Boolean = true


    override suspend fun executeOnBackground(): ShopStatisticsResp {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlRequestShopPackSpeed = GraphqlRequest(gqlQueries[GQLQueryStringConst.GET_SHOP_PACK_SPEED],
                ProductShopPackSpeed.Response::class.java, params)
        val gqlRequestShopRating = GraphqlRequest(gqlQueries[GQLQueryStringConst.GET_SHOP_RATING],
                ShopRatingStats.Response::class.java, params)
        val gqlRequestShopSatisfaction = GraphqlRequest(gqlQueries[GQLQueryStringConst.GET_SHOP_SATISFACTION],
                ShopSatisfaction.Response::class.java, params)

        gqlUseCase.addRequests(listOf(gqlRequestShopPackSpeed,
                gqlRequestShopRating, gqlRequestShopSatisfaction))


        val gqlResponse = gqlUseCase.executeOnBackground()

        val shopPackSpeed = if (gqlResponse.getError(ProductShopPackSpeed.Response::class.java)?.isNotEmpty() != true){
            gqlResponse.getData<ProductShopPackSpeed.Response>(ProductShopPackSpeed.Response::class.java).productShopPackSpeed
        } else null

        val shopRating = if (gqlResponse.getError(ShopRatingStats.Response::class.java)?.isNotEmpty() != true){
            gqlResponse.getData<ShopRatingStats.Response>(ShopRatingStats.Response::class.java).shopRatingStats
        } else null

        val shopSatisfaction = if (gqlResponse.getError(ShopSatisfaction.Response::class.java)?.isNotEmpty() != true){
            gqlResponse.getData<ShopSatisfaction.Response>(ShopSatisfaction.Response::class.java).shopSatisfaction
        } else null

        return ShopStatisticsResp(null, shopPackSpeed, shopRating, shopSatisfaction)
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopId"

        @JvmStatic
        fun createParams(shopId: Int): Map<String, Int> = mapOf(PARAM_SHOP_ID to shopId)
    }
}