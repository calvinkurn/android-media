package com.tokopedia.travelcalendar.view.model

import android.os.Parcel
import android.os.Parcelable

class HolidayResult(val id: String,
                    val attributes: HolidayDetail) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(HolidayDetail::class.java.classLoader)) {
    }

    companion object CREATOR : Parcelable.Creator<HolidayResult> {
        override fun createFromParcel(parcel: Parcel): HolidayResult {
            return HolidayResult(parcel)
        }

        override fun newArray(size: Int): Array<HolidayResult?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeParcelable(attributes, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

}

