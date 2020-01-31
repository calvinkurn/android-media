package com.tokopedia.flight.airport.data.source.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 05/03/19.
 */
class FlightPopularCityEntity(
        @SerializedName("countryID")
        @Expose
        val countryId: String = "",
        @SerializedName("countryName")
        @Expose
        val countryName: String = "",
        @SerializedName("cityID")
        @Expose
        val cityId: String = "",
        @SerializedName("airportCode")
        @Expose
        val airportCode: String = "",
        @SerializedName("cityCode")
        @Expose
        val cityCode: String = "",
        @SerializedName("cityName")
        @Expose
        val cityName: String = "",
        @SerializedName("airportName")
        @Expose
        val airportName: String = "")
