package com.tokopedia.common.travel.ticker.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.ticker.data.response.TravelTickerAttribute
import com.tokopedia.common.travel.ticker.data.response.TravelTickerEntity
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerModel
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

/**
 * @author by furqan on 31/03/2020
 */
class TravelTickerCoroutineUseCase @Inject constructor(
        @ApplicationContext val context: Context,
        private val useCase: MultiRequestGraphqlUseCase) {

    suspend fun execute(instanceName: String = "", pageName: String = ""): Result<TravelTickerModel> {
        useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        useCase.clearRequest()

        val params = mapOf(
                PARAM_DID to ANDROID_DEVICE_ID,
                PARAM_INSTANCE_NAME to instanceName,
                PARAM_PAGE to pageName
        )
        try {
            val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_travel_ticker)
            val graphqlRequest = GraphqlRequest(query, TravelTickerEntity::class.java, params)
            useCase.addRequest(graphqlRequest)

            val
        } catch (throwable: Throwable) {
            return Fail(throwable)
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
        private const val PARAM_INSTANCE_NAME = "instanceName"
        private const val PARAM_PAGE = "tickerPage"
        private const val PARAM_DID = "did"
        private const val ANDROID_DEVICE_ID = "5"
    }
}