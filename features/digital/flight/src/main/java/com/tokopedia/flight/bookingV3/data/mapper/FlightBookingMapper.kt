package com.tokopedia.flight.bookingV3.data.mapper

import com.tokopedia.flight.bookingV3.data.FlightCart
import com.tokopedia.flight.bookingV3.data.FlightCartViewEntity

/**
 * @author by jessica on 2019-10-29
 */

class FlightBookingMapper {

    companion object {
        fun mapToFlightCartView(flightCart: FlightCart): FlightCartViewEntity {
            val journies: MutableList<FlightCartViewEntity.JourneySummary> = arrayListOf()
            for (item in flightCart.cartData.flight.journeys) {

                var newJourney = FlightCartViewEntity.JourneySummary()
                var airlineName = ""
                var departureCityName = ""
                var arrivalCityName = ""
                var isMultipleAirline = false

                for ((position, route) in item.routes.withIndex()) {
                    for (includedItem in flightCart.included) {
                        if (route.airlineId.equals(includedItem.id, true)) {
                            if (airlineName.equals(includedItem.attributes.name, true)) break
                            if (position == 0) newJourney.airlineLogo = includedItem.attributes.logo else {
                                airlineName += " + "
                                newJourney.isMultipleAirline = true
                            }
                            airlineName += includedItem.attributes.name
                            break
                        }
                    }
                }

                for (includedItem in flightCart.included) {
                    if (item.departureAirportId.equals(includedItem.id, true)) {
                        departureCityName = includedItem.attributes.city
                        break
                    } else if (item.arrivalAirportId.equals(includedItem.id, true)) {
                        arrivalCityName = includedItem.attributes.city
                    }
                }

                newJourney.airline = airlineName
                newJourney.routeName = String.format("%s (%s) → %s (%s)", departureCityName, item.departureAirportId,
                        arrivalCityName, item.arrivalAirportId)
                newJourney.date
                newJourney.isRefundable = item.routes[0].refundable
                newJourney.isDirectFlight = item.routes.size == 1
                newJourney.journeyDetailUrl //TOBEFILLED

                journies.add(newJourney)
            }
            return FlightCartViewEntity(journeySummaries = journies)
        }
    }
}