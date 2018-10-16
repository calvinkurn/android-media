package com.tokopedia.flight.searchV2.data.repository

import android.text.TextUtils
import com.google.gson.Gson
import com.tokopedia.flight.airline.data.db.FlightAirlineDataListDBSource
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB
import com.tokopedia.flight.search.constant.FlightSortOption
import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.flight.search.data.cloud.model.response.Attributes
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData
import com.tokopedia.flight.search.data.cloud.model.response.Meta
import com.tokopedia.flight.search.data.cloud.model.response.Route
import com.tokopedia.flight.search.view.model.filter.RefundableEnum
import com.tokopedia.flight.searchV2.data.ComboAndMetaWrapper
import com.tokopedia.flight.searchV2.data.api.combined.FlightSearchCombinedDataApiSource
import com.tokopedia.flight.searchV2.data.db.*
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchCombinedApiRequestModel
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel
import rx.Observable
import rx.functions.Func2
import javax.inject.Inject

/**
 * Created by Rizky on 20/09/18.
 */
open class FlightSearchRepository @Inject constructor(
        private val flightSearchCombinedDataApiSource: FlightSearchCombinedDataApiSource,
        private val flightSearchDataCloudSource: FlightSearchDataCloudSource,
        private val flightSearchCombinedDataDbSource: FlightSearchCombinedDataDbSource,
        private val flightSearchSingleDataDbSource: FlightSearchSingleDataDbSource,
        private val flightAirportDataListDBSource: FlightAirportDataListDBSource,
        private val flightAirlineDataListDBSource: FlightAirlineDataListDBSource) {

    private fun generateJourneyAndRoutesObservable(journeyResponse: FlightSearchData, isReturnTrip: Boolean):
            Observable<JourneyAndRoutes> {
        return Observable.from(journeyResponse.attributes.routes)
                .flatMap { route ->
                    getAirlineById(route.airline)
                            .zipWith(getAirports(route.departureAirport, route.arrivalAirport)) {
                                airline: FlightAirlineDB, airport: Pair<FlightAirportDB, FlightAirportDB> ->
                                Pair(airline, airport)
                            }
                }
                .toList()
                .zipWith(getAirports(journeyResponse.attributes.departureAirport, journeyResponse.attributes.arrivalAirport)) {
                    routesAirlinesAndAirports: List<Pair<FlightAirlineDB, Pair<FlightAirportDB, FlightAirportDB>>>,
                    journeyAirports: Pair<FlightAirportDB, FlightAirportDB> ->
                    val journeyAirlines = arrayListOf<FlightAirlineDB>()
                    for (routeAirline in routesAirlinesAndAirports) {
                        if (!journeyAirlines.contains(routeAirline.first)) {
                            journeyAirlines.add(routeAirline.first)
                        }
                    }
                    createCompleteJourneyAndRoutes(journeyResponse, journeyAirports,
                            journeyAirlines, routesAirlinesAndAirports, isReturnTrip)
                }
    }

    fun getSearchSingle(params: HashMap<String, Any>, isReturnTrip: Boolean) : Observable<Meta> {
        return flightSearchDataCloudSource.getData(params).flatMap { response ->
            Observable.from(response.data).flatMap { journeyResponse ->
                generateJourneyAndRoutesObservable(journeyResponse, isReturnTrip)
            }.toList().map { journeyAndRoutesList ->
                flightSearchSingleDataDbSource.insertList(journeyAndRoutesList)
                val meta = response.meta
                meta.airlines = getAirlines(journeyAndRoutesList)
                meta
            }
        }
    }

    // call search single api and then combine the result with combo, airport and airline db
    fun getSearchSingleCombined(params: HashMap<String, Any>, isReturnTrip: Boolean): Observable<Meta> {
        return flightSearchDataCloudSource.getData(params).flatMap { response ->
            Observable.from(response.data).flatMap { journeyResponse ->
                generateJourneyAndRoutesObservable(journeyResponse, isReturnTrip)
                        .zipWith(flightSearchCombinedDataDbSource.getSearchCombined(journeyResponse.id)) {
                            journeyAndRoutes: JourneyAndRoutes, combos: List<FlightComboTable> ->
                            if (!combos.isEmpty()) {
                                val comboBestPairing = combos.find { it.isBestPairing }
                                val journeyTable = journeyAndRoutes.flightJourneyTable
                                if (comboBestPairing != null) {
                                    journeyAndRoutes.flightJourneyTable =
                                            createJourneyWithCombo(journeyTable, comboBestPairing)
                                } else {
                                    journeyAndRoutes.flightJourneyTable =
                                            createJourneyWithCombo(journeyTable, combos[0])
                                }
                                journeyAndRoutes
                            } else {
                                journeyAndRoutes
                            }
                        }
            }.toList().map { journeyAndRoutesList ->
                flightSearchSingleDataDbSource.insertList(journeyAndRoutesList)
                val meta = response.meta
                meta.airlines = getAirlines(journeyAndRoutesList)
                meta
            }
        }
    }

    fun getSearchCombinedReturn(params: HashMap<String, Any>?, onwardJourneyId: String?, isReturn: Boolean):
            Observable<Meta> {
        return flightSearchDataCloudSource.getData(params).flatMap { response ->
            Observable.from(response.data).flatMap { journeyResponse ->
                generateJourneyAndRoutesObservable(journeyResponse, isReturn)
                        .zipWith(flightSearchCombinedDataDbSource.getSearchCombined(journeyResponse.id)
                                .flatMapIterable { it }
                                .filter { it.onwardJourneyId == onwardJourneyId }
                                .toList()) {
                            journeyAndRoutes: JourneyAndRoutes, combos: List<FlightComboTable> ->
                            if (!combos.isEmpty()) {
                                val comboBestPairing = combos.find { it.isBestPairing }
                                val journeyTable = journeyAndRoutes.flightJourneyTable
                                if (comboBestPairing != null) {
                                    journeyAndRoutes.flightJourneyTable =
                                            createJourneyWithCombo(journeyTable, comboBestPairing)
                                } else {
                                    journeyAndRoutes.flightJourneyTable =
                                            createJourneyWithCombo(journeyTable, combos[0])
                                }
                                journeyAndRoutes
                            } else {
                                journeyAndRoutes
                            }
                        }
            }.toList().map { journeyAndRoutesList ->
                flightSearchSingleDataDbSource.insertList(journeyAndRoutesList)
                val meta = response.meta
                meta.airlines = getAirlines(journeyAndRoutesList)
                meta
            }
        }
    }

    fun getSearchCombined(flightSearchCombinedApiRequestModel: FlightSearchCombinedApiRequestModel):
            Observable<Meta> {
        return flightSearchCombinedDataApiSource.getData(flightSearchCombinedApiRequestModel)
                .map { response ->
                    val combosTable = arrayListOf<FlightComboTable>()
                    for (comboResponse in response.data) {
                        with(comboResponse) {
                            with(attributes) {
                                val onwardJourney = combos[0]
                                val returnJourney = combos[1]
                                combosTable.add(FlightComboTable(
                                        onwardJourney.id,
                                        returnJourney.id,
                                        id,
                                        onwardJourney.adultPrice,
                                        onwardJourney.childPrice,
                                        onwardJourney.infantPrice,
                                        onwardJourney.adultPriceNumeric,
                                        onwardJourney.childPriceNumeric,
                                        onwardJourney.infantPriceNumeric,
                                        isBestPairing
                                ))
                            }
                        }
                    }
                    ComboAndMetaWrapper(combosTable, response.meta)
                }.flatMap { comboAndMetaWrapper ->
                    Observable.just(flightSearchCombinedDataDbSource.insert(comboAndMetaWrapper.flightComboTables))
                            .map { comboAndMetaWrapper.meta }
                }
    }

    fun getSearchFilter(@FlightSortOption sortOption: Int, filterModel: FlightFilterModel):
            Observable<List<JourneyAndRoutes>> {
        return flightSearchSingleDataDbSource.getFilteredJourneys(filterModel, sortOption)
                .flatMap { journeyAndRoutesList ->
                    Observable.from(journeyAndRoutesList).flatMap { journeyAndRoutes ->
                        Observable.from(journeyAndRoutes.routes)
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
                    }.toList()
                }
    }

    fun getSearchJourneyById(journeyId: String): Observable<JourneyAndRoutes> {
        return flightSearchSingleDataDbSource.getJourneyById(journeyId)
                .flatMap { journeyAndRoutes ->
                    Observable.from(journeyAndRoutes.routes)
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
    }

    fun getSearchCount(filterModel: FlightFilterModel): Observable<Int> {
        return flightSearchSingleDataDbSource.getSearchCount(filterModel)
    }

    fun getSearchReturnBestPairsByOnwardJourneyId(filterModel: FlightFilterModel) : Observable<List<JourneyAndRoutes>> {
        return flightSearchCombinedDataDbSource.getSearchCombined(filterModel.journeyId)
                .flatMap {
                    Observable.from(it)
                            .filter { combo -> combo.isBestPairing }
                            .flatMap { bestPairingCombo ->
                                flightSearchSingleDataDbSource.getJourneyById(bestPairingCombo.returnJourneyId)
                            }
                            .toList()
                }
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

    private fun createCompleteJourneyAndRoutes(journeyResponse: FlightSearchData,
                                               journeyAirports: Pair<FlightAirportDB, FlightAirportDB>,
                                               journeyAirlines: List<FlightAirlineDB>,
                                               routesAirlinesAndAirports: List<Pair<FlightAirlineDB, Pair<FlightAirportDB, FlightAirportDB>>>,
                                               isReturn: Boolean): JourneyAndRoutes {
        val isRefundable = isRefundable(journeyResponse.attributes.routes)
        val flightJourneyTable = createFlightJourneyTable(journeyResponse.id, journeyResponse.attributes,
                isRefundable, isReturn)
        val routesAirlines = arrayListOf<FlightAirlineDB>()
        val routesAirports = arrayListOf<Pair<FlightAirportDB, FlightAirportDB>>()
        for (routeAirlineAndAirport in routesAirlinesAndAirports) {
            routesAirlines.add(routeAirlineAndAirport.first)
            routesAirports.add(Pair(routeAirlineAndAirport.second.first, routeAirlineAndAirport.second.second))
        }
        val completeJourney = createJourneyWithAirportAndAirline(
                flightJourneyTable, journeyAirports, journeyAirlines)
        val routes = createRoutes(journeyResponse.attributes.routes, journeyResponse.id, routesAirports,
                routesAirlines)
        return JourneyAndRoutes(completeJourney, routes)
    }

    private fun createFlightJourneyTable(journeyId: String, attributes: Attributes, isRefundable: RefundableEnum,
                                         isReturn: Boolean)
            : FlightJourneyTable {
        with(attributes) {
            return FlightJourneyTable(
                    journeyId,
                    term,
                    departureAirport,
                    "",
                    "",
                    arrivalAirport,
                    "",
                    "",
                    null,
                    departureTime,
                    departureTimeInt,
                    arrivalTime,
                    arrivalTimeInt,
                    totalTransit,
                    totalStop,
                    addDayArrival,
                    duration,
                    durationMinute,
                    fare.adult,
                    "",
                    fare.adultNumeric,
                    0,
                    fare.child,
                    "",
                    fare.childNumeric,
                    0,
                    fare.infant,
                    "",
                    fare.infantNumeric,
                    0,
                    total,
                    "",
                    totalNumeric,
                    0,
                    false,
                    beforeTotal,
                    fare.adultNumeric,
                    isReturn,
                    isRefundable,
                    !TextUtils.isEmpty(beforeTotal),
                    ""
            )
        }
    }

    private fun createRoutes(routes: List<Route>, journeyId: String,
                             routesAirports: List<Pair<FlightAirportDB, FlightAirportDB>>,
                             routesAirlines: List<FlightAirlineDB>): List<FlightRouteTable> {
        val gson = Gson()
        return routes.zip(routesAirports).zip(routesAirlines).map { it ->
            val (route, pairOfAirport) = it.first
            val routeAirline = it.second
            val routeDepartureAirport = pairOfAirport.first
            val routeArrivalAirport = pairOfAirport.second
            with(route) {
                FlightRouteTable(
                        journeyId,
                        airline,
                        routeAirline.name,
                        routeAirline.logo,
                        departureAirport,
                        routeDepartureAirport.airportName,
                        routeDepartureAirport.cityName,
                        arrivalAirport,
                        routeArrivalAirport.airportName,
                        routeArrivalAirport.cityName,
                        departureTimestamp,
                        arrivalTimestamp,
                        duration,
                        gson.toJson(infos),
                        layover,
                        flightNumber,
                        refundable,
                        gson.toJson(amenities),
                        stops,
                        gson.toJson(stopDetails)
                )
            }
        }
    }

    private fun createJourneyWithCombo(journey: FlightJourneyTable, flightComboTable: FlightComboTable): FlightJourneyTable {
        with(flightComboTable) {
            journey.adultCombo = adultPrice
            journey.childCombo = childPrice
            journey.infantCombo = infantPrice
            journey.adultNumericCombo = adultPriceNumeric
            journey.childNumericCombo = childPriceNumeric
            journey.infantNumericCombo = infantPriceNumeric
            journey.isBestPairing = isBestPairing
            journey.comboId = comboId
            journey.sortPrice = adultPriceNumeric
        }
        return journey
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

    private fun isRefundable(routes: List<Route>): RefundableEnum {
        var refundableCount = 0
        for (route in routes) {
            if (route.refundable) {
                refundableCount++
            }
        }
        return when (refundableCount) {
            routes.size -> RefundableEnum.REFUNDABLE
            0 -> RefundableEnum.NOT_REFUNDABLE
            else -> RefundableEnum.PARTIAL_REFUNDABLE
        }
    }

    private fun getAirlines(journeyAndRoutesList: List<JourneyAndRoutes>): List<String> {
        val airlines = arrayListOf<String>()
        for (journeyAndRoutes in journeyAndRoutesList) {
            for (route in journeyAndRoutes.routes) {
                if (!airlines.contains(route.airline)) {
                    airlines.add(route.airline)
                }
            }
        }
        return airlines
    }

    fun deleteFlightSearchReturnData(): Observable<Unit> {
        val filterModel = FlightFilterModel()
        filterModel.isReturn = true
        return flightSearchSingleDataDbSource.getFilteredJourneys(filterModel, FlightSortOption.NO_PREFERENCE)
                .flatMap { Observable.just(deleteFlightSearchReturnData(it)) }
    }

    fun deleteAllFlightSearchData() : Observable<Unit> {
        return Observable.create {
            it.onNext(deleteFlightSearchData())
        }
    }

    private fun deleteFlightSearchReturnData(journeyAndRoutesList: List<JourneyAndRoutes>) {
        flightSearchSingleDataDbSource.deleteSearchReturnData(journeyAndRoutesList)
    }

    private fun deleteFlightSearchData() {
        flightSearchSingleDataDbSource.deleteAllFlightSearchData()
        flightSearchCombinedDataDbSource.deleteAllFlightSearchCombinedData()
    }

}