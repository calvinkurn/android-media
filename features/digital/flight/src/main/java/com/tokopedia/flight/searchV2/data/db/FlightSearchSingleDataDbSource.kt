package com.tokopedia.flight.searchV2.data.db

import rx.Observable

/**
 * Created by Rizky on 24/09/18.
 */
class FlightSearchSingleDataDbSource(val journeyDao: JourneyDao,
                                     val routeDao: RouteDao) {

    fun getSearchSingle(): Observable<List<Journey>> {
        val journeys = arrayListOf<Journey>()
        return Observable.just(journeys)
    }

    fun insert(item: List<Journey>) {

    }

}