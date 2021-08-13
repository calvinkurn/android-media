package com.tokopedia.common.travel.ticker.domain

import com.tokopedia.common.travel.data.TravelTickerGQLQuery
import com.tokopedia.common.travel.ticker.data.TravelTickerRequest
import com.tokopedia.common.travel.ticker.data.response.TravelTickerAttribute
import com.tokopedia.common.travel.ticker.data.response.TravelTickerEntity
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
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
 * @author by furqan on 31/03/2020
 */
class TravelTickerCoroutineUseCase @Inject constructor(
        private val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(instanceName: String = "", pageName: String = ""): Result<TravelTickerModel> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.clearRequest()

        val params = mapOf(
                PARAM_TICKER_REQUEST to TravelTickerRequest(pageName, instanceName, ANDROID_DEVICE_ID)
        )
        return try {
            val query = TravelTickerGQLQuery.TRAVEL_TICKER
            val graphqlRequest = GraphqlRequest(query, TravelTickerEntity::class.java, params)
            useCase.addRequest(graphqlRequest)

            val tickerData = useCase.executeOnBackground().getSuccessData<TravelTickerEntity>().travelTicker
            Success(mapToTravelTickerViewModel(tickerData))
        } catch (throwable: Throwable) {
            Fail(throwable)
        }

    }

    private fun mapToTravelTickerViewModel(travelTickerAttribute: TravelTickerAttribute): TravelTickerModel {
        return TravelTickerModel(
                travelTickerAttribute.title,
                travelTickerAttribute.message,
                travelTickerAttribute.url,
                travelTickerAttribute.type,
                travelTickerAttribute.status,
                travelTickerAttribute.endTime,
                travelTickerAttribute.startTime,
                travelTickerAttribute.instances,
                travelTickerAttribute.page,
                travelTickerAttribute.isPeriod)
    }

    companion object {
        private const val PARAM_TICKER_REQUEST = "tickerRequest"
        private const val ANDROID_DEVICE_ID = 5
    }
}