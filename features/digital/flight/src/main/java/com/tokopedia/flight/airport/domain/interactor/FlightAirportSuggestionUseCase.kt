package com.tokopedia.flight.airport.domain.interactor

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.data.source.cloud.model.ResponseFlightAirportSuggesstion
import com.tokopedia.flight.airport.domain.FlightAirportMapper
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 06/03/19.
 */
class FlightAirportSuggestionUseCase @Inject constructor(@ApplicationContext val context: Context,
                                                         val graphqlUseCase: GraphqlUseCase,
                                                         val mapper: FlightAirportMapper)
    : UseCase<@JvmSuppressWildcards List<Visitable<*>>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<Visitable<*>>> {
        return Observable.just(requestParams)
                .flatMap(Func1<RequestParams, Observable<GraphqlResponse>> {
                    val query = GraphqlHelper.loadRawString(context.resources, R.raw.flight_airport_suggestion)
                    val variable = requestParams.parameters
                    if (!TextUtils.isEmpty(query)) {
                        graphqlUseCase.clearRequest()
                        graphqlUseCase.addRequest(GraphqlRequest(query, ResponseFlightAirportSuggesstion::class.java, variable))
                        return@Func1 graphqlUseCase.createObservable(null)
                    }
                    return@Func1 Observable.error(Exception("Query variable are empty"))
                })
                .map(Func1<GraphqlResponse, ResponseFlightAirportSuggesstion> {
                    it.getData(ResponseFlightAirportSuggesstion::class.java)
                })
                .map { it.flightSuggestion.flightSuggestionList }
                .map { mapper.groupingSuggestion(it) }
                .map { mapper.transformToVisitable(it) }
    }

    fun createRequestParam(keyword : String) : RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(INPUT_VARIABLE, keyword)
        return requestParams
    }

    companion object {
        val INPUT_VARIABLE = "input"
    }
}
