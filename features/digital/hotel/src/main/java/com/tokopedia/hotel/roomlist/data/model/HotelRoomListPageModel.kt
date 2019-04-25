package com.tokopedia.hotel.roomlist.data.model

import android.os.Parcel
import android.os.Parcelable

/**
 * @author by jessica on 22/04/19
 */

data class HotelRoomListPageModel(
        var propertyId: Int = 0,
        var propertyName: String = "",
        var checkIn: String = "",
        var checkInDateFmt: String = "",
        var checkOut: String = "",
        var checkOutDateFmt: String = "",
        var adult: Int = 0,
        var child: Int = 0,
        var room: Int = 0
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(propertyId)
        parcel.writeString(propertyName)
        parcel.writeString(checkIn)
        parcel.writeString(checkInDateFmt)
        parcel.writeString(checkOut)
        parcel.writeString(checkOutDateFmt)
        parcel.writeInt(adult)
        parcel.writeInt(child)
        parcel.writeInt(room)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<HotelRoomListPageModel> {
        override fun createFromParcel(parcel: Parcel): HotelRoomListPageModel {
            return HotelRoomListPageModel(parcel)
        }

        override fun newArray(size: Int): Array<HotelRoomListPageModel?> {
            return arrayOfNulls(size)
        }
    }

}