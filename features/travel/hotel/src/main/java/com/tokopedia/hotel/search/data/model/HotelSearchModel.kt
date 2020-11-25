package com.tokopedia.hotel.search.data.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by jessica on 22/04/20
 */
@Parcelize
data class HotelSearchModel(
        var checkIn: String = "",
        var checkOut: String = "",
        var id: Long = 0,
        var name: String = "",
        var type: String = "",
        var room: Int = 1,
        var adult: Int = 1,
        var lat: Float = 0f,
        var long: Float = 0f,
        var searchType: String = "",
        var searchId: String = ""
) : Parcelable