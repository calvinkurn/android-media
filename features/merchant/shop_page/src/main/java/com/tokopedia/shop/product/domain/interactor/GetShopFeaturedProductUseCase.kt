package com.tokopedia.shop.product.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.usecase.coroutines.UseCase

class GetShopFeaturedProductUseCase (private val gqlQuery: String,
                                     private val gqlUseCase: MultiRequestGraphqlUseCase): UseCase<List<ShopFeaturedProduct>>() {

    var params = mapOf<String, Int>()
    var isFromCacheFirst: Boolean = true

    override suspend fun executeOnBackground(): List<ShopFeaturedProduct> {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlRequest = GraphqlRequest(gqlQuery, ShopFeaturedProduct.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopFeaturedProduct.Response::class.java)

        if (error == null || error.isEmpty()){
            return gqlResponse.getData<ShopFeaturedProduct.Response>(ShopFeaturedProduct.Response::class.java)
                    .shopFeaturedProductList.data
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    companion object{
        private const val PARAM_SHOP_ID = "shopId"

        @JvmStatic
        fun createParams(shopId: Int): Map<String, Int> = mapOf(PARAM_SHOP_ID to shopId)
    }
}