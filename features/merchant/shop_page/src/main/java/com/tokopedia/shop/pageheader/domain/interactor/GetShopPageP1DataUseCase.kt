package com.tokopedia.shop.pageheader.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant
import com.tokopedia.shop.common.data.model.ShopPageGetDynamicTabResponse
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.SHOP_PAGE_SOURCE
import com.tokopedia.shop.common.domain.interactor.GqlShopPageGetDynamicTabUseCase
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.pageheader.data.model.NewShopPageHeaderP1
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetShopPageP1DataUseCase @Inject constructor(
    @Named(GQLQueryNamedConstant.SHOP_PAGE_P1_QUERIES)
    private var mapQuery: Map<String, String>,
    private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<NewShopPageHeaderP1>() {

    companion object {
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_SHOP_DOMAIN = "shopDomain"
        private const val PARAM_EXT_PARAM = "extParam"
        private const val KEY_DISTRICT_ID = "districtId"
        private const val KEY_CITY_ID = "cityId"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
        private const val KEY_TAB_NAME = "tabName"
        private const val KEY_CONNECTION_TYPE = "connectionType"

        @JvmStatic
        fun createParams(
            shopId: String,
            shopDomain: String,
            extParam: String,
            widgetUserAddressLocalData: LocalCacheModel,
            tabName: String,
            connectionType: String
        ): RequestParams = RequestParams.create().apply {
            putObject(PARAM_SHOP_ID, shopId)
            putObject(PARAM_SHOP_DOMAIN, shopDomain)
            putObject(PARAM_EXT_PARAM, extParam)
            putObject(KEY_DISTRICT_ID, widgetUserAddressLocalData.district_id)
            putObject(KEY_CITY_ID, widgetUserAddressLocalData.city_id)
            putObject(KEY_LATITUDE, widgetUserAddressLocalData.lat)
            putObject(KEY_LONGITUDE, widgetUserAddressLocalData.long)
            putObject(KEY_TAB_NAME, tabName)
            putObject(KEY_CONNECTION_TYPE, connectionType)
        }
    }

    var isFromCacheFirst: Boolean = true
    var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): NewShopPageHeaderP1 {
        val shopId: String = params.getString(PARAM_SHOP_ID, "")
        val shopDomain: String = params.getString(PARAM_SHOP_DOMAIN, "")
        val extParam: String = params.getString(PARAM_EXT_PARAM, "")
        val districtId: String = params.getString(KEY_DISTRICT_ID, "")
        val cityId: String = params.getString(KEY_CITY_ID, "")
        val latitude: String = params.getString(KEY_LATITUDE, "")
        val longitude: String = params.getString(KEY_LONGITUDE, "")
        val tabName: String = params.getString(KEY_TAB_NAME, "")
        val connectionType: String = params.getString(KEY_CONNECTION_TYPE, "")

        val listRequest = mutableListOf<GraphqlRequest>().apply {
            add(
                getShopDynamicTabDataRequest(
                    shopId,
                    extParam,
                    districtId,
                    cityId,
                    latitude,
                    longitude,
                    tabName,
                    connectionType
                )
            )
            add(getShopInfoCoreAndAssetsDataRequest(shopId, shopDomain))
        }
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(
            GraphqlCacheStrategy.Builder(
                if (isFromCacheFirst) {
                    CacheType.CACHE_FIRST
                } else {
                    CacheType.ALWAYS_CLOUD
                }
            ).build()
        )
        gqlUseCase.addRequests(listRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val getShopDynamicTabData = getResponseData<ShopPageGetDynamicTabResponse>(gqlResponse)
        val getShopInfoCoreAndAssetsData = getResponseData<ShopInfo.Response>(gqlResponse).result.data.first()
        return NewShopPageHeaderP1(
            shopPageGetDynamicTabResponse = getShopDynamicTabData,
            shopInfoCoreAndAssetsData = getShopInfoCoreAndAssetsData
        )
    }

    private fun getShopDynamicTabDataRequest(
        shopId: String,
        extParam: String,
        districtId: String,
        cityId: String,
        latitude: String,
        longitude: String,
        tabName: String,
        connectionType: String
    ): GraphqlRequest {
        val params = GqlShopPageGetDynamicTabUseCase.createParams(
            shopId = shopId.toIntOrZero(),
            extParam = extParam,
            districtId = districtId,
            cityId = cityId,
            latitude = latitude,
            longitude = longitude,
            tabName = tabName,
            connectionType = connectionType
        )
        return createGraphqlRequest<ShopPageGetDynamicTabResponse>(
            query = GqlShopPageGetDynamicTabUseCase.QUERY,
            parameters = params.parameters
        )
    }

    private fun getShopInfoCoreAndAssetsDataRequest(shopId: String, shopDomain: String): GraphqlRequest {
        val params = GQLGetShopInfoUseCase.createParams(
            if (shopId.toIntOrZero() == 0) listOf() else listOf(shopId.toIntOrZero()),
            shopDomain,
            source = SHOP_PAGE_SOURCE,
            fields = listOf(GQLGetShopInfoUseCase.FIELD_CORE, GQLGetShopInfoUseCase.FIELD_ASSETS, GQLGetShopInfoUseCase.FIELD_OTHER_GOLD_OS)
        )
        return createGraphqlRequest<ShopInfo.Response>(
            mapQuery[GQLQueryNamedConstant.SHOP_INFO_FOR_CORE_AND_ASSETS].orEmpty(),
            params.parameters
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
