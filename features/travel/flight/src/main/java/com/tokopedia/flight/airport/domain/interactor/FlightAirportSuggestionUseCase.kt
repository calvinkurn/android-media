package com.tokopedia.flight.airport.domain.interactor

import android.text.TextUtils
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.airportv2.data.FlightAirportQuery
import com.tokopedia.flight.airportv2.data.source.entity.ResponseFlightAirportSuggesstion
import com.tokopedia.flight.airportv2.domain.FlightAirportMapper
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
class FlightAirportSuggestionUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase,
                                                         val mapper: FlightAirportMapper)
    : UseCase<@JvmSuppressWildcards List<Visitable<*>>>() {

    override fun createObservable(requestParams: RequestParams): Observable<List<Visitable<*>>> {
        return Observable.just(requestParams)
                .flatMap(Func1<RequestParams, Observable<GraphqlResponse>> {
                    val query = FlightAirportQuery.QUERY_AIRPORT_SUGGESTION
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

    fun createRequestParam(keyword: String): RequestParams {
        val requestParams = RequestParams.create()
        requestParams.putString(INPUT_VARIABLE, keyword)
        return requestParams
    }

    companion object {
        val INPUT_VARIABLE = "input"
    }
}
