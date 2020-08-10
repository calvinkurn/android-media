package com.tokopedia.hotel.roomlist.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by jessica on 22/04/19
 */
@Parcelize
data class HotelRoomListPageModel(
        var propertyId: Long = 0,
        var propertyName: String = "",
        var checkIn: String = "",
        var checkInDateFmt: String = "",
        var checkOut: String = "",
        var checkOutDateFmt: String = "",
        var adult: Int = 0,
        var child: Int = 0,
        var room: Int = 0,
        var destinationType: String = "",
        var destinationName: String = ""
) : Parcelable