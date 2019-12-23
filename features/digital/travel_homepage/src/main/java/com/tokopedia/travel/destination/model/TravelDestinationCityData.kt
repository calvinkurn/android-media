package com.tokopedia.travel.destination.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 2019-12-23
 */

data class TravelDestinationCityData (
        @SerializedName("CityID")
        @Expose
        val cityId: String = "",

        @SerializedName("CityName")
        @Expose
        val cityName: String = ""
) {
    data class Response(
            @SerializedName("TravelDestinationCityData")
            @Expose
            val response: TravelDestinationCityData = TravelDestinationCityData()
    )
}