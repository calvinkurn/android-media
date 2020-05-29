package com.tokopedia.flight.airportv2.data.source.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 06/03/19.
 */
class FlightAirportSuggestionEntity(
        @SerializedName("airports")
        @Expose
        val airports : List<FlightAirportDetailEntity> = ArrayList(),
        @SerializedName("cityName")
        @Expose
        val cityName : List<FlightAirportInfoEntity>,
        @SerializedName("code")
        @Expose
        val code : String = "",
        @SerializedName("countryID")
        @Expose
        val countryId : String = "",
        @SerializedName("countryName")
        @Expose
        val countryName : List<FlightAirportInfoEntity>,
        @SerializedName("name")
        @Expose
        val name : List<FlightAirportInfoEntity>)