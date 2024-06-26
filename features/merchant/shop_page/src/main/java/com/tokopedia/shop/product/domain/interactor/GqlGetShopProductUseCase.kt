package com.tokopedia.shop.product.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.constant.ShopPageGqlQueryConstant
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

// This Use case can only be used by Shop Page feature since the PARAM_SOURCE is hardcoded to "shop".
// Incase any other feature wants to use this UseCase, then you may need to modify that source to be dynamic
class GqlGetShopProductUseCase @Inject constructor(
    private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ShopProduct.GetShopProduct>() {

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ShopProduct.GetShopProduct {
        gqlUseCase.clearRequest()
        val gqlCacheStrategyBuilder = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
        gqlUseCase.setCacheStrategy(gqlCacheStrategyBuilder.build())
        val gqlRequest = GraphqlRequest(ShopPageGqlQueryConstant.getShopProductQuery(), ShopProduct.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopProduct.Response::class.java)

        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ShopProduct.Response>(ShopProduct.Response::class.java)
                .getShopProduct
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun clearCache() {
        gqlUseCase.clearCache()
    }

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_FILTER = "filter"
        private const val PARAM_SOURCE = "source"
        private const val PARAM_VALUE_SOURCE = "shop"

        @JvmStatic
        fun createParams(
            shopId: String,
            filter: ShopProductFilterInput
        ): Map<String, Any> {
            return mapOf(
                PARAM_SHOP_ID to shopId,
                PARAM_FILTER to filter,
                PARAM_SOURCE to PARAM_VALUE_SOURCE
            )
        }
            
    }
}
