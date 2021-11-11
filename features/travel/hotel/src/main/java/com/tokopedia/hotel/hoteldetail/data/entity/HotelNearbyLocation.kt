package com.tokopedia.hotel.hoteldetail.data.entity


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 23/06/21
 */
data class HotelNearbyLocation(
        @SerializedName("lat")
        @Expose
        val lat: Double = 0.0,
        @SerializedName("lon")
        @Expose
        val lon: Double = 0.0
)