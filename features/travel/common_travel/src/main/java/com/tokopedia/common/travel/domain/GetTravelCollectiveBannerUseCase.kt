package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.constant.TravelType
import com.tokopedia.common.travel.data.TravelBannerGQLQuery
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import rx.Subscriber
import javax.inject.Inject

/**
 * @author by jessica on 2019-08-26
 */

class GetTravelCollectiveBannerUseCase @Inject constructor(private val multiRequestGraphqlUseCase: MultiRequestGraphqlUseCase,
                                                           private val graphqlUseCase: GraphqlUseCase) {

    suspend fun execute(product: TravelType, isFromCloud: Boolean)
            : Result<TravelCollectiveBannerModel> {

        if (isFromCloud) multiRequestGraphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        else multiRequestGraphqlUseCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())

        multiRequestGraphqlUseCase.clearRequest()

        return try {
            val params = mapOf(PARAM_BANNER_PRODUCT_KEY to getProductString(product))
            val graphqlRequest = GraphqlRequest(TravelBannerGQLQuery.QUERY_COLLECTIVE_BANNER,
                    TravelCollectiveBannerModel.Response::class.java, params)
            multiRequestGraphqlUseCase.addRequest(graphqlRequest)
            val travelCollectiveBannerModel = multiRequestGraphqlUseCase.executeOnBackground().getSuccessData<TravelCollectiveBannerModel.Response>().response

            Success(travelCollectiveBannerModel)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    fun executeRx(query: String, requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        requestParams?.let {
            val graphqlRequest = GraphqlRequest(query, TravelCollectiveBannerModel.Response::class.java, it.parameters)
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(graphqlRequest)
            graphqlUseCase.execute(requestParams, subscriber)
        }
    }

    fun createRequestParams(product: TravelType): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_BANNER_PRODUCT_KEY, getProductString(product))
        return requestParams
    }

    private fun getProductString(product: TravelType): String {
        return when (product) {
            TravelType.FLIGHT -> PARAM_PRODUCT_FLIGHT
            TravelType.HOTEL -> PARAM_PRODUCT_HOTEL
            TravelType.SUB_HOMEPAGE -> PARAM_PRODUCT_SUB_HOMEPAGE
            TravelType.ALL -> PARAM_PRODUCT_ALL
            TravelType.FLIGHT_VIDEO_BANNER -> PARAM_PRODUCT_FLIGHT_VIDEO_BANNER
            TravelType.HOTEL_VIDEO_BANNER -> PARAM_PRODUCT_HOTEL_VIDEO_BANNER
            else -> PARAM_PRODUCT_ALL
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

    companion object {
        const val PARAM_BANNER_PRODUCT_KEY = "product"

        const val PARAM_PRODUCT_FLIGHT = "FLIGHT"
        const val PARAM_PRODUCT_HOTEL = "HOTEL"
        const val PARAM_PRODUCT_SUB_HOMEPAGE = "SUBHOMEPAGE"
        const val PARAM_PRODUCT_FLIGHT_VIDEO_BANNER = "FLIGHTPROMOTIONAL"
        const val PARAM_PRODUCT_HOTEL_VIDEO_BANNER = "HOTELPROMOTIONAL"
        const val PARAM_PRODUCT_ALL = "ALL"

    }

}