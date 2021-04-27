package com.tokopedia.shop.common.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopBadge
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShopReputationUseCase @Inject constructor(private val gqlUseCase: MultiRequestGraphqlUseCase): UseCase<ShopBadge>() {

    var params = mapOf<String, Any>()
    var isFromCacheFirst: Boolean = true
    val request by lazy{
        GraphqlRequest(QUERY, ShopBadge.Response::class.java, params)
    }

    override suspend fun executeOnBackground(): ShopBadge {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())
        gqlUseCase.addRequest(request)
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
        private const val QUERY = "query getShopBadge(\$shopIds: [Int!]!){\n" +
                "     reputation_shops(shop_ids: \$shopIds) {\n" +
                "         badge\n" +
                "         badge_hd\n" +
                "         score\n" +
                "         score_map\n" +
                "     }\n" +
                " }"
        @JvmStatic
        fun createParams(shopId: Int): Map<String, Any> = mapOf(PARAM_SHOP_IDS to listOf(shopId))
    }
}