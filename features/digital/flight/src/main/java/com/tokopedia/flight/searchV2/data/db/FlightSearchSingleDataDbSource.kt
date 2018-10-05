package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.db.SimpleSQLiteQuery
import com.tokopedia.flight.search.constant.FlightSortOption
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 24/09/18.
 */
class FlightSearchSingleDataDbSource @Inject constructor(
        private val flightJourneyDao: FlightJourneyDao,
        private val flightRouteDao: FlightRouteDao) {

    fun findAllJourneys(): Observable<List<JourneyAndRoutesJava>> {
        return Observable.create {
            it.onNext(flightJourneyDao.findAllJourneys())
        }
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

    fun getFilteredJourneys(isReturn: Boolean, sortOption: FlightSortOption, filterModel: FlightFilterModel):
            Observable<List<JourneyAndRoutesJava>> {
        return Observable.create {
            it.onNext(flightJourneyDao.findFilteredJourneys(isReturn, filterModel.journeyId,
                    filterModel.isBestPairing))
        }
    }

    fun getFilteredJourneys2(isReturn: Boolean, sortOption: FlightSortOption, filterModel: FlightFilterModel):
            Observable<List<JourneyAndRoutesJava>> {
        return Observable.create {
            val sqlQuery = "SELECT * FROM FlightJourneyTable WHERE"
            val sqlStringBuilder = StringBuilder()
            sqlStringBuilder.append(sqlQuery)
            if (filterModel.isSpecialPrice) {
                sqlStringBuilder.append()
            }

            val simpleSQLiteQuery = SimpleSQLiteQuery(sqlStringBuilder.toString())

            it.onNext(flightJourneyDao.findFilteredJourneys(simpleSQLiteQuery))
        }
    }

}