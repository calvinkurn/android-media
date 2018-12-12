package com.tokopedia.travelcalendar.view.model

import android.os.Parcel
import android.os.Parcelable

class HolidayResult(var id: String? = null,
    var attributes: HolidayDetail? = null) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(HolidayDetail::class.java.classLoader)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeParcelable(attributes, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HolidayResult> {
        override fun createFromParcel(parcel: Parcel): HolidayResult {
            return HolidayResult(parcel)
        }

        override fun newArray(size: Int): Array<HolidayResult?> {
            return arrayOfNulls(size)
        }
    }
}

