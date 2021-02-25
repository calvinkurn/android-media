package com.tokopedia.shop.product.domain.interactor

import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.product.data.model.ShopFeaturedProduct
import com.tokopedia.shop.product.data.model.ShopFeaturedProductParams
import com.tokopedia.usecase.coroutines.UseCase

class GetShopFeaturedProductUseCase (private val gqlQuery: String,
                                     private val gqlUseCase: MultiRequestGraphqlUseCase): UseCase<List<ShopFeaturedProduct>>() {

    var params = mapOf<String, Any>()
    var isFromCacheFirst: Boolean = true

    override suspend fun executeOnBackground(): List<ShopFeaturedProduct> {
        gqlUseCase.clearRequest()
        gqlUseCase.setCacheStrategy(GraphqlCacheStrategy
                .Builder(if (isFromCacheFirst) CacheType.CACHE_FIRST else CacheType.ALWAYS_CLOUD).build())

        val gqlRequest = GraphqlRequest(gqlQuery, ShopFeaturedProduct.Response::class.java, params)
        gqlUseCase.addRequest(gqlRequest)
        val gqlResponse = gqlUseCase.executeOnBackground()
        val error = gqlResponse.getError(ShopFeaturedProduct.Response::class.java)

        if (error == null || error.isEmpty()){
            return gqlResponse.getData<ShopFeaturedProduct.Response>(ShopFeaturedProduct.Response::class.java)
                    .shopFeaturedProductList.data
        } else {
            throw MessageErrorException(error.joinToString(", ") { it.message })
        }
    }

    fun clearCache(){
        gqlUseCase.clearCache()
    }

    companion object{
        private const val PARAM_SHOP_ID = "shopId"
        private const val PARAM_USER_ID = "userID"
        private const val KEY_DISTRICT_ID = "districtId"
        private const val KEY_CITY_ID = "cityId"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"

        @JvmStatic
        fun createParams(
                params: ShopFeaturedProductParams
        ): Map<String, Any> = mapOf(
                PARAM_SHOP_ID to params.shopId.toIntOrZero(),
                PARAM_USER_ID to params.userId.toIntOrZero(),
                KEY_DISTRICT_ID to params.districtId,
                KEY_CITY_ID to params.cityId,
                KEY_LATITUDE to params.latitude,
                KEY_LONGITUDE to params.longitude
        )
    }
}