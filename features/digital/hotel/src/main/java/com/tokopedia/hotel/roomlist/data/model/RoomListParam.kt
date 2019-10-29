package com.tokopedia.hotel.roomlist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 15/04/19
 */

data class RoomListParam(
        @SerializedName("propertyID")
        @Expose
        var propertyId: Int = 0,

        @SerializedName("checkIn")
        @Expose
        var checkIn: String = "",

        @SerializedName("checkOut")
        @Expose
        var checkOut: String = "",

        @SerializedName("guest")
        @Expose
        var guest: Guest = Guest(),

        @SerializedName("room")
        @Expose
        var room: Int = 0

) {
    data class Guest(
            @SerializedName("adult")
            @Expose
            var adult: Int = 0,

            @SerializedName("childAge")
            @Expose
            var childAge: List<Int> = listOf()
    )
}