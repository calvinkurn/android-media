package com.tokopedia.flight.searchV2.data.repository.mapper

import android.text.TextUtils
import com.google.gson.Gson
import com.tokopedia.flight_dbflow.FlightAirlineDB
import com.tokopedia.flight_dbflow.FlightAirportDB
import com.tokopedia.flight.search.data.cloud.model.response.Attributes
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData
import com.tokopedia.flight.search.data.cloud.model.response.Route
import com.tokopedia.flight.search.view.model.filter.RefundableEnum
import com.tokopedia.flight.searchV2.data.db.FlightComboTable
import com.tokopedia.flight.searchV2.data.db.FlightJourneyTable
import com.tokopedia.flight.searchV2.data.db.FlightRouteTable
import com.tokopedia.flight.searchV2.data.db.JourneyAndRoutes
import javax.inject.Inject

/**
 * Created by Rizky on 17/10/18.
 */
class FlightSearchMapper @Inject constructor() {

    fun createCompleteJourneyAndRoutes(journeyResponse: FlightSearchData,
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
                    fare.adult,
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

    fun createJourneyWithCombo(journey: FlightJourneyTable, flightComboTable: FlightComboTable): FlightJourneyTable {
        with(flightComboTable) {
            journey.isBestPairing = isBestPairing
            journey.comboId = comboId
            if (!journey.isReturn) {
                journey.adultCombo = onwardAdultPrice
                journey.childCombo = onwardChildPrice
                journey.infantCombo = onwardInfantPrice
                journey.adultNumericCombo = onwardAdultPriceNumeric
                journey.childNumericCombo = onwardChildPriceNumeric
                journey.infantNumericCombo = onwardInfantPriceNumeric
                journey.sortPrice = onwardAdultPrice
                journey.sortPriceNumeric = onwardAdultPriceNumeric
            } else {
                journey.adultCombo = returnAdultPrice
                journey.childCombo = returnChildPrice
                journey.infantCombo = returnInfantPrice
                journey.adultNumericCombo = returnAdultPriceNumeric
                journey.childNumericCombo = returnChildPriceNumeric
                journey.infantNumericCombo = returnInfantPriceNumeric
                journey.sortPrice = returnAdultPrice
                journey.sortPriceNumeric = returnAdultPriceNumeric
            }
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

    fun isRefundable(routes: List<Route>): RefundableEnum {
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

    fun getAirlines(journeyAndRoutesList: List<JourneyAndRoutes>): List<String> {
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

}