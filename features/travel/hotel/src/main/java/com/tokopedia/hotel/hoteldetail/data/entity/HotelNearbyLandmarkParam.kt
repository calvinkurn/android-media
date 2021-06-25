package com.tokopedia.hotel.hoteldetail.data.entity


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 25/06/21
 */
data class HotelNearbyLandmarkParam(
        @SerializedName("filter")
        @Expose
        val filter: FilterNearbyLandmark = FilterNearbyLandmark(),
        @SerializedName("template")
        @Expose
        val template: String = ""
) {
    data class FilterNearbyLandmark(
            @SerializedName("propertyID")
            @Expose
            val propertyID: Long = 0
    )
}