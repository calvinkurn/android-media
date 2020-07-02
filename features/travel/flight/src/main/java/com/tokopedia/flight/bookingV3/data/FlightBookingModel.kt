package com.tokopedia.flight.bookingV3.data

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.flight.searchV4.presentation.model.FlightPriceModel
import com.tokopedia.flight.searchV4.presentation.model.FlightSearchPassDataModel

/**
 * @author by jessica on 2019-11-05
 */

data class FlightBookingModel (
        var departureId: String = "",
        var returnId: String = "",
        var departureDate: String = "",
        var departureTerm: String = "",
        var returnTerm: String = "",
        var cartId: String = "",
        var isDomestic: Boolean = true,
        var isMandatoryDob: Boolean = false,
        var flightPriceModel: FlightPriceModel = FlightPriceModel(),
        var searchParam: FlightSearchPassDataModel = FlightSearchPassDataModel(),
        var insurances: List<FlightCart.Insurance> = arrayListOf()
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readParcelable(FlightPriceModel::class.java.classLoader),
            parcel.readParcelable(FlightSearchPassDataModel::class.java.classLoader),
            parcel.createTypedArrayList(FlightCart.Insurance.CREATOR)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(departureId)
        parcel.writeString(returnId)
        parcel.writeString(departureDate)
        parcel.writeString(departureTerm)
        parcel.writeString(returnTerm)
        parcel.writeString(cartId)
        parcel.writeByte(if (isDomestic) 1 else 0)
        parcel.writeByte(if (isMandatoryDob) 1 else 0)
        parcel.writeParcelable(flightPriceModel, flags)
        parcel.writeParcelable(searchParam, flags)
        parcel.writeTypedList(insurances)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlightBookingModel> {
        override fun createFromParcel(parcel: Parcel): FlightBookingModel {
            return FlightBookingModel(parcel)
        }

        override fun newArray(size: Int): Array<FlightBookingModel?> {
            return arrayOfNulls(size)
        }
    }


}