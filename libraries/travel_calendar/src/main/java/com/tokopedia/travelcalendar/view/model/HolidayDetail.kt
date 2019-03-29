package com.tokopedia.travelcalendar.view.model

import android.os.Parcel
import android.os.Parcelable

import java.util.Date


class HolidayDetail constructor(val date: String, val label: String,
                    val dateHoliday: Date) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            TODO("dateHoliday")) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(label)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HolidayDetail> {
        override fun createFromParcel(parcel: Parcel): HolidayDetail {
            return HolidayDetail(parcel)
        }

        override fun newArray(size: Int): Array<HolidayDetail?> {
            return arrayOfNulls(size)
        }
    }
}
