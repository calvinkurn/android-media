package com.tokopedia.flight.passenger.view.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.passenger.view.adapter.FlightBookingPassengerTypeFactory
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import kotlinx.android.parcel.Parcelize

/**
 * @author by alvarisi on 11/7/17.
 */
@Parcelize
class FlightBookingPassengerModel(
        var passengerLocalId: Int = 0, //passengerLocalNumber
        var type: Int = 0,
        var passengerId: String = "",
        var passengerTitle: String = "",
        var headerTitle: String = "",
        var passengerFirstName: String = "",
        var passengerLastName: String = "",
        var passengerBirthdate: String = "",
        var flightBookingLuggageMetaViewModels: MutableList<FlightBookingAmenityMetaModel> = arrayListOf(),
        var flightBookingMealMetaViewModels: MutableList<FlightBookingAmenityMetaModel> = arrayListOf(),
        var passengerTitleId: Int = 0,
        var passportNumber: String? = null,
        var passportExpiredDate: String? = null,
        var passportNationality: TravelCountryPhoneCode? = null,
        var passportIssuerCountry: TravelCountryPhoneCode? = null
) : Parcelable, Visitable<FlightBookingPassengerTypeFactory> {

    override fun equals(other: Any?): Boolean {
        return other is FlightBookingPassengerModel &&
                other.passengerLocalId == passengerLocalId
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result *= prime * passengerLocalId * type
        return result
    }

    override fun type(typeFactory: FlightBookingPassengerTypeFactory): Int {
        return typeFactory.type(this)
    }

}