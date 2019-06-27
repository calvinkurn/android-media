package com.tokopedia.flight.search.data.repository

import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.search.data.api.combined.FlightSearchCombinedDataApiSource
import com.tokopedia.flight.search.data.api.single.FlightSearchDataCloudSource
import com.tokopedia.flight.search.data.api.single.response.*
import com.tokopedia.flight.search.data.cache.FlightSearchDataCacheSource
import com.tokopedia.flight.search.data.db.*
import com.tokopedia.flight.search.data.repository.mapper.FlightSearchMapper
import com.tokopedia.flight.search.presentation.model.FlightAirlineViewModel
import com.tokopedia.flight.search.presentation.model.FlightAirportViewModel
import com.tokopedia.flight.search.presentation.model.FlightSearchCombinedApiRequestModel
import com.tokopedia.flight.search.presentation.model.filter.FlightFilterModel
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
        private val flightSearchMapper: FlightSearchMapper,
        private val flightSearchDataCacheSource: FlightSearchDataCacheSource) {

    private fun generateJourneyAndRoutesObservable(journeyResponse: FlightSearchData, included: List<Included<AttributesInc>>, isReturnTrip: Boolean):
            Observable<JourneyAndRoutes> {
        return Observable.from(journeyResponse.attributes.routes)
                .flatMap { route -> getRouteAirlineByIdAndAirports(route, included) }
                .toList()
                .zipWith(getAirports(journeyResponse.attributes.departureAirport, journeyResponse.attributes.arrivalAirport, included)) { routesAirlinesAndAirports: List<Pair<FlightAirlineViewModel, Pair<FlightAirportViewModel, FlightAirportViewModel>>>,
                                                                                                                                          journeyAirports: Pair<FlightAirportViewModel, FlightAirportViewModel> ->
                    val journeyAirlines = arrayListOf<FlightAirlineViewModel>()
                    for (routeAirline in routesAirlinesAndAirports) {
                        if (!journeyAirlines.contains(routeAirline.first)) {
                            journeyAirlines.add(routeAirline.first)
                        }
                    }
                    flightSearchMapper.createCompleteJourneyAndRoutes(journeyResponse, journeyAirports,
                            journeyAirlines, routesAirlinesAndAirports, isReturnTrip)
                }
    }

    fun getSearchSingle(params: HashMap<String, Any>, isReturnTrip: Boolean): Observable<Meta> {
        return flightSearchDataCloudSource.getData(params).flatMap { response ->
            Observable.from(response.data).flatMap { journeyResponse ->
                generateJourneyAndRoutesObservable(journeyResponse, response.included, isReturnTrip)
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
                generateJourneyAndRoutesObservable(journeyResponse, response.included, isReturnTrip)
                        .zipWith(flightSearchCombinedDataDbSource.getSearchOnwardCombined(journeyResponse.id)) { journeyAndRoutes: JourneyAndRoutes, combos: List<FlightComboTable> ->
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
                generateJourneyAndRoutesObservable(journeyResponse, response.included, isReturn)
                        .zipWith(flightSearchCombinedDataDbSource.getSearchReturnCombined(journeyResponse.id)
                                .flatMap { combos ->
                                    if (combos != null && !combos.isEmpty()) {
                                        Observable.from(combos)
                                                .takeFirst { it.onwardJourneyId == onwardJourneyId }
                                                .defaultIfEmpty(null)
                                    } else {
                                        Observable.just(null)
                                    }
                                }) { journeyAndRoutes: JourneyAndRoutes, combo: FlightComboTable? ->
                            val journeyTable = journeyAndRoutes.flightJourneyTable
                            journeyAndRoutes.flightJourneyTable = combo?.let {
                                flightSearchMapper.createJourneyWithCombo(journeyTable, it)
                            } ?: run {
                                journeyTable
                            }
                            journeyAndRoutes
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
                .flatMap { response ->
                    Observable.from(response.data)
                            .map { comboResponse ->
                                with(comboResponse) {
                                    with(comboResponse.attributes) {
                                        val onwardJourneyCombo = combos[0]
                                        val returnJourneyCombo = combos[1]
                                        val comboTable = FlightComboTable(
                                                onwardJourneyCombo.id,
                                                onwardJourneyCombo.adultPrice,
                                                onwardJourneyCombo.childPrice,
                                                onwardJourneyCombo.infantPrice,
                                                onwardJourneyCombo.adultPriceNumeric,
                                                onwardJourneyCombo.childPriceNumeric,
                                                onwardJourneyCombo.infantPriceNumeric,
                                                returnJourneyCombo.id,
                                                returnJourneyCombo.adultPrice,
                                                returnJourneyCombo.childPrice,
                                                returnJourneyCombo.infantPrice,
                                                returnJourneyCombo.adultPriceNumeric,
                                                returnJourneyCombo.childPriceNumeric,
                                                returnJourneyCombo.infantPriceNumeric,
                                                id,
                                                isBestPairing
                                        )

                                        // update journey data
                                        val latestJourneyList = arrayListOf<FlightJourneyTable>()
                                        val onwardJourneyTable = flightSearchSingleDataDbSource
                                                .getJourneyRouteById(onwardJourneyCombo.id)?.flightJourneyTable
                                        val returnJourneyTable = flightSearchSingleDataDbSource
                                                .getJourneyRouteById(returnJourneyCombo.id)?.flightJourneyTable

                                        if (onwardJourneyTable != null && isJourneyNeedToUpdate(onwardJourneyTable, onwardJourneyCombo.adultPriceNumeric)) {
                                            latestJourneyList.add(flightSearchMapper.createJourneyWithCombo(
                                                    onwardJourneyTable, comboTable))
                                        }
                                        if (returnJourneyTable != null && isJourneyNeedToUpdate(returnJourneyTable, returnJourneyCombo.adultPriceNumeric)) {
                                            latestJourneyList.add(flightSearchMapper.createJourneyWithCombo(
                                                    returnJourneyTable, comboTable))
                                        }
                                        flightSearchSingleDataDbSource.updateJourneyDataList(latestJourneyList)

                                        comboTable
                                    }
                                }
                            }
                            .toList()
                            .flatMap { combosTable ->
                                Observable.just(flightSearchCombinedDataDbSource.insert(combosTable))
                                        .map { response.meta }
                            }
                }
    }

    fun getSearchFilter(@TravelSortOption sortOption: Int, filterModel: FlightFilterModel):
            Observable<JourneyAndRoutesModel> {
        return flightSearchSingleDataDbSource.getFilteredJourneys(filterModel, sortOption).zipWith(
                flightSearchDataCacheSource.cache, object : Func2<List<JourneyAndRoutes>, String, JourneyAndRoutesModel> {
            override fun call(t1: List<JourneyAndRoutes>, t2: String): JourneyAndRoutesModel {
                return JourneyAndRoutesModel(t1, t2)
            }
        })
    }

    fun getSearchJourneyById(journeyId: String): Observable<JourneyAndRoutes> {
        return flightSearchSingleDataDbSource.getJourneyById(journeyId)
    }

    fun getSearchCount(filterModel: FlightFilterModel): Observable<Int> {
        return flightSearchSingleDataDbSource.getSearchCount(filterModel)
    }

    private fun getRouteAirlineByIdAndAirports(route: Route, included: List<Included<AttributesInc>>):
            Observable<Pair<FlightAirlineViewModel, Pair<FlightAirportViewModel, FlightAirportViewModel>>>? {
        return getAirlineById(route.airline, included)
                .zipWith(getAirports(route.departureAirport, route.arrivalAirport, included)) { airline: FlightAirlineViewModel, airport: Pair<FlightAirportViewModel, FlightAirportViewModel> ->
                    Pair(airline, airport)
                }
    }

    private fun getAirports(departureAirport: String, arrivalAirport: String, included: List<Included<AttributesInc>>):
            Observable<Pair<FlightAirportViewModel, FlightAirportViewModel>> {

        val foundDepAirportObservable = Observable.from(included)
                .takeFirst { it.type == "airport" && it.id == departureAirport }
                .map { flightSearchMapper.extractAirportFromIncluded(it) }
        val foundArrAirportObservable = Observable.from(included)
                .takeFirst { it.type == "airport" && it.id == arrivalAirport }
                .map { flightSearchMapper.extractAirportFromIncluded(it) }

        return Observable.zip(foundDepAirportObservable, foundArrAirportObservable) { t1, t2 ->
            Pair(t1, t2)
        }
    }

    private fun getAirlineById(airlineId: String, included: List<Included<AttributesInc>>): Observable<FlightAirlineViewModel> {
        return Observable.from(included)
                .takeFirst { it.type == "airline" && it.id == airlineId }
                .map {
                    FlightAirlineViewModel(it.id, (it.attributes as AttributesAirline).name,
                            (it.attributes as AttributesAirline).shortName, (it.attributes as AttributesAirline).logo)
                }
    }

    fun deleteFlightSearchReturnData(): Observable<Unit> {
        val filterModel = FlightFilterModel()
        filterModel.isReturn = true
        return flightSearchSingleDataDbSource.getFilteredJourneys(filterModel, TravelSortOption.NO_PREFERENCE)
                .flatMap { Observable.just(deleteFlightSearchReturnData(it)) }
    }

    fun deleteAllFlightSearchData(): Observable<Unit> {
        return Observable.create {
            it.onNext(deleteFlightSearchData())
        }
    }

    fun getComboKey(onwardJourneyId: String, returnJourneyId: String) :  Observable<String> =
            flightSearchCombinedDataDbSource.getComboData(onwardJourneyId, returnJourneyId)
                    .map {
                        if (it.isNotEmpty()) {
                            it[0].comboId
                        } else {
                            ""
                        }
                    }

    private fun deleteFlightSearchReturnData(journeyAndRoutesList: List<JourneyAndRoutes>) {
        flightSearchSingleDataDbSource.deleteSearchReturnData(journeyAndRoutesList)
    }

    private fun deleteFlightSearchData() {
        flightSearchSingleDataDbSource.deleteAllFlightSearchData()
        flightSearchCombinedDataDbSource.deleteAllFlightSearchCombinedData()
    }

    private fun isJourneyNeedToUpdate(journeyTable: FlightJourneyTable, comboPrice: Int): Boolean =
            !journeyTable.isBestPairing && (journeyTable.adultNumericCombo == 0 ||
                    journeyTable.adultNumericCombo > comboPrice)

}