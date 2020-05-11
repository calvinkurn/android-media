package com.tokopedia.flight.search.data.repository.mapper

import com.google.gson.Gson
import com.tokopedia.flight.search.data.api.single.response.*
import com.tokopedia.flight.search.data.db.FlightComboTable
import com.tokopedia.flight.search.data.db.FlightJourneyTable
import com.tokopedia.flight.search.data.db.FlightRouteTable
import com.tokopedia.flight.search.data.db.JourneyAndRoutes
import com.tokopedia.flight.search.presentation.model.FlightAirlineModel
import com.tokopedia.flight.search.presentation.model.FlightAirportModel
import com.tokopedia.flight.search.presentation.model.filter.RefundableEnum
import javax.inject.Inject

/**
 * Created by Rizky on 17/10/18.
 */
class FlightSearchMapper @Inject constructor() {

    fun createCompleteJourneyAndRoutes(journeyResponse: FlightSearchData,
                                       journeyAirports: Pair<FlightAirportModel, FlightAirportModel>,
                                       journeyAirlines: List<FlightAirlineModel>,
                                       routesAirlinesAndAirports: List<Pair<Pair<FlightAirlineModel, FlightAirlineModel>, Pair<FlightAirportModel, FlightAirportModel>>>,
                                       isReturn: Boolean): JourneyAndRoutes {
        val isRefundable = isRefundable(journeyResponse.attributes.routes)
        val flightJourneyTable = createFlightJourneyTable(journeyResponse.id, journeyResponse.attributes,
                isRefundable, isReturn)
        val routesAirlines = arrayListOf<FlightAirlineModel>()
        val routesOperatingAirlines = arrayListOf<FlightAirlineModel>()
        val routesAirports = arrayListOf<Pair<FlightAirportModel, FlightAirportModel>>()
        for (routeAirlineAndAirport in routesAirlinesAndAirports) {
            routesAirlines.add(routeAirlineAndAirport.first.first)
            routesOperatingAirlines.add(routeAirlineAndAirport.first.second)
            routesAirports.add(Pair(routeAirlineAndAirport.second.first, routeAirlineAndAirport.second.second))
        }
        val completeJourney = createJourneyWithAirportAndAirline(
                flightJourneyTable, journeyAirports, journeyAirlines)
        val routes = createRoutes(journeyResponse.attributes.routes, journeyResponse.id, routesAirports,
                routesAirlines, routesOperatingAirlines)
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
                    isShowSpecialPriceTag,
                    fare.adult,
                    fare.adultNumeric,
                    isReturn,
                    isRefundable,
                    !beforeTotal.isNullOrEmpty(),
                    ""
            )
        }
    }

    private fun createRoutes(routes: List<Route>, journeyId: String,
                             routesAirports: List<Pair<FlightAirportModel, FlightAirportModel>>,
                             routesAirlines: List<FlightAirlineModel>,
                             routesOperatingAirlines: ArrayList<FlightAirlineModel>): List<FlightRouteTable> {
        val gson = Gson()
        return routes.zip(routesAirports).zip(routesAirlines).zip(routesOperatingAirlines).map {
            val (route, pairOfAirport) = it.first.first
            val routeAirline = it.first.second
            val routeOperatingAirline = it.second
            val routeDepartureAirport = pairOfAirport.first
            val routeArrivalAirport = pairOfAirport.second
            with(route) {
                FlightRouteTable(
                        journeyId,
                        airline,
                        routeAirline.name,
                        routeAirline.shortName,
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
                        gson.toJson(stopDetails),
                        routeOperatingAirline.name
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

    fun extractAirportFromIncluded(included: Included<AttributesInc>): FlightAirportModel {
        return if (included.attributes is AttributesAirport) {
            FlightAirportModel(included.id, (included.attributes as AttributesAirport).name,
                    (included.attributes as AttributesAirport).city)
        } else {
            FlightAirportModel("", "", "")
        }
    }

}