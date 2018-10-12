package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.db.SimpleSQLiteQuery
import android.util.Log
import com.tokopedia.flight.searchV2.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.searchV2.presentation.model.filter.RefundableEnum
import com.tokopedia.flight.searchV2.presentation.model.filter.TransitEnum
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 24/09/18.
 */
open class FlightSearchSingleDataDbSource @Inject constructor(
        private val flightJourneyDao: FlightJourneyDao,
        private val flightRouteDao: FlightRouteDao) {

    fun insertList(journeyAndRoutesList: List<JourneyAndRoutes>) {
        for (journey in journeyAndRoutesList) {
            flightJourneyDao.insert(journey.flightJourneyTable)
            flightRouteDao.insert(journey.routes)
        }
    }

    open fun insert(journeyAndRoutes: JourneyAndRoutes) {
        flightJourneyDao.insert(journeyAndRoutes.flightJourneyTable)
        flightRouteDao.insert(journeyAndRoutes.routes)
    }

    open fun findAllJourneys(): Observable<List<JourneyAndRoutes>> {
        return Observable.create {
            it.onNext(flightJourneyDao.findAllJourneys())
        }
    }

    fun getFilteredJourneys(filterModel: FlightFilterModel): Observable<List<JourneyAndRoutes>> {
        return Observable.create {
            val sqlQuery = "SELECT * FROM FlightJourneyTable WHERE "
            val sqlStringBuilder = StringBuilder()
            sqlStringBuilder.append(sqlQuery)

            sqlStringBuilder.append("durationMinute BETWEEN ${filterModel.durationMin} AND ${filterModel.durationMax} AND ")
            sqlStringBuilder.append("sortPrice BETWEEN ${filterModel.priceMin} AND ${filterModel.priceMax} AND ")

            val isSpecialPrice = if (filterModel.isSpecialPrice) 1 else 0
            val isBestPairing = if (filterModel.isBestPairing) 1 else 0
            val isReturnInt = if (filterModel.isReturn) 1 else 0

            sqlStringBuilder.append("${getRefundableCondition(filterModel.refundableTypeList)}")
            sqlStringBuilder.append("${getTransitCondition(filterModel.transitTypeList)}")
            sqlStringBuilder.append("${getDepartureTimeCondition(filterModel.departureTimeList)}")

            sqlStringBuilder.append("isSpecialPrice = $isSpecialPrice AND ")
            sqlStringBuilder.append("isBestPairing = $isBestPairing AND ")
            sqlStringBuilder.append("isReturn = $isReturnInt")

//            sqlStringBuilder.append("ORDER BY ")

            val simpleSQLiteQuery = SimpleSQLiteQuery(sqlStringBuilder.toString())

            Log.d("Flight search filter query: ", simpleSQLiteQuery.sql.toString())

            it.onNext(flightJourneyDao.findFilteredJourneys(simpleSQLiteQuery))
        }
    }

    fun getJourneyById(onwardJourneyId: String?): Observable<JourneyAndRoutes> {
        return Observable.create {
            it.onNext(flightJourneyDao.findJourneyById(onwardJourneyId))
        }
    }

    private fun getRefundableCondition(refundableEnumList: List<RefundableEnum>?): String? {
        if (refundableEnumList == null || refundableEnumList.isEmpty()) {
            return ""
        }
        val stringBuilder = StringBuilder()
        for (i in 0 until refundableEnumList.size) {
            val refundableEnum = refundableEnumList[i]
            stringBuilder.append("isRefundable = ${refundableEnum.id} ")
            if (i < refundableEnumList.size-1) {
                stringBuilder.append("OR ")
            }
        }
        stringBuilder.append("AND ")
        return stringBuilder.toString()
    }

    private fun getTransitCondition(transitList: List<TransitEnum>?): String? {
        if (transitList == null || transitList.isEmpty()) {
            return ""
        }
        val stringBuilder = StringBuilder()
        for (i in 0 until transitList.size) {
            val transitEnum = transitList[i]
            stringBuilder.append(
                    when (transitEnum) {
                        TransitEnum.DIRECT -> "totalTransit = 0 "
                        TransitEnum.ONE -> "totalTransit = 1 "
                        TransitEnum.TWO -> "totalTransit = 2 "
                        TransitEnum.THREE_OR_MORE -> "totalTransit > 3 "
                    }
            )
            if (i < transitList.size-1) {
                stringBuilder.append("OR ")
            }
        }
        stringBuilder.append("AND ")
        return stringBuilder.toString()
    }

    private fun getDepartureTimeCondition(departureTimeEnumList: List<DepartureTimeEnum>?): String? {
        if (departureTimeEnumList == null || departureTimeEnumList.isEmpty()) {
            return ""
        }
        val stringBuilder = StringBuilder()
        for (i in 0 until departureTimeEnumList.size) {
            val departureTimeEnum = departureTimeEnumList[i]
            stringBuilder.append(
                    when (departureTimeEnum) {
                        DepartureTimeEnum._00 -> "departureTimeInt BETWEEN 0 AND 599 "
                        DepartureTimeEnum._06 -> "departureTimeInt BETWEEN 600 AND 1200 "
                        DepartureTimeEnum._12 -> "departureTimeInt BETWEEN 1200 AND 1800 "
                        DepartureTimeEnum._18 -> "departureTimeInt BETWEEN 1800 AND 2400 "
                    }
            )
            if (i < departureTimeEnumList.size-1) {
                stringBuilder.append("OR ")
            }
        }
        stringBuilder.append("AND ")
        return stringBuilder.toString()
    }

    fun deleteAllFlightSearchData() {
        flightJourneyDao.deleteTable()
        flightRouteDao.deleteTable()
    }

    fun deleteFlightSearchReturnData() {
        flightJourneyDao.deleteFlightSearchReturnData()
    }

    fun deleteRouteByJourneyId(journeyId: String) {
        flightRouteDao.deleteByJourneyId(journeyId)
    }

//    private fun getOrderBy(flightSortOption: FlightSortOption): String {
//        when (flightSortOption) {
//            FlightSortOption.CHEAPEST -> "ORDER BY sortPrice"
//            FlightSortOption.EARLIEST_ARRIVAL -> "ORDER BY sortPrice"
//        }
//    }

//    fun insert(journey: JourneyAndRoutes?, combo: FlightComboTable?) {
//        val flightJourneyComboJoinTable = FlightJourneyComboJoinTable(journey?.flightJourneyTable?.id,
//                combo?.returnJourneyId)
//        flightJourneyComboJoinDao.insert(flightJourneyComboJoinTable)
//    }

}