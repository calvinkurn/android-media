package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GQLGetShopInfoUseCase (private val gqlQuery: String,
                             private val gqlUseCase: MultiRequestGraphqlUseCase): UseCase<ShopInfo>() {

    var params: RequestParams = RequestParams.EMPTY
    var isFromCacheFirst: Boolean = true

    override suspend fun executeOnBackground(): ShopInfo {
        val gqlRequest = GraphqlRequest(gqlQuery, ShopInfo.Response::class.java, params.parameters)
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopInfo.Response::class.java)
        if (error == null || error.isEmpty()){
            return (gqlResponse.getData(ShopInfo.Response::class.java) as ShopInfo.Response).result.data.first()
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object{
        private const val PARAM_SHOP_IDS = "shopIds"
        private const val PARAM_SHOP_FIELDS = "fields"
        private const val PARAM_SHOP_DOMAIN = "shopDomain"

        private val DEFAULT_SHOP_FIELDS = listOf("core", "favorite", "assets", "shipment",
                "last_active", "location", "terms", "allow_manage",
                "is_owner", "other-goldos", "status","is_open","closed_info","create_info")

        @JvmStatic
        fun createParams(shopIds: List<Int>, shopDomain: String? = null, fields: List<String> = DEFAULT_SHOP_FIELDS): RequestParams
                = RequestParams.create().apply {
            putObject(PARAM_SHOP_IDS, shopIds)
            putObject(PARAM_SHOP_FIELDS, fields)
            putString(PARAM_SHOP_DOMAIN, shopDomain)
        }
    }
}