package com.tokopedia.common.travel.ticker.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.ticker.data.response.TravelTickerAttribute
import com.tokopedia.common.travel.ticker.data.response.TravelTickerEntity
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * @author by furqan on 19/02/19
 */
class TravelTickerUseCase @Inject
constructor(@param:ApplicationContext private val context: Context, private val graphqlUseCase: GraphqlUseCase) : UseCase<TravelTickerViewModel>() {

    override fun createObservable(requestParams: RequestParams): Observable<TravelTickerViewModel> {
        return Observable.just(requestParams)
                .flatMap(object : Func1<RequestParams, Observable<GraphqlResponse>> {
                    override fun call(reqParams: RequestParams?): Observable<GraphqlResponse> {
                        val params = HashMap<String, Any>()
                        if (reqParams != null) {
                            params[PARAM_DID] = ANDROID_DEVICE_ID
                            params[PARAM_INSTANCE_NAME] = reqParams.getString(PARAM_INSTANCE_NAME, "")
                            params[PARAM_PAGE] = reqParams.getString(PARAM_PAGE, "")
                        }

                        val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_travel_ticker)

                        if (query.isNotEmpty()) {
                            val request = GraphqlRequest(query, TravelTickerEntity::class.java, params, TRAVEL_TICKER_OPERATION_NAME)
                            graphqlUseCase.clearRequest()
                            graphqlUseCase.addRequest(request)
                            return graphqlUseCase.createObservable(null)
                        }

                        return Observable.error<GraphqlResponse>(Exception("Query and/or variable are empty."))
                    }
                })
                .map<TravelTickerEntity> { graphqlResponse -> graphqlResponse.getData(TravelTickerEntity::class.java) }
                .map { travelTickerEntity -> travelTickerEntity.travelTicker }
                .flatMap { travelTickerAttribute ->
                    Observable.just(
                            mapToTravelTickerViewModel(travelTickerAttribute))
                }
    }

    private fun mapToTravelTickerViewModel(travelTickerAttribute: TravelTickerAttribute): TravelTickerViewModel {
        return TravelTickerViewModel(
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

    fun createRequestParams(instanceName: String, tickerPage: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(PARAM_INSTANCE_NAME, instanceName)
        requestParams.putString(PARAM_PAGE, tickerPage)
        return requestParams
    }

    companion object {

        private val PARAM_INSTANCE_NAME = "instanceName"
        private val PARAM_PAGE = "tickerPage"
        private val PARAM_DID = "did"
        private val TRAVEL_TICKER_OPERATION_NAME = "travelTicker"
        private val ANDROID_DEVICE_ID = "5"
    }
}