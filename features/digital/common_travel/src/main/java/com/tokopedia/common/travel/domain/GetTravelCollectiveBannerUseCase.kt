package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.constant.TravelType
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 2019-08-26
 */

class GetTravelCollectiveBannerUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(query: String, product: TravelType, isFromCloud: Boolean)
            : Result<TravelCollectiveBannerModel> {

        if (isFromCloud) useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        else useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())

        useCase.clearRequest()

        return try {
            val params = mapOf(PARAM_BANNER_PRODUCT_KEY to getProductString(product))
            val graphqlRequest = GraphqlRequest(query, TravelCollectiveBannerModel.Response::class.java, params)
            useCase.addRequest(graphqlRequest)
            val travelCollectiveBannerModel = useCase.executeOnBackground().getSuccessData<TravelCollectiveBannerModel.Response>().response

            Success(travelCollectiveBannerModel)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun getProductString(product: TravelType): String {
        return when (product) {
            TravelType.FLIGHT -> PARAM_PRODUCT_FLIGHT
            TravelType.HOTEL -> PARAM_PRODUCT_HOTEL
            TravelType.SUB_HOMEPAGE -> PARAM_PRODUCT_SUB_HOMEPAGE
            else -> PARAM_PRODUCT_ALL
        }
    }

    companion object {
        const val PARAM_BANNER_PRODUCT_KEY = "product"

        const val PARAM_PRODUCT_FLIGHT = "FLIGHT"
        const val PARAM_PRODUCT_HOTEL = "HOTEL"
        const val PARAM_PRODUCT_SUB_HOMEPAGE = "SUBHOMEPAGE"
        const val PARAM_PRODUCT_ALL = "ALL"
    }

}