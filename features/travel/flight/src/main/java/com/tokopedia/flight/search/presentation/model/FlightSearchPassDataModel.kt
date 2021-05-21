package com.tokopedia.flight.search.presentation.model

import android.os.Parcelable
import com.tokopedia.flight.airportv2.presentation.model.FlightAirportModel
import com.tokopedia.flight.homepage.presentation.model.FlightClassModel
import com.tokopedia.flight.homepage.presentation.model.FlightPassengerModel
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 25/06/2020
 */
@Parcelize
data class FlightSearchPassDataModel(var departureDate: String = "",
                                     var returnDate: String = "",
                                     var isOneWay: Boolean = true,
                                     var flightPassengerModel: FlightPassengerModel = FlightPassengerModel(),
                                     var departureAirport: FlightAirportModel = FlightAirportModel(),
                                     var arrivalAirport: FlightAirportModel = FlightAirportModel(),
                                     var flightClass: FlightClassModel = FlightClassModel(),
                                     var linkUrl: String = "",
                                     var searchRequestId: String = "") : Parcelable {

    fun getDate(isReturning: Boolean): String =
            if (isReturning) returnDate else departureDate

    fun getDepartureAirport(isReturning: Boolean): String {
        return if(isReturning){
            if(!arrivalAirport.airportCode.isNullOrEmpty()) arrivalAirport.airportCode else arrivalAirport.cityCode
        } else {
            if(!departureAirport.airportCode.isNullOrEmpty()) departureAirport.airportCode else departureAirport.cityCode
        }
    }

    fun getArrivalAirport(isReturning: Boolean): String {
        return if(isReturning){
            if(!departureAirport.airportCode.isNullOrEmpty()) departureAirport.airportCode else departureAirport.cityCode
        } else {
            if(!arrivalAirport.airportCode.isNullOrEmpty()) arrivalAirport.airportCode else arrivalAirport.cityCode
        }
    }
}