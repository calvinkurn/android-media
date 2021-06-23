package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HotelNearbyPlaces(
        @SerializedName("header")
        @Expose
        val header: String = "",
        @SerializedName("icon")
        @Expose
        val icon: String = "",
        @SerializedName("places")
        @Expose
        val places: List<HotelNearbyLandmark.HotelSingleNearbyPlace> = listOf(),
        @SerializedName("type")
        @Expose
        val type: String = ""
)