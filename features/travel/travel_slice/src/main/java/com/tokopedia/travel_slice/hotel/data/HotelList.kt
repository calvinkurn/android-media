package com.tokopedia.travel_slice.hotel.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 14/10/20
 */

data class HotelList(
        @SerializedName("propertyList")
        @Expose
        val propertyList: List<HotelData> = listOf()
) {
    data class Response(
            @SerializedName("propertySearch")
            @Expose
            val propertySearch: HotelList = HotelList()
    )
}