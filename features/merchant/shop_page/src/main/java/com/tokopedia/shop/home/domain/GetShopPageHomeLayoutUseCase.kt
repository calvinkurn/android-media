package com.tokopedia.shop.home.domain

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.*
import com.tokopedia.shop.home.GqlQueryConstant.GQL_GET_SHOP_PAGE_HOME_LAYOUT
import com.tokopedia.shop.home.data.model.ShopLayoutWidget
import com.tokopedia.shop.home.data.model.ShopLayoutWidgetParamsModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import javax.inject.Named

class GetShopPageHomeLayoutUseCase @Inject constructor(
        @Named(GQL_GET_SHOP_PAGE_HOME_LAYOUT)
        val gqlQuery: String,
        private val gqlUseCase: MultiRequestGraphqlUseCase
) : UseCase<ShopLayoutWidget>() {

    companion object {
        private const val KEY_SHOP_ID = "shopId"
        private const val KEY_STATUS = "status"
        private const val KEY_LAYOUT_ID = "layoutId"
        private const val KEY_DISTRICT_ID = "districtId"
        private const val KEY_CITY_ID = "cityId"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"


        @JvmStatic
        fun createParams(
                paramsModel: ShopLayoutWidgetParamsModel
        ) = mapOf<String, Any>(
                KEY_SHOP_ID to paramsModel.shopId,
                KEY_STATUS to paramsModel.status,
                KEY_LAYOUT_ID to paramsModel.layoutId,
                KEY_DISTRICT_ID to paramsModel.districtId,
                KEY_CITY_ID to paramsModel.cityId,
                KEY_LATITUDE to paramsModel.latitude,
                KEY_LONGITUDE to paramsModel.longitude
        )
    }

    var params = mapOf<String, Any>()

    override suspend fun executeOnBackground(): ShopLayoutWidget {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.CLOUD_THEN_CACHE).build())

        val gqlRequest = GraphqlRequest(gqlQuery, ShopLayoutWidget.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(GraphqlError::class.java)
        if (error == null || error.isEmpty()) {
            return gqlResponse.getData<ShopLayoutWidget.Response>(ShopLayoutWidget.Response::class.java)
                    .shopLayoutWidget
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun clearCache() {
        gqlUseCase.clearCache()
    }

}