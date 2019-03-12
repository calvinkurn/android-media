package com.tokopedia.flight.airport.data.source.cloud

import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi
import com.tokopedia.flight.common.di.scope.FlightScope
import rx.Observable
import java.util.*
import javax.inject.Inject

/**
 * Created by zulfikarrahman on 11/21/17.
 */

class FlightAirportDataListCloudSource @Inject
constructor(@param:FlightScope private val flightApi: FlightApi) : DataListCloudSource<FlightAirportCountry>() {

    override fun getData(params: HashMap<String, Any>): Observable<List<FlightAirportCountry>> {
        val paramsString = HashMap<String, String>()
        paramsString[KEYWORD] = params[KEYWORD].toString()
        return flightApi.getFlightAirportList(paramsString)
                .flatMap { dataResponseResponse -> Observable.just(dataResponseResponse.body().data) }
    }

    companion object {

        val KEYWORD = "keyword"
    }
}
