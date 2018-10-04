package com.tokopedia.flight.searchV2.data.repository

import com.tokopedia.flight.airline.data.db.FlightAirlineDataListDBSource
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB
import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.flight.search.data.cloud.model.response.*
import com.tokopedia.flight.searchV2.data.ResponseJourneysAndMetaWrapper
import com.tokopedia.flight.searchV2.data.api.combined.FlightSearchCombinedDataApiSource
import com.tokopedia.flight.searchV2.data.api.combined.response.FlightSearchCombinedResponse
import com.tokopedia.flight.searchV2.data.db.*
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchCombinedApiRequestModel
import rx.Observable
import rx.functions.Func2
import java.util.*
import javax.inject.Inject

/**
 * Created by Rizky on 20/09/18.
 */
open class FlightSearchRepository @Inject constructor(
        private val flightSearchCombinedDataApiSource: FlightSearchCombinedDataApiSource,
        private val flightSearchDataCloudSource: FlightSearchDataCloudSource,
        private val flightSearchComboDataDbSource: FlightSearchComboDataDbSource,
        private val flightSearchSingleDataDbSource: FlightSearchSingleDataDbSource,
        private val flightAirportDataListDBSource: FlightAirportDataListDBSource,
        private val flightAirlineDataListDBSource: FlightAirlineDataListDBSource) {

    // simply call search combined and then insert to db
    fun getSearchCombined(flightSearchCombinedApiRequestModel: FlightSearchCombinedApiRequestModel):
            Observable<List<FlightComboTable>> {
        return object : NetworkBoundResourceObservable<FlightDataResponse<List<FlightSearchCombinedResponse>>,
                List<FlightComboTable>>() {
            override fun loadFromDb(): Observable<List<FlightComboTable>> {
                return flightSearchComboDataDbSource.getAllCombos()
            }

            override fun shouldFetch(data: List<FlightComboTable>?) = data == null || data.isEmpty()

            override fun createCall(): Observable<FlightDataResponse<List<FlightSearchCombinedResponse>>> =
                    flightSearchCombinedDataApiSource.getData(flightSearchCombinedApiRequestModel)

            override fun mapResponse(response: FlightDataResponse<List<FlightSearchCombinedResponse>>): List<FlightComboTable> {
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
                return combosTable
            }

            override fun saveCallResult(items: List<FlightComboTable>) {
                flightSearchComboDataDbSource.insert(items)
            }
        }.asObservable()
    }

    // call search single api and then combine the result with combo, airport and airline db
    fun getSearchSingleCombined(params: HashMap<String, Any>): Observable<Meta> {
        return flightSearchDataCloudSource.getData(params).flatMap { it ->
            Observable.just(it).flatMap { response ->
                Observable.from(response.data).flatMap { journey ->
                    Observable.from(journey.attributes.routes)
                            .flatMap { route -> getAirlineById(route.airline) }
                            .toList()
                            .zipWith(getAirports(journey.attributes.departureAirport, journey.attributes.arrivalAirport)) {
                                airlines: List<FlightAirlineDB>, pairOfAirport: Pair<FlightAirportDB, FlightAirportDB> ->
                                val routes = createRoutes(journey.attributes.routes, journey.id)
                                val flightJourneyTable = createJourney(journey.id, journey.attributes, routes)
                                return@zipWith createJourneyWithAirportAndAirline(flightJourneyTable, pairOfAirport, airlines)
                            }.zipWith(flightSearchComboDataDbSource.getSearchCombined(journey.id)) {
                                journeyTable: FlightJourneyTable, combos: List<FlightComboTable> ->
                                val flightJourneyTable: FlightJourneyTable = if (!combos.isEmpty()) {
                                    val comboBestPairing = combos.find {
                                        it.isBestPairing
                                    }
                                    if (comboBestPairing != null) {
                                        createJourneyWithCombo(journeyTable, comboBestPairing)
                                    } else {
                                        createJourneyWithCombo(journeyTable, combos[0])
                                    }
                                } else {
                                    journeyTable
                                }
                                flightJourneyTable
                            }
                }.toList().map {
                    flightSearchSingleDataDbSource.insert(it)
                    response.meta
                }
            }
        }
    }

    private fun createJourneyWithCombo(journey: FlightJourneyTable, flightComboTable: FlightComboTable): FlightJourneyTable {
        journey.adultCombo = flightComboTable.adultPrice
        journey.childCombo = flightComboTable.childPrice
        journey.infantCombo = flightComboTable.infantPrice
        journey.adultNumericCombo = flightComboTable.adultPriceNumeric
        journey.childNumericCombo = flightComboTable.childPriceNumeric
        journey.infantNumericCombo = flightComboTable.infantPriceNumeric
        journey.isBestPairing = flightComboTable.isBestPairing
        return journey
    }

    // call get search single api and save the result to db
    fun getSearchSingle(params: HashMap<String, Any>) : Observable<Meta> {
        return object : NetworkBoundResourceObservable<FlightDataResponse<List<FlightSearchData>>,
                ResponseJourneysAndMetaWrapper>() {
            override fun loadFromDb(): Observable<ResponseJourneysAndMetaWrapper> {
                return flightSearchSingleDataDbSource.getSearchSingle().map {
                    ResponseJourneysAndMetaWrapper(it, Meta())
                }
            }

            override fun shouldFetch(data: ResponseJourneysAndMetaWrapper?) =
                    data?.flightJourneys == null || data.flightJourneys.isEmpty()

            override fun createCall(): Observable<FlightDataResponse<List<FlightSearchData>>> =
                    flightSearchDataCloudSource.getData(params)

            override fun mapResponse(response: FlightDataResponse<List<FlightSearchData>>): ResponseJourneysAndMetaWrapper {
                val journeys = arrayListOf<FlightJourneyTable>()
                for (journey in response.data) {
                    with(journey.attributes) {
                        val routes = createRoutes(routes, journey.id)
                        val flightJourneyTable = createJourney(journey.id, this, routes)
                        journeys.add(flightJourneyTable)
                    }
                }
                return ResponseJourneysAndMetaWrapper(journeys, response.meta)
            }

            override fun saveCallResult(items: ResponseJourneysAndMetaWrapper) {
                for (journey in items.flightJourneys) {
                    Observable.from(journey.routes)
                            .flatMap { getAirlineById(it.airline) }
                            .toList()
                            .flatMap { airlines ->
                                getAirports(journey.departureAirport, journey.arrivalAirport).map { pairOfAirport ->
                                    val flightJourneyTable =
                                            createJourneyWithAirportAndAirline(journey, pairOfAirport,
                                                    airlines)
                                    flightSearchSingleDataDbSource.insert(flightJourneyTable)
                                }
                            }
                }
            }
        }.asObservable().map {
            it.meta
        }
    }

    private fun createJourneyWithAirportAndAirline(journey: FlightJourneyTable,
                                                   pairOfAirport: Pair<FlightAirportDB, FlightAirportDB>,
                                                   airlines: List<FlightAirlineDB>): FlightJourneyTable {
        journey.departureAirportName = pairOfAirport.first.airportName
        journey.departureAirportCity = pairOfAirport.first.cityName
        journey.arrivalAirportName = pairOfAirport.second.airportName
        journey.arrivalAirportCity = pairOfAirport.second.cityName
        journey.flightAirlineDBS = airlines
        return journey
    }

    fun getAirports(departureAirport: String, arrivalAirport: String) :
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

    private fun createJourney(journeyId: String, attributes: Attributes, routes: List<FlightRouteTable>)
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
                    routes
            )
        }
    }

    fun createRoutes(routes: List<Route>, journeyId: String) : List<FlightRouteTable> {
        return routes.map {
            with(it) {
                FlightRouteTable(
                        journeyId,
                        airline,
                        departureAirport,
                        arrivalAirport,
                        departureTimestamp,
                        arrivalTimestamp,
                        duration,
                        layover,
                        flightNumber,
                        refundable,
                        stops
                )
            }
        }
    }

    fun getAirlineById(airlineId: String): Observable<FlightAirlineDB> {
        return flightAirlineDataListDBSource.getAirline(airlineId)
    }

}