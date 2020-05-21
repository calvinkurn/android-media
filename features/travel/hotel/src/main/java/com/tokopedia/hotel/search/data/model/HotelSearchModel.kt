package com.tokopedia.hotel.search.data.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by jessica on 22/04/20
 */
data class HotelSearchModel(
        var checkIn: String = "",
        var checkOut: String = "",
        var id: Long = 0,
        var name: String = "",
        var type: String = "",
        var room: Int = 1,
        var adult: Int = 1,
        var lat: Float = 0f,
        var long: Float = 0f
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readFloat()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(checkIn)
        parcel.writeString(checkOut)
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(type)
        parcel.writeInt(room)
        parcel.writeInt(adult)
        parcel.writeFloat(lat)
        parcel.writeFloat(long)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HotelSearchModel> {
        override fun createFromParcel(parcel: Parcel): HotelSearchModel {
            return HotelSearchModel(parcel)
        }

        override fun newArray(size: Int): Array<HotelSearchModel?> {
            return arrayOfNulls(size)
        }
    }

}