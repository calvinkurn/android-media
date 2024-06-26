package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 23/06/21
 */
data class HotelItemNearbyPlace(
        @SerializedName("distance")
        @Expose
        val distance: String = "",
        @SerializedName("icon")
        @Expose
        val icon: String = "",
        @SerializedName("location")
        @Expose
        val location: HotelNearbyLocation = HotelNearbyLocation(),
        @SerializedName("name")
        @Expose
        val name: String = ""
)