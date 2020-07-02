package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GetBroadcasterShopConfigUseCase(private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<Broadcaster.Config>() {

    var params: RequestParams = RequestParams.EMPTY
    var isFromCacheFirst: Boolean = true
    private val query = """
    query getBroadcasterConfig(${'$'}shopId: String!){
        broadcasterGetShopConfig(shopID: ${'$'}shopId)
        {
          streamAllowed
        }
    }
    """
    val request by lazy{
        GraphqlRequest(query, Broadcaster.Config::class.java, params.parameters)
    }

    override suspend fun executeOnBackground(): Broadcaster.Config {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopInfo.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(Broadcaster.Config::class.java) as Broadcaster.Config)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_SHOP_IDS = "shopId"

        @JvmStatic
        fun createParams(
                shopId: Int
        ): RequestParams = RequestParams.create().apply {
            putObject(PARAM_SHOP_IDS, shopId.toString())
        }

    }
}