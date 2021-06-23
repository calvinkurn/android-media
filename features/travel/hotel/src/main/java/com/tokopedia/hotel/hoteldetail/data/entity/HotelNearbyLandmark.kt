package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 23/06/21
 */
data class HotelNearbyLandmark(
        @SerializedName("result")
        @Expose
        val result: List<HotelNearbyPlaces> = listOf(),

        @SerializedName("information")
        @Expose
        val information: String = ""
)