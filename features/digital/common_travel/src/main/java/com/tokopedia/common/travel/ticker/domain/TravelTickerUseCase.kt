package com.tokopedia.common.travel.ticker.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.common.travel.ticker.data.response.TravelTickerAttribute
import com.tokopedia.common.travel.ticker.data.response.TravelTickerEntity
import com.tokopedia.common.travel.ticker.presentation.model.TravelTickerViewModel
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import java.lang.Exception
import javax.inject.Inject

/**
 * @author by furqan on 18/02/19
 */
class TravelTickerUseCase @Inject constructor(@ApplicationContext private val context: Context,
                                              private val graphqlUseCase: GraphqlUseCase) :
        UseCase<TravelTickerViewModel>() {

    private val PARAM_INSTANCE_NAME = "instanceName"
    private val PARAM_PAGE = "tickerPage"
    private val PARAM_DID = "did"
    private val TRAVEL_TICKER_OPERATION_NAME = "travelTicker"
    private val ANDROID_DEVICE_ID = "5"

    override fun createObservable(requestParams: RequestParams?): Observable<TravelTickerViewModel> {
        Observable.just(requestParams).flatMap {
            val params = HashMap<String, Any>()
            if (it != null) {
                params.put(PARAM_DID, ANDROID_DEVICE_ID)
                params.put(PARAM_INSTANCE_NAME, it.getString(PARAM_INSTANCE_NAME, ""))
                params.put(PARAM_PAGE, it.getString(PARAM_PAGE, ""))
            }

            val query = GraphqlHelper.loadRawString(context.resources, R.raw.query_travel_ticker)

            if (query.isNotEmpty()) {
                val request = GraphqlRequest(query, TravelTickerEntity::class.java,
                        params, TRAVEL_TICKER_OPERATION_NAME)
                graphqlUseCase.clearRequest()
                graphqlUseCase.addRequest(request)
                return graphqlUseCase.createObservable(null)
            }

            return Observable.error(Exception("Query and/or variable are empty."))
        }.map {
            (TravelTickerEntity) it.getData(TravelTickerEntity::class.java)
        }.flatMap {
            Observable.just(mapToTravelTickerViewModel(it.travelTicker))
        }
    }


    private fun mapToTravelTickerViewModel(travelTickerAttribute: TravelTickerAttribute): TravelTickerViewModel =
            TravelTickerViewModel(
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