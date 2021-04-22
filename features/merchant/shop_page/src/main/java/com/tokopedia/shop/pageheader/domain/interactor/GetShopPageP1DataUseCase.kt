package com.tokopedia.shop.pageheader.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.feedcomponent.data.pojo.whitelist.WhitelistQuery
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.SHOP_PAGE_SOURCE
import com.tokopedia.shop.common.domain.interactor.GqlGetIsShopOsUseCase
import com.tokopedia.shop.common.domain.interactor.GqlGetIsShopPmUseCase
import com.tokopedia.shop.common.graphql.data.isshopofficial.GetIsShopOfficialStore
import com.tokopedia.shop.common.graphql.data.isshoppowermerchant.GetIsShopPowerMerchant
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.pageheader.ShopPageHeaderConstant
import com.tokopedia.shop.pageheader.data.model.ShopPageGetHomeType
import com.tokopedia.shop.pageheader.data.model.ShopPageHeaderP1
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetShopPageP1DataUseCase @Inject constructor(
        @Named(GQLQueryNamedConstant.SHOP_PAGE_P1_QUERIES)
        private var mapQuery: Map<String, String>,
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ShopPageHeaderP1>() {

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_SHOP_DOMAIN = "shopDomain"

        @JvmStatic
        fun createParams(
                shopId: Int,
                shopDomain: String
        ): RequestParams = RequestParams.create().apply {
            putObject(PARAM_SHOP_ID, shopId)
            putObject(PARAM_SHOP_DOMAIN, shopDomain)
        }
    }

    var isFromCacheFirst: Boolean = true
    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): ShopPageHeaderP1 {
        val shopId: Int = params.getInt(PARAM_SHOP_ID, 0)
        val shopDomain: String = params.getString(PARAM_SHOP_DOMAIN, "")
        val listRequest = mutableListOf<GraphqlRequest>().apply {
            add(getIsShopOfficialRequest(shopId))
            add(getIsShopPowerMerchantRequest(shopId))
            add(getShopInfoTopContentDataRequest(shopId, shopDomain))
            add(getShopInfoHomeTypeDataRequest(shopId))
            add(getShopInfoCoreAndAssetsDataRequest(shopId, shopDomain))
            add(getFeedWhitelistRequest(shopId))
        }
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(
                if (isFromCacheFirst)
                    CacheType.CACHE_FIRST
                else CacheType.ALWAYS_CLOUD
        ).build())
        gqlUseCase.addRequests(listRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val getIsOfficialData = getResponseData<GetIsShopOfficialStore.Response>(gqlResponse).result
        val getIsShopPowerMerchant = getResponseData<GetIsShopPowerMerchant.Response>(gqlResponse).result
        val getShopInfoTopContentData = getResponseData<ShopInfo.Response>(gqlResponse).result.data.first()
        val getShopInfoHomeTypeData = getResponseData<ShopPageGetHomeType.Response>(gqlResponse).shopPageGetHomeType
        val getShopInfoCoreAndAssetsData = getResponseData<ShopInfo.Response>(gqlResponse).result.data.first()
        val getFeedWhitelist = getResponseData<WhitelistQuery>(gqlResponse).whitelist
        return ShopPageHeaderP1(
                getIsOfficialData,
                getIsShopPowerMerchant,
                getShopInfoTopContentData,
                getShopInfoHomeTypeData,
                getShopInfoCoreAndAssetsData,
                getFeedWhitelist
        )
    }

    private fun getIsShopOfficialRequest(shopId: Int): GraphqlRequest {
        val params = GqlGetIsShopOsUseCase.createParams(shopId)
        return createGraphqlRequest<GetIsShopOfficialStore.Response>(
                mapQuery[GQLQueryNamedConstant.GET_IS_OFFICIAL].orEmpty(),
                params.parameters
        )
    }

    private fun getIsShopPowerMerchantRequest(shopId: Int): GraphqlRequest {
        val params = GqlGetIsShopPmUseCase.createParams(shopId)
        return createGraphqlRequest<GetIsShopPowerMerchant.Response>(
                mapQuery[GQLQueryNamedConstant.GET_IS_POWER_MERCHANT].orEmpty(),
                params.parameters
        )
    }

    private fun getShopInfoTopContentDataRequest(shopId: Int, shopDomain: String): GraphqlRequest {
        val params = GQLGetShopInfoUseCase.createParams(
                if (shopId == 0) listOf() else listOf(shopId),
                shopDomain,
                source = SHOP_PAGE_SOURCE,
                fields = listOf(GQLGetShopInfoUseCase.FIELD_TOP_CONTENT)
        )
        return createGraphqlRequest<ShopInfo.Response>(
                mapQuery[GQLQueryNamedConstant.SHOP_INFO_FOR_TOP_CONTENT].orEmpty(),
                params.parameters
        )
    }

    private fun getShopInfoHomeTypeDataRequest(shopId: Int): GraphqlRequest {
        val params = GqlShopPageGetHomeType.createParams(shopId)
        return createGraphqlRequest<ShopPageGetHomeType.Response>(
                GqlShopPageGetHomeType.QUERY,
                params.parameters
        )
    }

    private fun getShopInfoCoreAndAssetsDataRequest(shopId: Int, shopDomain: String): GraphqlRequest {
        val params = GQLGetShopInfoUseCase.createParams(
                if (shopId == 0) listOf() else listOf(shopId),
                shopDomain,
                source = SHOP_PAGE_SOURCE,
                fields = listOf(GQLGetShopInfoUseCase.FIELD_CORE, GQLGetShopInfoUseCase.FIELD_ASSETS)
        )
        return createGraphqlRequest<ShopInfo.Response>(
                mapQuery[GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS].orEmpty(),
                params.parameters
        )
    }

    private fun getFeedWhitelistRequest(shopId: Int): GraphqlRequest {
        val params = GetWhitelistUseCase.createRequestParams(
                GetWhitelistUseCase.WHITELIST_SHOP,
                shopId.toString()
        )
        return createGraphqlRequest<WhitelistQuery>(
                mapQuery[ShopPageHeaderConstant.SHOP_PAGE_FEED_WHITELIST].orEmpty(),
                params
        )
    }

    private inline fun <reified T> createGraphqlRequest(
            query: String,
            parameters: Map<String, Any>
    ): GraphqlRequest {
        return GraphqlRequest(query, T::class.java, parameters)
    }

    private inline fun <reified T> getResponseData(response: GraphqlResponse): T {
        val error = response.getError(T::class.java)
        if (error == null || error.isEmpty()) {
            return response.getData<T>(T::class.java)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }
}