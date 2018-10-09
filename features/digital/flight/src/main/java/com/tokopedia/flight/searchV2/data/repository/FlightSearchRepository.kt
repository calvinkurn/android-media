package com.tokopedia.flight.searchV2.data.repository

import android.text.TextUtils
import com.tokopedia.flight.airline.data.db.FlightAirlineDataListDBSource
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB
import com.tokopedia.flight.airport.data.source.db.FlightAirportDataListDBSource
import com.tokopedia.flight.airport.data.source.db.model.FlightAirportDB
import com.tokopedia.flight.search.constant.FlightSortOption
import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.flight.search.data.cloud.model.response.*
import com.tokopedia.flight.search.view.model.filter.RefundableEnum
import com.tokopedia.flight.searchV2.data.ResponseJourneysAndMetaWrapper
import com.tokopedia.flight.searchV2.data.api.combined.FlightSearchCombinedDataApiSource
import com.tokopedia.flight.searchV2.data.api.combined.response.FlightSearchCombinedResponse
import com.tokopedia.flight.searchV2.data.db.*
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchCombinedApiRequestModel
import com.tokopedia.flight.searchV2.presentation.model.filter.FlightFilterModel
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
        private val flightSearchCombinedDataDbSource: FlightSearchCombinedDataDbSource,
        private val flightSearchSingleDataDbSource: FlightSearchSingleDataDbSource,
        private val flightAirportDataListDBSource: FlightAirportDataListDBSource,
        private val flightAirlineDataListDBSource: FlightAirlineDataListDBSource) {

    // simply call search combined and then insert to db
    fun getSearchCombined(flightSearchCombinedApiRequestModel: FlightSearchCombinedApiRequestModel):
            Observable<List<FlightComboTable>> {
        return object : NetworkBoundResourceObservable<FlightDataResponse<List<FlightSearchCombinedResponse>>,
                List<FlightComboTable>>() {
            override fun loadFromDb(): Observable<List<FlightComboTable>> {
                return flightSearchCombinedDataDbSource.getAllCombos()
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
                flightSearchCombinedDataDbSource.insert(items)
            }
        }.asObservable()
    }

    // call search single api and then combine the result with combo, airport and airline db
    fun getSearchSingleCombined(params: HashMap<String, Any>): Observable<Meta> {
        return flightSearchDataCloudSource.getData(params).flatMap { response ->
            Observable.from(response.data).flatMap { journeyResponse ->
                Observable.from(journeyResponse.attributes.routes)
                        .flatMap { route -> getAirlineById(route.airline) }
                        .toList()
                        .zipWith(getAirports(journeyResponse.attributes.departureAirport, journeyResponse.attributes.arrivalAirport)) {
                            airlines: List<FlightAirlineDB>, pairOfAirport: Pair<FlightAirportDB, FlightAirportDB> ->
                            val isRefundable = isRefundable(journeyResponse.attributes.routes)
                            val flightJourneyTable = createJourney(journeyResponse.id, journeyResponse.attributes, isRefundable)
                            val completeJourney = createJourneyWithAirportAndAirline(
                                    flightJourneyTable, pairOfAirport, airlines)
                            val routes = createRoutes(journeyResponse.attributes.routes, journeyResponse.id)
                            return@zipWith JourneyAndRoutes(completeJourney, routes)
                        }.zipWith(flightSearchCombinedDataDbSource.getSearchCombined(journeyResponse.id)) {
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
            }.toList().map { journeyAndRoutes ->
                flightSearchSingleDataDbSource.insert(journeyAndRoutes)
                response.meta
            }

        }
    }

    // call get search single api and save the result to db
    fun getSearchSingle(params: HashMap<String, Any>) : Observable<Meta> {
        return object : NetworkBoundResourceObservable<FlightDataResponse<List<FlightSearchData>>,
                ResponseJourneysAndMetaWrapper>() {
            override fun loadFromDb(): Observable<ResponseJourneysAndMetaWrapper> {
                return flightSearchSingleDataDbSource.findAllJourneys().map {
                    ResponseJourneysAndMetaWrapper(it, Meta())
                }
            }

            override fun shouldFetch(data: ResponseJourneysAndMetaWrapper?) =
                    data?.flightJourneys == null || data.flightJourneys.isEmpty()

            override fun createCall(): Observable<FlightDataResponse<List<FlightSearchData>>> =
                    flightSearchDataCloudSource.getData(params)

            override fun mapResponse(response: FlightDataResponse<List<FlightSearchData>>): ResponseJourneysAndMetaWrapper {
                val journeyAndRoutesJavaList = arrayListOf<JourneyAndRoutes>()
                for (journey in response.data) {
                    with(journey.attributes) {
                        val routes = createRoutes(routes, journey.id)
                        val isRefundable = isRefundable(journey.attributes.routes)
                        val flightJourneyTable = createJourney(journey.id, this, isRefundable)
                        val journeyAndRoutesJava = JourneyAndRoutes(flightJourneyTable, routes)
                        journeyAndRoutesJavaList.add(journeyAndRoutesJava)
                    }
                }
                return ResponseJourneysAndMetaWrapper(journeyAndRoutesJavaList, response.meta)
            }

            override fun saveCallResult(items: ResponseJourneysAndMetaWrapper) {
                for (journey in items.flightJourneys) {
                    Observable.from(journey.routes)
                            .flatMap { getAirlineById(it.airline) }
                            .toList()
                            .flatMap { _ ->
                                getAirports(journey.flightJourneyTable.departureAirport, journey.flightJourneyTable.arrivalAirport)
                                        .map {
                                            flightSearchSingleDataDbSource.insert(journey)
                                        }
                            }
                }
            }
        }.asObservable().map {
            it.meta
        }
    }

    fun getSearchFilter(sortOption: FlightSortOption, filterModel: FlightFilterModel):
            Observable<List<JourneyAndRoutes>> {
        return flightSearchSingleDataDbSource.getFilteredJourneys(filterModel)
    }

    fun getSearchReturnBestPairsByOnwardJourneyId(onwardJourneyId: String) : Observable<List<JourneyAndRoutes>> {
        return flightSearchCombinedDataDbSource.getSearchCombined(onwardJourneyId)
                .flatMapIterable { it }
                .filter { it.isBestPairing }
                .flatMap { flightSearchSingleDataDbSource.getJourneyById(it.returnJourneyId) }
                .toList()
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

    private fun createJourney(journeyId: String, attributes: Attributes, isRefundable: RefundableEnum)
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
                    0,
                    false,
                    isRefundable,
                    !TextUtils.isEmpty(beforeTotal)
            )
        }
    }

    private fun createRoutes(routes: List<Route>, journeyId: String) : List<FlightRouteTable> {
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

    private fun createJourneyWithCombo(journey: FlightJourneyTable, flightComboTable: FlightComboTable): FlightJourneyTable {
        journey.adultCombo = flightComboTable.adultPrice
        journey.childCombo = flightComboTable.childPrice
        journey.infantCombo = flightComboTable.infantPrice
        journey.adultNumericCombo = flightComboTable.adultPriceNumeric
        journey.childNumericCombo = flightComboTable.childPriceNumeric
        journey.infantNumericCombo = flightComboTable.infantPriceNumeric
        journey.isBestPairing = flightComboTable.isBestPairing
        journey.sortPrice = flightComboTable.adultPriceNumeric
        return journey
    }

    private fun getAirlineById(airlineId: String): Observable<FlightAirlineDB> {
        return flightAirlineDataListDBSource.getAirline(airlineId)
    }

}