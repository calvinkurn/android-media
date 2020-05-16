package com.tokopedia.flight.search.presentation.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Rizky on 23/10/18.
 */
data class FlightAirlineModel(val id: String, val name: String, val shortName: String, val logo: String): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(shortName)
        parcel.writeString(logo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlightAirlineModel> {
        override fun createFromParcel(parcel: Parcel): FlightAirlineModel {
            return FlightAirlineModel(parcel)
        }

        override fun newArray(size: Int): Array<FlightAirlineModel?> {
            return arrayOfNulls(size)
        }
    }

}