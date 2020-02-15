package com.tokopedia.flight.detail.view.model

import android.os.Parcel
import android.os.Parcelable


/**
 * @author by furqan on 29/10/2019
 */
class FlightDetailRouteInfoViewModel() : Parcelable {
    var label: String? = null
    var value: String? = null

    constructor(parcel: Parcel) : this() {
        label = parcel.readString()
        value = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(label)
        parcel.writeString(value)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlightDetailRouteInfoViewModel> {
        override fun createFromParcel(parcel: Parcel): FlightDetailRouteInfoViewModel {
            return FlightDetailRouteInfoViewModel(parcel)
        }

        override fun newArray(size: Int): Array<FlightDetailRouteInfoViewModel?> {
            return arrayOfNulls(size)
        }
    }
}