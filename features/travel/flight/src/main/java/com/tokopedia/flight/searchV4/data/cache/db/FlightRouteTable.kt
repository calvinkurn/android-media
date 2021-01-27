package com.tokopedia.flight.searchV4.data.cache.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Furqan on 05/11/20.
 */
@Entity
class FlightRouteTable(var journeyId: String,
                       var airline: String,
                       var airlineName: String,
                       var airlineShortName: String,
                       var airlineLogo: String,
                       var departureAirport: String,
                       var departureAirportName: String,
                       var departureAirportCity: String,
                       var arrivalAirport: String,
                       var arrivalAirportName: String,
                       var arrivalAirportCity: String,
                       var departureTimestamp: String,
                       var arrivalTimestamp: String,
                       var duration: String,
                       var infos: String,
                       var layover: String,
                       var flightNumber: String,
                       var isRefundable: Boolean,
                       var amenities: String,
                       var stops: Int,
                       var stopDetail: String,
                       var operatingAirline: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

}