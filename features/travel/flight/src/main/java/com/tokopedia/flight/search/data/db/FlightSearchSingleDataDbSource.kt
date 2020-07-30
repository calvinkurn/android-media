package com.tokopedia.flight.search.data.db

import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.filter.presentation.FlightFilterFacilityEnum
import com.tokopedia.flight.search.data.api.single.response.Amenity
import com.tokopedia.flight.search.presentation.model.FlightAirlineModel
import com.tokopedia.flight.search.presentation.model.FlightAirportModel
import com.tokopedia.flight.search.presentation.model.filter.DepartureTimeEnum
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum
import com.tokopedia.flight.search.presentation.model.filter.TransitEnum
import rx.Observable
import javax.inject.Inject

/**
 * Created by Rizky on 24/09/18.
 */
open class FlightSearchSingleDataDbSource @Inject constructor(
        private val flightJourneyDao: FlightJourneyDao,
        private val flightRouteDao: FlightRouteDao) {

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
            val sqlQuery = "SELECT DISTINCT FlightJourneyTable.* FROM FlightJourneyTable LEFT JOIN FlightRouteTable ON " +
                    "FlightJourneyTable.id = FlightRouteTable.journeyId WHERE "

            val query = buildQuery(sqlQuery, filterModel, flightSortOption)
            val filteredJourney = flightJourneyDao.findFilteredJourneys(query)
            val facilityFilterList = getFacilityFilter(filterModel.facilityList)

            it.onNext(getJourneyFilteredByFacility(filteredJourney, facilityFilterList))
        }.flatMap { journeyAndRoutesList ->
            Observable.from(journeyAndRoutesList).flatMap { journeyAndRoutes ->
                populateCompactJourneyAndRoutes(journeyAndRoutes)
            }.toList()
        }
    }

    fun getFilteredJourneysCoroutine(filterModel: FlightFilterModel, @TravelSortOption flightSortOption: Int):
            List<JourneyAndRoutes> {
        val sqlQuery = "SELECT DISTINCT FlightJourneyTable.* FROM FlightJourneyTable LEFT JOIN FlightRouteTable ON " +
                "FlightJourneyTable.id = FlightRouteTable.journeyId WHERE "

        val query = buildQuery(sqlQuery, filterModel, flightSortOption)
        val filteredJourney = flightJourneyDao.findFilteredJourneys(query)
        val facilityFilterList = getFacilityFilter(filterModel.facilityList)

        return getJourneyFilteredByFacility(filteredJourney, facilityFilterList).map {
            populateCompactJourneyAndRoutesCoroutine(it)
        }.toList()
    }

    fun getFilteredJourneysStatisticCoroutine(filterModel: FlightFilterModel, @TravelSortOption flightSortOption: Int):
            List<JourneyAndRoutes> {
        val sqlQuery = "SELECT DISTINCT FlightJourneyTable.* FROM FlightJourneyTable LEFT JOIN FlightRouteTable ON " +
                "FlightJourneyTable.id = FlightRouteTable.journeyId WHERE "

        val query = buildQueryStatistic(sqlQuery, filterModel, flightSortOption)
        val filteredJourney = flightJourneyDao.findFilteredJourneys(query)

        return filteredJourney.map {
            populateCompactJourneyAndRoutesCoroutine(it)
        }.toList()
    }

    private fun populateCompactJourneyAndRoutes(journeyAndRoutes: JourneyAndRoutes): Observable<JourneyAndRoutes> {
        val journeyDepartureAirport = FlightAirportModel(journeyAndRoutes.flightJourneyTable.departureAirport,
                journeyAndRoutes.flightJourneyTable.departureAirportName, journeyAndRoutes.flightJourneyTable.departureAirportCity)
        val journeyArrivalAirport = FlightAirportModel(journeyAndRoutes.flightJourneyTable.arrivalAirport,
                journeyAndRoutes.flightJourneyTable.arrivalAirportName, journeyAndRoutes.flightJourneyTable.arrivalAirportCity)
        val pairOfJourneyAirport = Pair(journeyDepartureAirport, journeyArrivalAirport)

        return Observable.from(journeyAndRoutes.routes)
                .flatMap { route ->
                    val flightAirlineViewModel = FlightAirlineModel(route.airline, route.airlineName, route.airlineShortName, route.airlineLogo)
                    val departureAirport = FlightAirportModel(route.departureAirport, route.departureAirportName, route.departureAirportCity)
                    val arrivalAirport = FlightAirportModel(route.arrivalAirport, route.arrivalAirportName, route.arrivalAirportCity)
                    Observable.just(flightAirlineViewModel)
                            .map { Pair(flightAirlineViewModel, Pair(departureAirport, arrivalAirport)) }
                }
                .toList()
                .zipWith(Observable.just(pairOfJourneyAirport)) { routesAirlinesAndAirports: List<Pair<FlightAirlineModel, Pair<FlightAirportModel, FlightAirportModel>>>,
                                                                  journeyAirports: Pair<FlightAirportModel, FlightAirportModel> ->
                    val journeyAirlines = arrayListOf<FlightAirlineModel>()
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

    private fun populateCompactJourneyAndRoutesCoroutine(journeyAndRoutes: JourneyAndRoutes): JourneyAndRoutes {
        val journeyDepartureAirport = FlightAirportModel(journeyAndRoutes.flightJourneyTable.departureAirport,
                journeyAndRoutes.flightJourneyTable.departureAirportName, journeyAndRoutes.flightJourneyTable.departureAirportCity)
        val journeyArrivalAirport = FlightAirportModel(journeyAndRoutes.flightJourneyTable.arrivalAirport,
                journeyAndRoutes.flightJourneyTable.arrivalAirportName, journeyAndRoutes.flightJourneyTable.arrivalAirportCity)
        val pairOfJourneyAirport = Pair(journeyDepartureAirport, journeyArrivalAirport)

        val journeyAndRouteList = journeyAndRoutes.routes.map {
            val flightAirlineViewModel = FlightAirlineModel(it.airline, it.airlineName, it.airlineShortName, it.airlineLogo)
            val departureAirport = FlightAirportModel(it.departureAirport, it.departureAirportName, it.departureAirportCity)
            val arrivalAirport = FlightAirportModel(it.arrivalAirport, it.arrivalAirportName, it.arrivalAirportCity)

            Pair(flightAirlineViewModel, Pair(departureAirport, arrivalAirport))
        }.toList()

        val journeyAirlines = arrayListOf<FlightAirlineModel>()
        for (routeAirline in journeyAndRouteList) {
            if (!journeyAirlines.contains(routeAirline.first)) {
                journeyAirlines.add(routeAirline.first)
            }
        }

        journeyAndRoutes.flightJourneyTable = createJourneyWithAirportAndAirline(
                journeyAndRoutes.flightJourneyTable, pairOfJourneyAirport, journeyAirlines)

        return journeyAndRoutes
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

    fun getSearchCountCoroutine(filterModel: FlightFilterModel): Int {
        val sqlQuery = "SELECT DISTINCT FlightJourneyTable.* FROM FlightJourneyTable LEFT JOIN FlightRouteTable ON " +
                "FlightJourneyTable.id = FlightRouteTable.journeyId WHERE "

        val query = buildQuery(sqlQuery, filterModel, TravelSortOption.CHEAPEST)
        val filteredJourney = flightJourneyDao.findFilteredJourneys(query)
        val facilityFilter = getFacilityFilter(filterModel.facilityList)

        return getJourneyFilteredByFacility(filteredJourney, facilityFilter).size
    }

    private fun getFacilityFilter(facilityFilterList: List<FlightFilterFacilityEnum>?): Map<FlightFilterFacilityEnum, Boolean> {
        val map = mutableMapOf(
                Pair(FlightFilterFacilityEnum.BAGGAGE, false),
                Pair(FlightFilterFacilityEnum.MEAL, false)
        )

        if (facilityFilterList != null) {
            for (facility in facilityFilterList) {
                map[facility] = true
            }
        }

        return map
    }

    private fun getJourneyFilteredByFacility(filteredJourney: List<JourneyAndRoutes>,
                                             facilityFilter: Map<FlightFilterFacilityEnum, Boolean>)
            : List<JourneyAndRoutes> {
        return filteredJourney.filter {
            var shouldCount = false
            val isBaggageFiltered = facilityFilter[FlightFilterFacilityEnum.BAGGAGE] ?: false
            val isMealFiltered = facilityFilter[FlightFilterFacilityEnum.MEAL] ?: false
            val type = object : TypeToken<List<Amenity>>() {}.type

            for (route in it.routes) {
                val amenities = Gson().fromJson<List<Amenity>>(route.amenities, type)

                if (amenities.isNotEmpty()) {
                    if (isBaggageFiltered && isMealFiltered) {
                        shouldCount = ((amenities.size == 2)
                                && (amenities[0].icon == "baggage" || amenities[0].icon == "meal")
                                && (amenities[1].icon == "baggage" || amenities[1].icon == "meal"))
                    } else if (isBaggageFiltered) {
                        for (amenity in amenities) {
                            if (amenity.icon == "baggage") {
                                shouldCount = true
                                break
                            }
                        }
                    } else if (isMealFiltered) {
                        for (amenity in amenities) {
                            if (amenity.icon == "meal") {
                                shouldCount = true
                                break
                            }
                        }
                    } else {
                        shouldCount = true
                    }
                } else shouldCount = !(isBaggageFiltered || isMealFiltered)
            }

            shouldCount
        }.toList()
    }

    private fun buildQueryStatistic(sqlQuery: String, filterModel: FlightFilterModel, flightSortOption: Int): SimpleSQLiteQuery {
        val sqlStringBuilder = StringBuilder()
        sqlStringBuilder.append(sqlQuery)

        val isBestPairing = if (filterModel.isBestPairing) 1 else 0
        val isReturnInt = if (filterModel.isReturn) 1 else 0

        if (filterModel.isReturn) {
            sqlStringBuilder.append("FlightJourneyTable.isBestPairing = $isBestPairing AND ")
        }
        sqlStringBuilder.append("FlightJourneyTable.isReturn = $isReturnInt")

        sqlStringBuilder.append(getOrderBy(flightSortOption))

        return SimpleSQLiteQuery(sqlStringBuilder.toString())
    }

    private fun buildQuery(sqlQuery: String, filterModel: FlightFilterModel, flightSortOption: Int): SimpleSQLiteQuery {
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
        sqlStringBuilder.append("${getArrivalTimeCondition(filterModel.arrivalTimeList)}")

        if (filterModel.isSpecialPrice) {
            sqlStringBuilder.append("FlightJourneyTable.isSpecialPrice = $isSpecialPrice AND ")
        }
        if (filterModel.isReturn) {
            sqlStringBuilder.append("FlightJourneyTable.isBestPairing = $isBestPairing AND ")
        }
        sqlStringBuilder.append("FlightJourneyTable.isReturn = $isReturnInt")

        sqlStringBuilder.append(getOrderBy(flightSortOption))

        return SimpleSQLiteQuery(sqlStringBuilder.toString())
    }

    fun getJourneyById(onwardJourneyId: String?): Observable<JourneyAndRoutes> {
        return Observable.create<JourneyAndRoutes> {
            it.onNext(flightJourneyDao.findJourneyById(onwardJourneyId))
        }.flatMap { journeyAndRoutes ->
            populateCompactJourneyAndRoutes(journeyAndRoutes)
        }
    }

    fun getJourneyRouteById(onwardJourneyId: String?): JourneyAndRoutes? {
        return flightJourneyDao.findJourneyById(onwardJourneyId)
    }

    fun updateJourneyData(journeyTable: FlightJourneyTable) {
        flightJourneyDao.update(journeyTable)
    }

    fun updateJourneyDataList(journeyTableList: List<FlightJourneyTable>) {
        flightJourneyDao.update(journeyTableList)
    }

    private fun getAirlineCondition(airlines: List<String>?): String? {
        if (airlines == null || airlines.isEmpty()) {
            return ""
        }
        val stringBuilder = StringBuilder()
        stringBuilder.append("(")
        for (i in airlines.indices) {
            val airline = airlines[i]
            stringBuilder.append("FlightRouteTable.airline = '$airline' ")
            if (i < airlines.size - 1) {
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
        for (i in refundableEnumList.indices) {
            val refundableEnum = refundableEnumList[i]
            stringBuilder.append("FlightJourneyTable.isRefundable = ${refundableEnum.id} ")
            if (i < refundableEnumList.size - 1) {
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
        for (i in transitList.indices) {
            val transitEnum = transitList[i]
            stringBuilder.append(
                    when (transitEnum) {
                        TransitEnum.DIRECT -> "FlightJourneyTable.totalTransit = 0 "
                        TransitEnum.ONE -> "FlightJourneyTable.totalTransit = 1 "
                        TransitEnum.TWO -> "FlightJourneyTable.totalTransit > 1 "
                    }
            )
            if (i < transitList.size - 1) {
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
        for (i in departureTimeEnumList.indices) {
            val departureTimeEnum = departureTimeEnumList[i]
            stringBuilder.append(
                    when (departureTimeEnum) {
                        DepartureTimeEnum._00 -> "FlightJourneyTable.departureTimeInt BETWEEN 0 AND 599 "
                        DepartureTimeEnum._06 -> "FlightJourneyTable.departureTimeInt BETWEEN 600 AND 1200 "
                        DepartureTimeEnum._12 -> "FlightJourneyTable.departureTimeInt BETWEEN 1200 AND 1800 "
                        DepartureTimeEnum._18 -> "FlightJourneyTable.departureTimeInt BETWEEN 1800 AND 2400"
                    }
            )
            if (i < departureTimeEnumList.size - 1) {
                stringBuilder.append("OR ")
            }
        }
        stringBuilder.append(") AND ")
        return stringBuilder.toString()
    }

    private fun getArrivalTimeCondition(arrivalTimeEnumList: List<DepartureTimeEnum>?): String? {
        if (arrivalTimeEnumList == null || arrivalTimeEnumList.isEmpty()) {
            return ""
        }
        val stringBuilder = StringBuilder()
        stringBuilder.append("(")
        for (i in arrivalTimeEnumList.indices) {
            val arrivalTimeEnum = arrivalTimeEnumList[i]
            stringBuilder.append(
                    when (arrivalTimeEnum) {
                        DepartureTimeEnum._00 -> "FlightJourneyTable.arrivalTimeInt BETWEEN 0 AND 599 "
                        DepartureTimeEnum._06 -> "FlightJourneyTable.arrivalTimeInt BETWEEN 600 AND 1200 "
                        DepartureTimeEnum._12 -> "FlightJourneyTable.arrivalTimeInt BETWEEN 1200 AND 1800 "
                        DepartureTimeEnum._18 -> "FlightJourneyTable.arrivalTimeInt BETWEEN 1800 AND 2400"
                    }
            )
            if (i < arrivalTimeEnumList.size - 1) {
                stringBuilder.append("OR ")
            }
        }
        stringBuilder.append(") AND ")
        return stringBuilder.toString()
    }

    private fun getOrderBy(@TravelSortOption flightSortOption: Int): String {
        return when (flightSortOption) {
            TravelSortOption.CHEAPEST -> " ORDER BY FlightJourneyTable.sortPriceNumeric ASC, FlightJourneyTable.departureTimeInt ASC"
            TravelSortOption.EARLIEST_ARRIVAL -> " ORDER BY FlightJourneyTable.arrivalTimeInt ASC"
            TravelSortOption.EARLIEST_DEPARTURE -> " ORDER BY FlightJourneyTable.departureTimeInt ASC"
            TravelSortOption.LATEST_ARRIVAL -> " ORDER BY FlightJourneyTable.arrivalTimeInt DESC"
            TravelSortOption.LATEST_DEPARTURE -> " ORDER BY FlightJourneyTable.departureTimeInt DESC"
            TravelSortOption.SHORTEST_DURATION -> " ORDER BY FlightJourneyTable.durationMinute ASC"
            TravelSortOption.LONGEST_DURATION -> " ORDER BY FlightJourneyTable.durationMinute DESC"
            TravelSortOption.MOST_EXPENSIVE -> " ORDER BY FlightJourneyTable.sortPriceNumeric DESC, FlightJourneyTable.departureTimeInt ASC"
            TravelSortOption.NO_PREFERENCE -> ""
            else -> ""
        }
    }

    fun deleteAllFlightSearchData() {
        flightJourneyDao.deleteTable()
        flightRouteDao.deleteTable()
    }

    suspend fun deleteAllFlightSearchDataCoroutine() {
        flightJourneyDao.deleteTableCoroutine()
        flightRouteDao.deleteTableCoroutine()
    }

    fun deleteSearchReturnData(journeyAndRoutesList: List<JourneyAndRoutes>) {
        for (journeyAndRoutes in journeyAndRoutesList) {
            flightRouteDao.deleteByJourneyId(journeyAndRoutes.flightJourneyTable.id)
        }
        flightJourneyDao.deleteFlightSearchReturnData()
    }

    private fun createJourneyWithAirportAndAirline(journey: FlightJourneyTable,
                                                   pairOfAirport: Pair<FlightAirportModel, FlightAirportModel>,
                                                   airlines: List<FlightAirlineModel>): FlightJourneyTable {
        val (departureAirport, arrivalAirport) = pairOfAirport
        journey.departureAirportName = departureAirport.airportName
        journey.departureAirportCity = departureAirport.cityName
        journey.arrivalAirportName = arrivalAirport.airportName
        journey.arrivalAirportCity = arrivalAirport.cityName
        journey.flightAirlineDBS = airlines
        return journey
    }

}