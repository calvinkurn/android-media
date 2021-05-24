package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase

class GQLGetShopInfoUseCase(private var gqlQuery: String,
                            private val gqlUseCase: MultiRequestGraphqlUseCase) : UseCase<ShopInfo>() {

    var params: RequestParams = RequestParams.EMPTY
    var isFromCacheFirst: Boolean = true
    val request by lazy{
        GraphqlRequest(gqlQuery, ShopInfo.Response::class.java, params.parameters)
    }

    override suspend fun executeOnBackground(): ShopInfo {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopInfo.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(ShopInfo.Response::class.java) as ShopInfo.Response).result.data.first()
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    companion object {
        private const val PARAM_SHOP_IDS = "shopIds"
        private const val PARAM_SHOP_FIELDS = "fields"
        private const val PARAM_SHOP_DOMAIN = "shopDomain"
        private const val PARAM_SOURCE = "source"
        const val SHOP_PAGE_SOURCE = "gql-shoppage"
        const val MERCHANT_VOUCHER_SOURCE = "gql-mv"
        const val PRODUCT_ADD_SOURCE = "gql-product-add"
        const val PRODUCT_EDIT_SOURCE = "gql-product-edit"
        const val PRODUCT_MANAGE_SOURCE = "gql-product-manage"
        const val FAVORITE_LIST_SOURCE = "gql-fav-list"
        const val SHOP_INFO_SOURCE = "gql-shopinfo-widget"
        const val SHOP_PRODUCT_LIST_RESULT_SOURCE = "gql-productlist-shoppage"
        const val TOP_ADS_SOURCE = "gql-topads"
        const val FIELD_CORE = "core"
        const val FIELD_ASSETS = "assets"
        const val FIELD_LAST_ACTIVE = "last_active"
        const val FIELD_LOCATION = "location"
        const val FIELD_ALLOW_MANAGE = "allow_manage"
        const val FIELD_IS_OWNER = "is_owner"
        const val FIELD_STATUS = "status"
        const val FIELD_IS_OPEN = "is_open"
        const val FIELD_CLOSED_INFO = "closed_info"
        const val FIELD_CREATE_INFO = "create_info"
        const val FIELD_SHOP_SNIPPET = "shop-snippet"
        const val FIELD_OTHER_GOLD_OS = "other-goldos"
        const val FIELD_OS = "os"
        const val FIELD_GOLD = "gold"
        const val FIELD_TOP_CONTENT = "topContent"
        const val FIELD_HOME_TYPE = "shopHomeType"



        private val DEFAULT_SHOP_FIELDS = listOf("core", "favorite", "assets", "shipment",
                "last_active", "location", "terms", "allow_manage",
                "is_owner", "other-goldos", "status", "is_open", "closed_info", "create_info", "shop-snippet")

        @JvmStatic
        fun createParams(
                shopIds: List<Int>,
                shopDomain: String? = null,
                fields: List<String> = DEFAULT_SHOP_FIELDS,
                source: String =  ""
        ): RequestParams = RequestParams.create().apply {
            putObject(PARAM_SHOP_IDS, shopIds)
            putObject(PARAM_SHOP_FIELDS, fields)
            putString(PARAM_SHOP_DOMAIN, shopDomain)
            putString(PARAM_SOURCE, source)
        }

        @JvmStatic
        fun getDefaultShopFields(): List<String> {
            return DEFAULT_SHOP_FIELDS
        }
    }
}