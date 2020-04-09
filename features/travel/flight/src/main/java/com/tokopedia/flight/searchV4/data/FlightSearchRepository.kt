package com.tokopedia.flight.searchV4.data

import com.tokopedia.flight.search.data.db.FlightSearchSingleDataDbSource
import com.tokopedia.flight.search.data.db.JourneyAndRoutes
import com.tokopedia.flight.search.presentation.model.FlightAirlineModel
import com.tokopedia.flight.search.presentation.model.FlightAirportModel
import com.tokopedia.flight.searchV4.data.cloud.FlightSearchDataCloudSource
import com.tokopedia.flight.searchV4.data.cloud.single.*
import com.tokopedia.flight.searchV4.domain.FlightSearchMapper.Companion.createCompleteJourneyAndRoutes
import com.tokopedia.flight.searchV4.domain.FlightSearchMapper.Companion.getAirlineById
import com.tokopedia.flight.searchV4.domain.FlightSearchMapper.Companion.getAirlines
import com.tokopedia.flight.searchV4.domain.FlightSearchMapper.Companion.getAirports
import javax.inject.Inject

/**
 * @author by furqan on 08/04/2020
 */
class FlightSearchRepository @Inject constructor(
        private val flightSearchSingleDataDbSource: FlightSearchSingleDataDbSource,
        private val flightSearchDataCloudSource: FlightSearchDataCloudSource) {

    suspend fun getSearchSingle(searchParam: FlightSearchRequestModel, isReturnTrip: Boolean): FlightSearchMetaEntity {
        val searchData = flightSearchDataCloudSource.getSearchSingleData(searchParam)
        val journeyAndRouteList = searchData.data.map {
            generateJourneyAndRoutes(it, searchData.included, isReturnTrip)
        }.toList()
        flightSearchSingleDataDbSource.insertList(journeyAndRouteList)
        val meta = searchData.meta
        meta.airlineList = getAirlines(journeyAndRouteList)
        return meta
    }

    private fun generateJourneyAndRoutes(journeyResponse: FlightSearchData,
                                         includedList: List<FlightSearchIncluded>,
                                         isReturnTrip: Boolean): JourneyAndRoutes =
            journeyResponse.routes.map {
                getRouteAirlineByIdAndAirports(it, includedList)
            }.toList().let { routeAirlineAndAirports ->
                getAirports(journeyResponse.departureAirportId, journeyResponse.arrivalAirportId, includedList).let { journeyAirports ->
                    val journeyAirlines = arrayListOf<FlightAirlineModel>()
                    for (routeAirline in routeAirlineAndAirports) {
                        if (!journeyAirlines.contains(routeAirline.first.first)) {
                            journeyAirlines.add(routeAirline.first.first)
                        }
                    }
                    createCompleteJourneyAndRoutes(journeyResponse, journeyAirports,
                            journeyAirlines, routeAirlineAndAirports, isReturnTrip)
                }
            }

    private fun getRouteAirlineByIdAndAirports(route: FlightSearchRoute,
                                               includedList: List<FlightSearchIncluded>)
            : Pair<Pair<FlightAirlineModel, FlightAirlineModel>, Pair<FlightAirportModel, FlightAirportModel>> =
            if (route.operatingAirlineId.isNotEmpty()) {
                getAirlineById(route.airlineId, includedList).let { routeAirline ->
                    getAirlineById(route.operatingAirlineId, includedList).let { routeOperatingAirline ->
                        getAirports(route.departureAirportId, route.arrivalAirportId, includedList).let { routeAirportPair ->
                            Pair(Pair(routeAirline, routeOperatingAirline), routeAirportPair)
                        }
                    }
                }
            } else {
                getAirlineById(route.airlineId, includedList).let { routeAirline ->
                    getAirports(route.departureAirportId, route.arrivalAirportId, includedList).let { routeAirportPair ->
                        Pair(Pair(routeAirline, FlightAirlineModel("", "", "", "")), routeAirportPair)
                    }
                }
            }

}