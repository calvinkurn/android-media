package com.tokopedia.hotel.roomlist.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 08/05/19
 */

data class HotelAddCartParam(
        @SerializedName("idempotencyKey")
        @Expose
        var idempotencyKey: String = "",

        @SerializedName("checkIn")
        @Expose
        val checkIn: String = "",

        @SerializedName("checkOut")
        @Expose
        val checkOut: String = "",

        @SerializedName("propertyID")
        @Expose
        val propertyId: Int = 0,

        @SerializedName("rooms")
        @Expose
        val rooms: List<Room> = listOf()
) {
    data class Room(
            @SerializedName("roomID")
            @Expose
            val roomId: String = "",

            @SerializedName("numOfRooms")
            @Expose
            val numOfRooms: Int = 0
    )
}