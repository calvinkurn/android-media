package com.tokopedia.shop.common.domain.interactor

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.data.model.ShopPageGetHomeType
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GqlShopPageGetHomeType @Inject constructor(
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ShopPageGetHomeType>() {

    companion object {
        private const val PARAM_SHOP_ID = "shopID"
        private const val PARAM_EXT_PARAM = "extParam"
        private const val KEY_DISTRICT_ID = "districtId"
        private const val KEY_CITY_ID = "cityId"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"

        @JvmStatic
        fun createParams(
            shopId: String,
            extParam: String,
            districtId: String = "",
            cityId: String = "",
            latitude: String = "",
            longitude: String = ""
        ): RequestParams =
            RequestParams.create().apply {
                putObject(PARAM_SHOP_ID, shopId.toIntOrZero())
                putObject(PARAM_EXT_PARAM, extParam)
                putObject(KEY_DISTRICT_ID, districtId)
                putObject(KEY_CITY_ID, cityId)
                putObject(KEY_LATITUDE, latitude)
                putObject(KEY_LONGITUDE, longitude)
            }

        const val QUERY = """
            query shopPageGetHomeType(${'$'}shopID: Int!, ${'$'}extParam: String!, ${'$'}districtId: String,${'$'}cityId: String,${'$'}latitude: String,${'$'}longitude: String){
              shopPageGetHomeType(
                shopID: ${'$'}shopID,
                extParam: ${'$'}extParam,
                districtID: ${'$'}districtId,
                cityID: ${'$'}cityId,
                latitude: ${'$'}latitude,
                longitude: ${'$'}longitude
              ){
                shopHomeType 
                homeLayoutData {
                  layoutID
                  masterLayoutID
                  widgetIDList{
                    widgetID
                    widgetMasterID
                    widgetType
                    widgetName
                  }
                }
                shopLayoutFeatures {
                    name
                    isActive
                }
              }
            }
        """
    }

    var params: RequestParams = RequestParams.EMPTY
    var isFromCacheFirst: Boolean = true
    val request by lazy {
        GraphqlRequest(QUERY, ShopPageGetHomeType.Response::class.java, params.parameters)
    }

    override suspend fun executeOnBackground(): ShopPageGetHomeType {
        gqlUseCase.clearRequest()
        gqlUseCase.addRequest(request)
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopPageGetHomeType.Response::class.java)
        if (error == null || error.isEmpty()) {
            return (gqlResponse.getData(ShopPageGetHomeType.Response::class.java) as ShopPageGetHomeType.Response).shopPageGetHomeType
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.joinToString(separator = ", "))
        }
    }

    fun clearCache(){
        gqlUseCase.clearCache()
    }
}