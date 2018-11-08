package com.tokopedia.flight.searchV2.data.repository

import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.airline.data.db.FlightAirlineDataListDBSource
import com.tokopedia.flight_dbflow.FlightAirlineDB
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource
import com.tokopedia.flight_dbflow.FlightAirportDB
import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData
import com.tokopedia.flight.search.data.cloud.model.response.Meta
import com.tokopedia.flight.searchV2.data.ComboAndMetaWrapper
import com.tokopedia.flight.searchV2.data.api.combined.FlightSearchCombinedDataApiSource
import com.tokopedia.flight.searchV2.data.db.FlightComboTable
import com.tokopedia.flight.searchV2.data.db.FlightSearchCombinedDataDbSource
import com.tokopedia.flight.searchV2.data.db.FlightSearchSingleDataDbSource
import com.tokopedia.flight.searchV2.data.db.JourneyAndRoutes
import com.tokopedia.flight.searchV2.data.repository.mapper.FlightSearchMapper
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
        private val flightAirlineDataListDBSource: FlightAirlineDataListDBSource,
        private val flightSearchMapper: FlightSearchMapper) {

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
                    flightSearchMapper.createCompleteJourneyAndRoutes(journeyResponse, journeyAirports,
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
                meta.airlines = flightSearchMapper.getAirlines(journeyAndRoutesList)
                meta
            }
        }
    }

    // call search single api and then combine the result with combo, airport and airline db
    fun getSearchSingleCombined(params: HashMap<String, Any>, isReturnTrip: Boolean): Observable<Meta> {
        return flightSearchDataCloudSource.getData(params).flatMap { response ->
            Observable.from(response.data).flatMap { journeyResponse ->
                generateJourneyAndRoutesObservable(journeyResponse, isReturnTrip)
                        .zipWith(flightSearchCombinedDataDbSource.getSearchOnwardCombined(journeyResponse.id)) {
                            journeyAndRoutes: JourneyAndRoutes, combos: List<FlightComboTable> ->
                            if (!combos.isEmpty()) {
                                val comboBestPairing = combos.find { it.isBestPairing }
                                val journeyTable = journeyAndRoutes.flightJourneyTable
                                if (comboBestPairing != null) {
                                    journeyAndRoutes.flightJourneyTable =
                                            flightSearchMapper.createJourneyWithCombo(journeyTable, comboBestPairing)
                                } else {
                                    journeyAndRoutes.flightJourneyTable =
                                            flightSearchMapper.createJourneyWithCombo(journeyTable, combos[0])
                                }
                                journeyAndRoutes
                            } else {
                                journeyAndRoutes
                            }
                        }
            }.toList().map { journeyAndRoutesList ->
                flightSearchSingleDataDbSource.insertList(journeyAndRoutesList)
                val meta = response.meta
                meta.airlines = flightSearchMapper.getAirlines(journeyAndRoutesList)
                meta
            }
        }
    }

    fun getSearchCombinedReturn(params: HashMap<String, Any>?, onwardJourneyId: String?, isReturn: Boolean):
            Observable<Meta> {
        return flightSearchDataCloudSource.getData(params).flatMap { response ->
            Observable.from(response.data).flatMap { journeyResponse ->
                generateJourneyAndRoutesObservable(journeyResponse, isReturn)
                        .zipWith(flightSearchCombinedDataDbSource.getSearchReturnCombined(journeyResponse.id)
                                .flatMap { it ->
                                    if (it != null && !it.isEmpty()) {
                                        Observable.from(it)
                                                .takeFirst { it.onwardJourneyId == onwardJourneyId }
                                                .defaultIfEmpty(null)
                                    } else {
                                        Observable.just(null)
                                    }
                                }) {
                            journeyAndRoutes: JourneyAndRoutes, combo: FlightComboTable? ->
                            if (combo != null) {
                                val journeyTable = journeyAndRoutes.flightJourneyTable
                                journeyAndRoutes.flightJourneyTable =
                                        flightSearchMapper.createJourneyWithCombo(journeyTable, combo)
                                journeyAndRoutes
                            } else {
                                journeyAndRoutes
                            }
                        }
            }.toList().map { journeyAndRoutesList ->
                flightSearchSingleDataDbSource.insertList(journeyAndRoutesList)
                val meta = response.meta
                meta.airlines = flightSearchMapper.getAirlines(journeyAndRoutesList)
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

    fun getSearchFilter(@TravelSortOption sortOption: Int, filterModel: FlightFilterModel):
            Observable<List<JourneyAndRoutes>> {
        return flightSearchSingleDataDbSource.getFilteredJourneys(filterModel, sortOption)
    }

    fun getSearchJourneyById(journeyId: String): Observable<JourneyAndRoutes> {
        return flightSearchSingleDataDbSource.getJourneyById(journeyId)
    }

    fun getSearchCount(filterModel: FlightFilterModel): Observable<Int> {
        return flightSearchSingleDataDbSource.getSearchCount(filterModel)
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

    fun deleteFlightSearchReturnData(): Observable<Unit> {
        val filterModel = FlightFilterModel()
        filterModel.isReturn = true
        return flightSearchSingleDataDbSource.getFilteredJourneys(filterModel, TravelSortOption.NO_PREFERENCE)
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