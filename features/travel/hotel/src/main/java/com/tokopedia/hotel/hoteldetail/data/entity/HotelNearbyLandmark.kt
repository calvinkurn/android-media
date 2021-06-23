package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HotelNearbyLandmark(
    @SerializedName("information")
    @Expose
    val information: String = "",
    @SerializedName("result")
    @Expose
    val result: List<HotelNearbyPlaces> = listOf()
)