package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.db.SimpleSQLiteQuery
import android.text.TextUtils
import android.util.Log
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
            val sqlQuery = "SELECT * FROM FlightJourneyTable WHERE"
            val sqlStringBuilder = StringBuilder()
            sqlStringBuilder.append(sqlQuery)

            if (!TextUtils.isEmpty(filterModel.journeyId)) {
                sqlStringBuilder.append("id = " + "`" + filterModel.journeyId + "`" + " & ")
            }
            sqlStringBuilder.append("durationMinute < " + filterModel.durationMax + " & durationMinute > " +
                    filterModel.durationMin + " & ")
            sqlStringBuilder.append("sortPrice < " + filterModel.priceMax + " & sortPrice > " +
                    filterModel.priceMin + " & ")

            sqlStringBuilder.append("isSpecialPrice = " + "`" + filterModel.isSpecialPrice.toString() + "`" + " & ")
            sqlStringBuilder.append("isBestPairing = "  + "`" + filterModel.isBestPairing.toString() + "`" + " & ")
            sqlStringBuilder.append("isReturn = "       + "`" + isReturn.toString() + "`")

            val simpleSQLiteQuery = SimpleSQLiteQuery(sqlStringBuilder.toString())

            Log.d("Flight search filter query: ", simpleSQLiteQuery.sql.toString())

            it.onNext(flightJourneyDao.findFilteredJourneys(simpleSQLiteQuery))
        }
    }

}