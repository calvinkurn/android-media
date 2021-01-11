package com.tokopedia.travel_slice.hotel.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 14/10/20
 */

class HotelData(
        @SerializedName("id")
        @Expose
        val id: Long = 0,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("roomPrice")
        @Expose
        val roomPrice: List<PropertyPrice> = listOf(),

        @SerializedName("location")
        @Expose
        val location: Location = Location(),

        @SerializedName("image")
        @Expose
        val image: List<Image> = listOf()

) {
    data class PropertyPrice(
            @SerializedName("totalPrice")
            @Expose
            val totalPrice: String = ""
    )

    data class Image(
            @SerializedName("urlMax300")
            @Expose
            val urlMax300: String = ""
    )

    data class Location(
            @SerializedName("cityName")
            @Expose
            val cityName: String = ""
    )
}