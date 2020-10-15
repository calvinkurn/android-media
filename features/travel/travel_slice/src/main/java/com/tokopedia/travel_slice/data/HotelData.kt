package com.tokopedia.travel_slice.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 14/10/20
 */

class HotelData(
        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("address")
        @Expose
        val address: String = "",

        @SerializedName("roomPrice")
        @Expose
        val roomPrice: List<PropertyPrice> = listOf(),

        @SerializedName("star")
        @Expose
        val star: Int = 0,

        @SerializedName("review")
        @Expose
        val review: Review = Review(),

        @SerializedName("location")
        @Expose
        val location: Location = Location()
) {
    data class PropertyPrice(
            @SerializedName("totalPrice")
            @Expose
            val totalPrice: String = ""
    )

    data class Review(
            @SerializedName("reviewScore")
            @Expose
            val reviewScore: Float = 0.0f
    )

    data class Location(
            @SerializedName("description")
            @Expose
            val description: String = ""
    )
}