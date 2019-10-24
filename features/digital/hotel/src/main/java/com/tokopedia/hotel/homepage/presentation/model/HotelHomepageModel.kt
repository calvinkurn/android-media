package com.tokopedia.hotel.homepage.presentation.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by furqan on 05/04/19
 */
class HotelHomepageModel(var checkInDate: String = "",
                         var checkInDateFmt: String = "",
                         var checkOutDate: String = "",
                         var checkOutDateFmt: String = "",
                         var nightCounter: Long = 0,
                         var roomCount: Int = 1,
                         var adultCount: Int = 1,
                         var locLat: Double = 0.0,
                         var locLong: Double = 0.0,
                         var locName: String = "Bali",
                         var locId: Int = 4712,
                         var locType: String = "region") : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(checkInDate)
        parcel.writeString(checkInDateFmt)
        parcel.writeString(checkOutDate)
        parcel.writeString(checkOutDateFmt)
        parcel.writeLong(nightCounter)
        parcel.writeInt(roomCount)
        parcel.writeInt(adultCount)
        parcel.writeDouble(locLat)
        parcel.writeDouble(locLong)
        parcel.writeString(locName)
        parcel.writeInt(locId)
        parcel.writeString(locType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HotelHomepageModel> {
        override fun createFromParcel(parcel: Parcel): HotelHomepageModel {
            return HotelHomepageModel(parcel)
        }

        override fun newArray(size: Int): Array<HotelHomepageModel?> {
            return arrayOfNulls(size)
        }
    }

}