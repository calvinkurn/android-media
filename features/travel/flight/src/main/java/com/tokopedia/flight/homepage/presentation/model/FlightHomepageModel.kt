package com.tokopedia.flight.homepage.presentation.model

import android.os.Parcelable
import com.tokopedia.flight.airport.presentation.model.FlightAirportModel
import kotlinx.android.parcel.Parcelize

/**
 * Created by furqan on 06/10/21.
 */
@Parcelize
class FlightHomepageModel : Parcelable, Cloneable {

    var departureDate: String? = null
    var returnDate: String? = null
    var isOneWay = false
    var flightPassengerViewModel: FlightPassengerModel? = null
    var departureAirport: FlightAirportModel? = null
    var arrivalAirport: FlightAirportModel? = null
    var flightClass: FlightClassModel? = null

    public override fun clone(): Any {
        return try {
            super.clone()
        } catch (e: CloneNotSupportedException) {
            e.printStackTrace()
            throw RuntimeException("Failed to Clone FlightHomepageModel")
        }
    }

}