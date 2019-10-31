package com.tokopedia.flight.bookingV3.data.mapper

import com.tokopedia.common.travel.utils.TravelDateUtil
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
                    } else if (item.arrivalAirportId.equals(includedItem.id, true)) {
                        arrivalCityName = includedItem.attributes.city
                    }
                }

                var departureDateString = TravelDateUtil.dateToString(TravelDateUtil.EEE_DD_MMM_YY, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, item.departureTime))
                departureDateString += String.format(" • %s-%s", TravelDateUtil.dateToString(TravelDateUtil.HH_MM, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, item.departureTime)),
                        TravelDateUtil.dateToString(TravelDateUtil.HH_MM, TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD_T_HH_MM_SS_Z, item.arrivalTime)))
                if (item.addDayArrival > 0) departureDateString += String.format(" (+%d hari)", item.addDayArrival)

                newJourney.airline = airlineName
                newJourney.routeName = String.format("%s (%s) → %s (%s)", departureCityName, item.departureAirportId,
                        arrivalCityName, item.arrivalAirportId)
                newJourney.isRefundable = item.routes[0].refundable
                newJourney.transit = item.routes.size - 1
                newJourney.journeyDetailUrl //TOBEFILLED
                newJourney.date = departureDateString

                journies.add(newJourney)
            }
            return FlightCartViewEntity(journeySummaries = journies)
        }
    }
}