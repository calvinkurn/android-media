package com.tokopedia.flight.searchV2.data.db

import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 24/09/18.
 */
class FlightSearchSingleDataDbSource @Inject constructor(
        private val flightJourneyDao: FlightJourneyDao,
        private val flightRouteDao: FlightRouteDao) {

    fun getSearchSingle(): Observable<List<FlightJourneyTable>> {
        val journeys = arrayListOf<FlightJourneyTable>()
        return Observable.just(journeys)
    }

    fun insert(items: List<FlightJourneyTable>) {
        flightJourneyDao.insert(items)
        for (journey in items) {
            flightRouteDao.insert(journey.routes)
        }
    }

    fun insert(item: FlightJourneyTable) {
        flightJourneyDao.insert(item)
    }

}