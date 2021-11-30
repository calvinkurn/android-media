package com.tokopedia.flight.passenger.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by alvarisi on 11/22/17.
 */
@Parcelize
class FlightBookingAmenityMetaModel(
        var arrivalId: String = "",
        var departureId: String = "",
        var journeyId: String = "",
        var key: String = "",
        var description: String = "",
        var amenities: List<FlightBookingAmenityModel> = arrayListOf()
) : Parcelable {

    override fun hashCode(): Int {
        return key.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is FlightBookingAmenityMetaModel &&
                other.key.equals(key, ignoreCase = true)
    }

}