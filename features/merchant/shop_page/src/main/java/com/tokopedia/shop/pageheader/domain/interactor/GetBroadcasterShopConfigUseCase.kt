package com.tokopedia.shop.pageheader.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.graphql.data.shopinfo.Broadcaster
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

/**
 * Created by jegul on 24/07/20
 */
class GetBroadcasterShopConfigUseCase(private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<Broadcaster.Config>() {

    var params: RequestParams = RequestParams.EMPTY
    private var isFromCacheFirst: Boolean = true
    private val query = """
    query getBroadcasterConfig(${'$'}shopId: String!){
        broadcasterGetShopConfig(shopID: ${'$'}shopId)
        {
          streamAllowed
        }
    }
    """
    val request by lazy{
        GraphqlRequest(query, Broadcaster::class.java, params.parameters)
    }

    override suspend fun executeOnBackground(): Broadcaster.Config {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(Broadcaster::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(Broadcaster::class.java) as Broadcaster).config
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_SHOP_IDS = "shopId"

        @JvmStatic
        fun createParams(
                shopId: String
        ): RequestParams = RequestParams.create().apply {
            putObject(PARAM_SHOP_IDS, shopId)
        }

    }
}