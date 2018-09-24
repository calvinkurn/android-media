package com.tokopedia.flight.searchV2.data.repository

import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse
import com.tokopedia.flight.search.view.model.FlightSearchViewModel
import com.tokopedia.flight.searchV2.NetworkBoundResourceObservable
import com.tokopedia.flight.searchV2.data.api.combined.FlightSearchCombinedDataApiSource
import com.tokopedia.flight.searchV2.data.api.combined.response.FlightSearchCombinedResponse
import com.tokopedia.flight.searchV2.data.db.FlightSearchCombinedDataDbSource
import rx.Observable
import java.util.*

/**
 * Created by Rizky on 20/09/18.
 */
class FlightSearchRepository(val flightSearchCombinedDataApiSource: FlightSearchCombinedDataApiSource,
                             val flightSearchCombinedDataDbSource: FlightSearchCombinedDataDbSource) {

//    fun getSearchCombined(params: HashMap<String, Any>): Observable<List<FlightSearchViewModel>>? {
//        return object : NetworkBoundResourceObservable<List<FlightSearchViewModel>,
//                FlightDataResponse<List<FlightSearchCombinedResponse>>>() {
//            override fun loadFromDb(): Observable<List<FlightSearchViewModel>> {
//                return flightSearchCombinedDataDbSource.getSearchCombined()
//            }
//
//            override fun shouldFetch(data: List<FlightSearchViewModel>?) = data == null
//
//            override fun createCall(): Observable<FlightDataResponse<List<FlightSearchCombinedResponse>>> =
//                    flightSearchCombinedDataApiSource.getData(params)
//
//            override fun mapResponse(it: FlightDataResponse<List<FlightSearchCombinedResponse>>):
//                    List<FlightSearchViewModel> {
//
//            }
//
//            override fun saveCallResult(item: List<FlightSearchViewModel>) {
//
//            }
//        }.asObservable()
//    }

}