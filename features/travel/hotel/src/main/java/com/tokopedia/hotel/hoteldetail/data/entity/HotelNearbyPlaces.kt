package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 23/06/21
 */
data class HotelNearbyPlaces(
        @SerializedName("header")
        @Expose
        val header: String = "",
        @SerializedName("icon")
        @Expose
        val icon: String = "",
        @SerializedName("places")
        @Expose
        val places: List<HotelItemNearbyPlace> = listOf(),
        @SerializedName("type")
        @Expose
        val type: String = ""
)