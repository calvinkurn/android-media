package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-10-03
 */

data class HotelCrossSellingRequest (
        @SerializedName("cityID")
        val cityId: Int = 0,

        @SerializedName("checkIn")
        val checkIn: String = "",

        @SerializedName("checkOut")
        val checkOut: String = "",

        @SerializedName("location")
        val location: Location = Location()
) {
    data class Location(
            @SerializedName("latitude")
            val latitude: String = "",

            @SerializedName("longitude")
            val longitude: String = ""
    )
}