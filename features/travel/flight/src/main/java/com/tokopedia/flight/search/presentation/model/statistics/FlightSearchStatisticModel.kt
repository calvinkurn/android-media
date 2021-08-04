package com.tokopedia.flight.search.presentation.model.statistics

import com.tokopedia.flight.search.presentation.model.FlightAirlineModel

/**
 * Created by User on 10/30/2017.
 */
class FlightSearchStatisticModel(val minPrice: Int,
                                 val maxPrice: Int,
                                 val minDuration: Int,
                                 val maxDuration: Int,
                                 val transitTypeStatList: List<TransitStat>,
                                 val airlineStatList: List<AirlineStat>,
                                 val departureTimeStatList: List<DepartureStat>,
                                 val arrivalTimeStatList: List<DepartureStat>,
                                 val refundableTypeStatList: List<RefundableStat>,
                                 val isHaveSpecialPrice: Boolean,
                                 val isHaveBaggage: Boolean,
                                 val isHaveInFlightMeal: Boolean,
                                 val isHasFreeRapidTest: Boolean,
                                 val isSeatDistancing: Boolean) {

    fun getAirline(airlineID: String): FlightAirlineModel {
        val airlineStatList: List<AirlineStat> = airlineStatList
        var i = 0
        val sizei = airlineStatList.size
        while (i < sizei) {
            val flightAirlineDB = airlineStatList[i].airlineDB
            if (airlineID == flightAirlineDB.id) {
                return flightAirlineDB
            }
            i++
        }
        return FlightAirlineModel(airlineID, "", "", "")
    }

}