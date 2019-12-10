package com.tokopedia.play.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.play.R
import com.tokopedia.play.data.ShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase


/**
 * Created by mzennis on 2019-12-10.
 */

class GetShopInfoUseCase(
        @ApplicationContext context: Context,
        private val gqlUseCase: MultiRequestGraphqlUseCase
): UseCase<ShopInfo>() {

    var params: RequestParams = RequestParams.EMPTY
    private val query = GraphqlHelper.loadRawString(context.resources, R.raw.gql_get_shop_info)

    override suspend fun executeOnBackground(): ShopInfo {
        val gqlRequest = GraphqlRequest(query, ShopInfo.Response::class.java, params.parameters)
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(gqlRequest)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopInfo.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(ShopInfo.Data::class.java) as ShopInfo.Data).data.shopInfo
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_SHOP_IDS = "shopIds"
        private const val PARAM_SHOP_FIELDS = "fields"

        private val DEFAULT_SHOP_FIELDS = listOf("core", "favorite", "assets", "shipment",
                "last_active", "location", "terms", "allow_manage",
                "is_owner", "other-goldos", "status", "is_open", "closed_info", "create_info")

        fun createParams(shopId: Int, fields: List<String> = DEFAULT_SHOP_FIELDS): RequestParams = RequestParams.create().apply {
            putObject(PARAM_SHOP_IDS, shopId)
            putObject(PARAM_SHOP_FIELDS, fields)
        }
    }
}