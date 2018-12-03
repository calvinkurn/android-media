package com.tokopedia.flight.searchV2.data.db

import android.arch.persistence.db.SimpleSQLiteQuery
import android.util.Log
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.airline.data.db.FlightAirlineDataListDBSource
import com.tokopedia.flight_dbflow.FlightAirlineDB
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource
import com.tokopedia.flight_dbflow.FlightAirportDB
import com.tokopedia.flight.search.view.model.filter.DepartureTimeEnum
import com.tokopedia.flight.search.view.model.filter.RefundableEnum
import com.tokopedia.flight.search.view.model.filter.TransitEnum
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel
import rx.Observable
import rx.functions.Func2
import javax.inject.Inject

/**
 * Created by Rizky on 24/09/18.
 */
open class FlightSearchSingleDataDbSource @Inject constructor(
        private val flightJourneyDao: FlightJourneyDao,
        private val flightRouteDao: FlightRouteDao,
        private val flightAirportDataListDBSource: FlightAirportDataListDBSource,
        private val flightAirlineDataListDBSource: FlightAirlineDataListDBSource) {

    open fun insertList(journeyAndRoutesList: List<JourneyAndRoutes>) {
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

    fun getFilteredJourneys(filterModel: FlightFilterModel, @TravelSortOption flightSortOption: Int):
            Observable<List<JourneyAndRoutes>> {
        return Observable.create<List<JourneyAndRoutes>> {
            val sqlQuery = if (filterModel.airlineList != null && !filterModel.airlineList.isEmpty()) {
                "SELECT DISTINCT FlightJourneyTable.* FROM FlightJourneyTable LEFT JOIN FlightRouteTable ON " +
                        "FlightJourneyTable.id = FlightRouteTable.journeyId WHERE "
            } else {
                "SELECT * FROM FlightJourneyTable WHERE "
            }
            val query = buildQuery(sqlQuery, filterModel, flightSortOption)
            it.onNext(flightJourneyDao.findFilteredJourneys(query))
        }.flatMap { journeyAndRoutesList ->
            Observable.from(journeyAndRoutesList).flatMap { journeyAndRoutes ->
                populateCompactJourneyAndRoutes(journeyAndRoutes)
            }.toList()
        }
    }

    private fun populateCompactJourneyAndRoutes(journeyAndRoutes: JourneyAndRoutes): Observable<JourneyAndRoutes> {
        return Observable.from(journeyAndRoutes.routes)
                .flatMap { route ->
                    getAirlineById(route.airline).zipWith(getAirports(route.departureAirport, route.arrivalAirport)) {
                        airline: FlightAirlineDB, airport: Pair<FlightAirportDB, FlightAirportDB> ->
                        Pair(airline, airport)
                    }
                }
                .toList()
                .zipWith(getAirports(journeyAndRoutes.flightJourneyTable.departureAirport, journeyAndRoutes.flightJourneyTable.arrivalAirport)) {
                    routesAirlinesAndAirports: List<Pair<FlightAirlineDB, Pair<FlightAirportDB, FlightAirportDB>>>,
                    journeyAirports: Pair<FlightAirportDB, FlightAirportDB> ->
                    val journeyAirlines = arrayListOf<FlightAirlineDB>()
                    for (routeAirline in routesAirlinesAndAirports) {
                        if (!journeyAirlines.contains(routeAirline.first)) {
                            journeyAirlines.add(routeAirline.first)
                        }
                    }
                    journeyAndRoutes.flightJourneyTable = createJourneyWithAirportAndAirline(
                            journeyAndRoutes.flightJourneyTable, journeyAirports, journeyAirlines)
                    journeyAndRoutes
                }
    }

    fun getSearchCount(filterModel: FlightFilterModel): Observable<Int> {
        return Observable.create {
            val sqlQuery = if (filterModel.airlineList != null && !filterModel.airlineList.isEmpty()) {
                "SELECT COUNT(DISTINCT FlightJourneyTable.id) FROM FlightJourneyTable LEFT JOIN FlightRouteTable ON " +
                        "FlightJourneyTable.id = FlightRouteTable.journeyId WHERE "
            } else {
                "SELECT count(*) FROM FlightJourneyTable WHERE "
            }
            val query = buildQuery(sqlQuery, filterModel, TravelSortOption.CHEAPEST)
            it.onNext(flightJourneyDao.getSearchCount(query))
        }
    }

    private fun buildQuery(sqlQuery: String, filterModel: FlightFilterModel, flightSortOption: Int) : SimpleSQLiteQuery {
        val sqlStringBuilder = StringBuilder()
        sqlStringBuilder.append(sqlQuery)

        sqlStringBuilder.append("FlightJourneyTable.durationMinute BETWEEN ${filterModel.durationMin} AND ${filterModel.durationMax} AND ")
        sqlStringBuilder.append("FlightJourneyTable.sortPriceNumeric BETWEEN ${filterModel.priceMin} AND ${filterModel.priceMax} AND ")

        val isSpecialPrice = if (filterModel.isSpecialPrice) 1 else 0
        val isBestPairing = if (filterModel.isBestPairing) 1 else 0
        val isReturnInt = if (filterModel.isReturn) 1 else 0

        sqlStringBuilder.append("${getAirlineCondition(filterModel.airlineList)}")
        sqlStringBuilder.append("${getRefundableCondition(filterModel.refundableTypeList)}")
        sqlStringBuilder.append("${getTransitCondition(filterModel.transitTypeList)}")
        sqlStringBuilder.append("${getDepartureTimeCondition(filterModel.departureTimeList)}")

        if (filterModel.isSpecialPrice) {
            sqlStringBuilder.append("FlightJourneyTable.isSpecialPrice = $isSpecialPrice AND ")
        }
        if (filterModel.isReturn) {
            sqlStringBuilder.append("FlightJourneyTable.isBestPairing = $isBestPairing AND ")
        }
        sqlStringBuilder.append("FlightJourneyTable.isReturn = $isReturnInt")

        sqlStringBuilder.append(getOrderBy(flightSortOption))

        val simpleSQLiteQuery = SimpleSQLiteQuery(sqlStringBuilder.toString())

        Log.d("FlightSearchCount: ", simpleSQLiteQuery.sql.toString())

        return simpleSQLiteQuery
    }

    fun getJourneyById(onwardJourneyId: String?): Observable<JourneyAndRoutes> {
        return Observable.create<JourneyAndRoutes> {
            it.onNext(flightJourneyDao.findJourneyById(onwardJourneyId))
        }.flatMap { journeyAndRoutes ->
            populateCompactJourneyAndRoutes(journeyAndRoutes)
        }
    }

    private fun getAirlineCondition(airlines: List<String>?): String? {
        if (airlines == null || airlines.isEmpty()) {
            return ""
        }
        val stringBuilder = StringBuilder()
        stringBuilder.append("(")
        for (i in 0 until airlines.size) {
            val airline = airlines[i]
            stringBuilder.append("FlightRouteTable.airline = '$airline' ")
            if (i < airlines.size-1) {
                stringBuilder.append("OR ")
            }
        }
        stringBuilder.append(") AND ")
        return stringBuilder.toString()
    }

    private fun getRefundableCondition(refundableEnumList: List<RefundableEnum>?): String? {
        if (refundableEnumList == null || refundableEnumList.isEmpty()) {
            return ""
        }
        val stringBuilder = StringBuilder()
        stringBuilder.append("(")
        for (i in 0 until refundableEnumList.size) {
            val refundableEnum = refundableEnumList[i]
            stringBuilder.append("FlightJourneyTable.isRefundable = ${refundableEnum.id} ")
            if (i < refundableEnumList.size-1) {
                stringBuilder.append("OR ")
            }
        }
        stringBuilder.append(") AND ")
        return stringBuilder.toString()
    }

    private fun getTransitCondition(transitList: List<TransitEnum>?): String? {
        if (transitList == null || transitList.isEmpty()) {
            return ""
        }
        val stringBuilder = StringBuilder()
        stringBuilder.append("(")
        for (i in 0 until transitList.size) {
            val transitEnum = transitList[i]
            stringBuilder.append(
                    when (transitEnum) {
                        TransitEnum.DIRECT -> "FlightJourneyTable.totalTransit = 0 "
                        TransitEnum.ONE -> "FlightJourneyTable.totalTransit = 1 "
                        TransitEnum.TWO -> "FlightJourneyTable.totalTransit = 2 "
                        TransitEnum.THREE_OR_MORE -> "FlightJourneyTable.totalTransit > 3 "
                    }
            )
            if (i < transitList.size-1) {
                stringBuilder.append("OR ")
            }
        }
        stringBuilder.append(") AND ")
        return stringBuilder.toString()
    }

    private fun getDepartureTimeCondition(departureTimeEnumList: List<DepartureTimeEnum>?): String? {
        if (departureTimeEnumList == null || departureTimeEnumList.isEmpty()) {
            return ""
        }
        val stringBuilder = StringBuilder()
        stringBuilder.append("(")
        for (i in 0 until departureTimeEnumList.size) {
            val departureTimeEnum = departureTimeEnumList[i]
            stringBuilder.append(
                    when (departureTimeEnum) {
                        DepartureTimeEnum._00 -> "FlightJourneyTable.departureTimeInt BETWEEN 0 AND 599 "
                        DepartureTimeEnum._06 -> "FlightJourneyTable.departureTimeInt BETWEEN 600 AND 1200 "
                        DepartureTimeEnum._12 -> "FlightJourneyTable.departureTimeInt BETWEEN 1200 AND 1800 "
                        DepartureTimeEnum._18 -> "FlightJourneyTable.departureTimeInt BETWEEN 1800 AND 2400 "
                    }
            )
            if (i < departureTimeEnumList.size-1) {
                stringBuilder.append("OR ")
            }
        }
        stringBuilder.append(") AND ")
        return stringBuilder.toString()
    }

    private fun getOrderBy(@TravelSortOption flightSortOption: Int): String {
        return when (flightSortOption) {
            FlightSortOption.CHEAPEST -> " ORDER BY FlightJourneyTable.sortPriceNumeric ASC"
            FlightSortOption.EARLIEST_ARRIVAL -> " ORDER BY FlightJourneyTable.arrivalTimeInt ASC"
            FlightSortOption.EARLIEST_DEPARTURE -> " ORDER BY FlightJourneyTable.departureTimeInt ASC"
            FlightSortOption.LATEST_ARRIVAL -> " ORDER BY FlightJourneyTable.arrivalTimeInt DESC"
            FlightSortOption.LATEST_DEPARTURE -> " ORDER BY FlightJourneyTable.departureTimeInt DESC"
            FlightSortOption.SHORTEST_DURATION -> " ORDER BY FlightJourneyTable.durationMinute ASC"
            FlightSortOption.LONGEST_DURATION -> " ORDER BY FlightJourneyTable.durationMinute DESC"
            FlightSortOption.MOST_EXPENSIVE -> " ORDER BY FlightJourneyTable.sortPriceNumeric DESC"
            FlightSortOption.NO_PREFERENCE -> ""
            else -> ""
        }
    }

    fun deleteAllFlightSearchData() {
        flightJourneyDao.deleteTable()
        flightRouteDao.deleteTable()
    }

    fun deleteSearchReturnData(journeyAndRoutesList: List<JourneyAndRoutes>) {
        for (journeyAndRoutes in journeyAndRoutesList) {
            flightRouteDao.deleteByJourneyId(journeyAndRoutes.flightJourneyTable.id)
        }
        flightJourneyDao.deleteFlightSearchReturnData()
    }

    private fun getAirports(departureAirport: String, arrivalAirport: String) :
            Observable<Pair<FlightAirportDB, FlightAirportDB>> {
        return Observable.zip(
                flightAirportDataListDBSource.getAirport(departureAirport),
                flightAirportDataListDBSource.getAirport(arrivalAirport),
                Func2<FlightAirportDB, FlightAirportDB, Pair<FlightAirportDB, FlightAirportDB>>
                { t1, t2 ->
                    return@Func2 Pair(t1, t2)
                }
        )
    }

    private fun getAirlineById(airlineId: String): Observable<FlightAirlineDB> {
        return flightAirlineDataListDBSource.getAirline(airlineId)
    }

    private fun createJourneyWithAirportAndAirline(journey: FlightJourneyTable,
                                                   pairOfAirport: Pair<FlightAirportDB, FlightAirportDB>,
                                                   airlines: List<FlightAirlineDB>): FlightJourneyTable {
        val (departureAirport, arrivalAirport) = pairOfAirport
        journey.departureAirportName = departureAirport.airportName
        journey.departureAirportCity = departureAirport.cityName
        journey.arrivalAirportName = arrivalAirport.airportName
        journey.arrivalAirportCity = arrivalAirport.cityName
        journey.flightAirlineDBS = airlines
        return journey
    }

}