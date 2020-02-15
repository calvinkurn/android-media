package com.tokopedia.flight.bookingV3.data

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by jessica on 2019-12-11
 */

data class FlightContactData (
        val name: String = "",
        val email: String = "",
        val phone: String = "",
        val country: String = "",
        val countryCode: Int = 62
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(country)
        parcel.writeInt(countryCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlightContactData> {
        override fun createFromParcel(parcel: Parcel): FlightContactData {
            return FlightContactData(parcel)
        }

        override fun newArray(size: Int): Array<FlightContactData?> {
            return arrayOfNulls(size)
        }
    }
}