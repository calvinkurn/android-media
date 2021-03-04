package com.tokopedia.flight.searchV4.domain

import com.google.gson.Gson
import com.tokopedia.flight.searchV4.data.cache.db.FlightComboTable
import com.tokopedia.flight.searchV4.data.cache.db.FlightJourneyTable
import com.tokopedia.flight.searchV4.data.cache.db.FlightRouteTable
import com.tokopedia.flight.searchV4.data.cache.db.JourneyAndRoutes
import com.tokopedia.flight.searchV4.data.cloud.single.FlightSearchData
import com.tokopedia.flight.searchV4.data.cloud.single.FlightSearchIncluded
import com.tokopedia.flight.searchV4.data.cloud.single.FlightSearchRoute
import com.tokopedia.flight.searchV4.presentation.model.FlightAirlineModel
import com.tokopedia.flight.searchV4.presentation.model.FlightAirportModel
import com.tokopedia.flight.searchV4.presentation.model.filter.RefundableEnum

/**
 * @author by furqan on 08/04/2020
 */
class FlightSearchMapper {

    companion object {
        private const val TYPE_AIRLINE = "airline"
        private const val TYPE_AIRPORT = "airport"

        private val mapperInstance = FlightSearchMapper()

        fun createCompleteJourneyAndRoutes(journeyResponse: FlightSearchData,
                                           journeyAirports: Pair<FlightAirportModel, FlightAirportModel>,
                                           journeyAirlines: List<FlightAirlineModel>,
                                           routesAirlinesAndAirports: List<Pair<Pair<FlightAirlineModel, FlightAirlineModel>, Pair<FlightAirportModel, FlightAirportModel>>>,
                                           isReturn: Boolean): JourneyAndRoutes {
            val isRefundable = mapperInstance.isRefundable(journeyResponse.routes)
            val flightJourneyTable = mapperInstance.createFlightJourneyTable(
                    journeyResponse, isRefundable, isReturn)
            val routesAirlines = arrayListOf<FlightAirlineModel>()
            val routesOperatingAirlines = arrayListOf<FlightAirlineModel>()
            val routesAirports = arrayListOf<Pair<FlightAirportModel, FlightAirportModel>>()
            for (item in routesAirlinesAndAirports) {
                routesAirlines.add(item.first.first)
                routesOperatingAirlines.add(item.first.second)
                routesAirports.add(item.second)
            }
            val completeJourney = mapperInstance.createJourneyWithAirportAndAirline(
                    flightJourneyTable, journeyAirports, journeyAirlines)
            val routes = mapperInstance.createRoutes(journeyResponse.routes, journeyResponse.id, routesAirports,
                    routesAirlines, routesOperatingAirlines)
            return JourneyAndRoutes(completeJourney, routes)
        }

        fun getAirlineById(airlineId: String, includedList: List<FlightSearchIncluded>)
                : FlightAirlineModel {
            includedList.firstOrNull { it.type == TYPE_AIRLINE && it.id == airlineId }?.let {
                return FlightAirlineModel(it.id,
                        it.attributes.name,
                        it.attributes.shortName,
                        it.attributes.logo)
            }
            return FlightAirlineModel("", "", "", "")
        }

        fun getAirports(departureAirportId: String, arrivalAirportId: String, includedList: List<FlightSearchIncluded>)
                : Pair<FlightAirportModel, FlightAirportModel> {
            val departureAirport = includedList.firstOrNull {
                it.type == TYPE_AIRPORT && it.id == departureAirportId
            }.let {
                mapperInstance.extractAirportFromIncluded(it)
            }
            val arrivalAirport = includedList.firstOrNull {
                it.type == TYPE_AIRPORT && it.id == arrivalAirportId
            }.let {
                mapperInstance.extractAirportFromIncluded(it)
            }

            return Pair(departureAirport, arrivalAirport)
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
    }

    private fun extractAirportFromIncluded(included: FlightSearchIncluded?): FlightAirportModel {
        included?.let {
            return FlightAirportModel(it.id, it.attributes.name, it.attributes.city)
        }
        return FlightAirportModel("", "", "")
    }

    private fun isRefundable(routes: List<FlightSearchRoute>): RefundableEnum {
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

    private fun createFlightJourneyTable(flightSearchData: FlightSearchData, isRefundable: RefundableEnum,
                                         isReturn: Boolean)
            : FlightJourneyTable {
        with(flightSearchData) {
            return FlightJourneyTable(
                    id,
                    term,
                    hasFreeRapidTest,
                    isSeatDistancing,
                    departureAirportId,
                    "",
                    "",
                    arrivalAirportId,
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
                    totalPrice,
                    "",
                    totalPriceNumeric,
                    0,
                    false,
                    beforeTotalPrice,
                    showSpecialPriceTag,
                    fare.adult,
                    fare.adultNumeric,
                    isReturn,
                    !beforeTotalPrice.isNullOrEmpty(),
                    isRefundable,
                    ""
            )
        }
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

    private fun createRoutes(routes: List<FlightSearchRoute>, journeyId: String,
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
                        airlineId,
                        routeAirline.name,
                        routeAirline.shortName,
                        routeAirline.logo,
                        departureAirportId,
                        routeDepartureAirport.airportName,
                        routeDepartureAirport.cityName,
                        arrivalAirportId,
                        routeArrivalAirport.airportName,
                        routeArrivalAirport.cityName,
                        departureTime,
                        arrivalTime,
                        duration,
                        gson.toJson(infos),
                        layover,
                        flightNumber,
                        refundable,
                        gson.toJson(amenities),
                        stop,
                        gson.toJson(stopDetails),
                        routeOperatingAirline.name
                )
            }
        }
    }

}