package com.tokopedia.shop.common.graphql.domain.usecase.shopbasicdata

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.usecase.coroutines.UseCase

class GetShopReputationUseCase (private val gqlQuery: String,
                                private val gqlUseCase: MultiRequestGraphqlUseCase): UseCase<ShopBadge>() {

    var params = mapOf<String, Any>()
    var isFromCacheFirst: Boolean = true

    override suspend fun executeOnBackground(): ShopBadge {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())
        gqlUseCase.addRequest(GraphqlRequest(gqlQuery, ShopBadge.Response::class.java, params))
        val gqlResponse = gqlUseCase.executeOnBackground()

        val gqlError = gqlResponse.getError(ShopBadge.Response::class.java)
        if (gqlError?.isNotEmpty() != true){
            return gqlResponse.getData<ShopBadge.Response>(ShopBadge.Response::class.java)
                    .result.first()
        } else {
            throw MessageErrorException(gqlError.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_SHOP_IDS = "shopIds"

        @JvmStatic
        fun createParams(shopId: Int): Map<String, Any> = mapOf(PARAM_SHOP_IDS to listOf(shopId))
    }
}