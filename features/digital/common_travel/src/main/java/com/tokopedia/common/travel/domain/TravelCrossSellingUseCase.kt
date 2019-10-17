package com.tokopedia.common.travel.domain

import com.tokopedia.common.travel.data.entity.TravelCrossSelling
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

class TravelCrossSellingUseCase @Inject constructor(private val useCase: MultiRequestGraphqlUseCase,
                                                    private val graphqlUseCase: GraphqlUseCase) {

    suspend fun execute(query: String, orderId: String, orderCategory: String): Result<TravelCrossSelling> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.clearRequest()

        val params = mapOf(ORDER_ID to orderId, ORDER_CATEGORY to orderCategory)

        try {
            val graphqlRequest = GraphqlRequest(query, TravelCrossSelling.Response::class.java, params)
            useCase.addRequest(graphqlRequest)

//            val travelCrossSelling = useCase.executeOnBackground().getSuccessData<TravelCrossSelling.Response>().response

            var list = listOf<TravelCrossSelling.Item>().toMutableList()
            for (i in 1..5) {
                list.add(TravelCrossSelling.Item(product = "FLIGHT", title = "Tambah tiket pesawat", content = "Balik ke Jakarta", prefix = "",
                        uri = "tokopedia://pesawat/search?dest=DPS_Denpasar_CGK_Jakarta_2019-11-04&a=1&c=0&i=0&s=1&auto_search=1",
                        uriWeb = "https://www.tokopedia.com/flight/search/?term=DPS-CGK-20191104&amp;a=1&amp;c=0&amp;i=0&amp;k=1",
                        imageUrl = "https://ecs7.tokopedia.net/img/attachment/2019/7/25/8966428/8966428_9eeb33f3-4c63-40b7-83d6-26dc1c4c4100.png",
                        value = ""))
            }
//            return Success(travelCrossSelling)
            return Success(TravelCrossSelling(items = list, meta = TravelCrossSelling.Meta(title = "Lengkapi Perjalananmu")))
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }

    fun executeRx(query: String, requestParams: RequestParams?, subscriber: Subscriber<GraphqlResponse>?) {
        requestParams?.let {
            val graphqlRequest = GraphqlRequest(query, TravelCrossSelling.Response::class.java, it.parameters)
            graphqlUseCase.clearRequest()
            graphqlUseCase.addRequest(graphqlRequest)
            graphqlUseCase.execute(requestParams, subscriber)
        }
    }

    fun createRequestParams(orderId: String, orderCategory: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(ORDER_ID, orderId)
        requestParams.putString(ORDER_CATEGORY, orderCategory)
        return requestParams
    }

    companion object {
        const val PARAM_FLIGHT_PRODUCT = "FLIGHTS"
        const val PARAM_HOTEL_PRODUCT = "HOTELS"

        const val ORDER_ID = "orderId"
        const val ORDER_CATEGORY = "orderCategory"
    }
}