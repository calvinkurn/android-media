package com.tokopedia.flight.searchV2.data.db

import rx.Observable

/**
 * Created by Rizky on 24/09/18.
 */
class FlightSearchSingleDataDbSource(val journeyDao: JourneyDao,
                                     val routeDao: RouteDao) {

    fun getSearchSingle(): Observable<List<Journey>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insert(item: List<Journey>) {

    }

}