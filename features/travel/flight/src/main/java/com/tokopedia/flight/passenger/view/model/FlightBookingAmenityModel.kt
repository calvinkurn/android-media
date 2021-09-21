package com.tokopedia.flight.passenger.view.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.flight.passenger.view.adapter.FlightAmenityAdapterTypeFactory
import kotlinx.android.parcel.Parcelize

/**
 * Created by furqn on 06/10/21.
 */
@Parcelize
data class FlightBookingAmenityModel(
        var id: String = "",
        var title: String = "",
        var price: String = "",
        var priceNumeric: Long = 0,
        var departureId: String = "",
        var arrivalId: String = "",
        var amenityType: Int = 0)
    : Parcelable, Visitable<FlightAmenityAdapterTypeFactory> {

    override fun toString(): String {
        return title
    }

    override fun equals(other: Any?): Boolean {
        return other is FlightBookingAmenityModel &&
                other.id.equals(id, ignoreCase = true)
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun type(typeFactory: FlightAmenityAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}