package com.tokopedia.flight.searchV2.data.repository

import com.tokopedia.flight.search.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData
import com.tokopedia.flight.searchV2.data.db.NetworkBoundResourceObservable
import com.tokopedia.flight.searchV2.data.api.combined.FlightSearchCombinedDataApiSource
import com.tokopedia.flight.searchV2.data.api.combined.response.FlightSearchCombinedResponse
import com.tokopedia.flight.searchV2.data.db.*
import com.tokopedia.flight.searchV2.presentation.model.FlightSearchMetaViewModel
import rx.Observable
import java.util.*

/**
 * Created by Rizky on 20/09/18.
 */
class FlightSearchRepository(private val flightSearchCombinedDataApiSource: FlightSearchCombinedDataApiSource,
                             private val flightSearchDataCloudSource: FlightSearchDataCloudSource,
                             private val flightSearchCombinedDataDbSource: FlightSearchCombinedDataDbSource,
                             private val flightSearchSingleDataDbSource: FlightSearchSingleDataDbSource) {

    fun getSearchCombined(params: HashMap<String, Any>): Observable<List<Combo>>? {
        val journeyId = ""
        return object : NetworkBoundResourceObservable<List<Combo>,
                FlightDataResponse<List<FlightSearchCombinedResponse>>>() {
            override fun loadFromDb(): Observable<List<Combo>> {
                return flightSearchCombinedDataDbSource.getSearchCombined(journeyId)
            }

            override fun shouldFetch(data: List<Combo>?) = data == null

            override fun createCall(): Observable<FlightDataResponse<List<FlightSearchCombinedResponse>>> =
                    flightSearchCombinedDataApiSource.getData(params)

            override fun mapResponse(it: FlightDataResponse<List<FlightSearchCombinedResponse>>): List<Combo> {
                val combos = arrayListOf<Combo>()
                for (flightSearchCombinedResponse in it.data) {

                }
                return combos
            }

            override fun saveCallResult(item: List<Combo>) {

            }
        }.asObservable()
    }

    fun getSearchSingle(params: HashMap<String, Any>) : Observable<FlightSearchMetaViewModel>? {
        return object : NetworkBoundResourceObservable<List<Journey>,
                FlightDataResponse<List<FlightSearchData>>>() {
            override fun loadFromDb(): Observable<List<Journey>> {
                return flightSearchSingleDataDbSource.getSearchSingle()
            }

            override fun shouldFetch(data: List<Journey>?) = data == null

            override fun createCall(): Observable<FlightDataResponse<List<FlightSearchData>>> =
                    flightSearchDataCloudSource.getData(params)

            override fun mapResponse(it: FlightDataResponse<List<FlightSearchData>>): List<Journey> {
                val journeys = arrayListOf<Journey>()
                for (flightSearchSingleResponse in it.data) {
                    with(flightSearchSingleResponse.attributes) {
                        val routes = flightSearchSingleResponse.attributes.routes.map {
                            with(it) {
                                Route(
                                        journeyId = flightSearchSingleResponse.id,
                                        airline = airline,
                                        departureAirport = departureAirport,
                                        arrivalAirport = arrivalAirport,
                                        departureTimestamp = departureTimestamp,
                                        arrivalTimestamp = arrivalTimestamp,
                                        duration = duration,
                                        layover = layover,
                                        flightNumber = flightNumber,
                                        isRefundable = refundable,
                                        stops = stops
                                )
                            }
                        }
                        journeys.add(Journey(
                                id = flightSearchSingleResponse.id,
                                term = term,
                                departureAirport = departureAirport,
                                departureTime = departureTime,
                                departureTimeInt = departureTimeInt,
                                arrivalAirport = arrivalAirport,
                                arrivalTime = arrivalTime,
                                arrivalTimeInt = arrivalTimeInt,
                                totalTransit = totalTransit,
                                totalStop = totalStop,
                                addDayArrival = addDayArrival,
                                duration = duration,
                                durationMinute = durationMinute,
                                total = total,
                                totalNumeric = totalNumeric,
                                beforeTotal = beforeTotal,
                                routes = routes
                        ))
                    }
                }
                return journeys
            }

            override fun saveCallResult(item: List<Journey>) {
                flightSearchSingleDataDbSource.insert(item)
            }
        }.asObservable()?.map {
            FlightSearchMetaViewModel()
        }
    }

}