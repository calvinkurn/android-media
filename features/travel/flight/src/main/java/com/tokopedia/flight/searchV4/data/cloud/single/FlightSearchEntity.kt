package com.tokopedia.flight.searchV4.data.cloud.single

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 06/04/2020
 */

class FlightSearchEntity(
        @SerializedName("data")
        @Expose
        var data: List<FlightSearchData> = arrayListOf(),
        @SerializedName("error")
        @Expose
        var error: List<FlightSearchErrorEntity> = arrayListOf(),
        @SerializedName("meta")
        @Expose
        var meta: FlightSearchMetaEntity = FlightSearchMetaEntity(),
        @SerializedName("included")
        @Expose
        val included: List<FlightSearchIncluded> = arrayListOf()
) {
    class Response(
            @SerializedName("flightSearch")
            @Expose
            val flightSearch: FlightSearchEntity = FlightSearchEntity()
    )
}

class FlightSearchData(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("term")
        @Expose
        val term: String = "",
        @SerializedName("hasFreeRapidTest")
        @Expose
        val hasFreeRapidTest: Boolean = false,
        @SerializedName("isSeatDistancing")
        @Expose
        val isSeatDistancing: Boolean = false,
        @SerializedName("departureAirportID")
        @Expose
        val departureAirportId: String = "",
        @SerializedName("departureTime")
        @Expose
        val departureTime: String = "",
        @SerializedName("departureTimeInt")
        @Expose
        val departureTimeInt: Int = 0,
        @SerializedName("arrivalAirportID")
        @Expose
        val arrivalAirportId: String = "",
        @SerializedName("arrivalTime")
        @Expose
        val arrivalTime: String = "",
        @SerializedName("arrivalTimeInt")
        @Expose
        val arrivalTimeInt: Int = 0,
        @SerializedName("totalTransit")
        @Expose
        val totalTransit: Int = 0,
        @SerializedName("addDayArrival")
        @Expose
        val addDayArrival: Int = 0,
        @SerializedName("totalStop")
        @Expose
        val totalStop: Int = 0,
        @SerializedName("duration")
        @Expose
        val duration: String = "",
        @SerializedName("durationMinute")
        @Expose
        val durationMinute: Int = 0,
        @SerializedName("durationLong")
        @Expose
        val durationLong: String = "",
        @SerializedName("total")
        @Expose
        val totalPrice: String = "",
        @SerializedName("totalNumeric")
        @Expose
        val totalPriceNumeric: Int = 0,
        @SerializedName("beforeTotal")
        @Expose
        val beforeTotalPrice: String = "",
        @SerializedName("showSpecialPriceTag")
        @Expose
        val showSpecialPriceTag: Boolean = false,
        @SerializedName("routes")
        @Expose
        val routes: List<FlightSearchRoute> = arrayListOf(),
        @SerializedName("fare")
        @Expose
        val fare: FlightSearchFare = FlightSearchFare()
)

class FlightSearchRoute(
        @SerializedName("airlineID")
        @Expose
        val airlineId: String = "",
        @SerializedName("departureAirportID")
        @Expose
        val departureAirportId: String = "",
        @SerializedName("departureTime")
        @Expose
        val departureTime: String = "",
        @SerializedName("arrivalAirportID")
        @Expose
        val arrivalAirportId: String = "",
        @SerializedName("arrivalTime")
        @Expose
        val arrivalTime: String = "",
        @SerializedName("duration")
        @Expose
        val duration: String = "",
        @SerializedName("layover")
        @Expose
        val layover: String = "",
        @SerializedName("flightNumber")
        @Expose
        val flightNumber: String = "",
        @SerializedName("refundable")
        @Expose
        val refundable: Boolean = false,
        @SerializedName("stop")
        @Expose
        val stop: Int = 0,
        @SerializedName("operatingAirlineID")
        @Expose
        val operatingAirlineId: String = "",
        @SerializedName("amenities")
        @Expose
        val amenities: List<FlightSearchAmenity> = arrayListOf(),
        @SerializedName("stopDetail")
        @Expose
        val stopDetails: List<FlightSearchStopDetail> = arrayListOf(),
        @SerializedName("infos")
        @Expose
        val infos: List<FlightSearchInfo> = arrayListOf()
)

/*

 */

class FlightSearchAmenity(
        @SerializedName("icon")
        @Expose
        val icon: String = "",
        @SerializedName("label")
        @Expose
        val label: String = ""
)

class FlightSearchStopDetail(
        @SerializedName("code")
        @Expose
        val code: String = "",
        @SerializedName("city")
        @Expose
        val city: String = ""
)

class FlightSearchFare(
        @SerializedName("adult")
        @Expose
        val adult: String = "",
        @SerializedName("child")
        @Expose
        val child: String = "",
        @SerializedName("infant")
        @Expose
        val infant: String = "",
        @SerializedName("adultNumeric")
        @Expose
        val adultNumeric: Int = 0,
        @SerializedName("childNumeric")
        @Expose
        val childNumeric: Int = 0,
        @SerializedName("infantNumeric")
        @Expose
        val infantNumeric: Int = 0
)

class FlightSearchInfo(
        @SerializedName("label")
        @Expose
        val label: String = "",
        @SerializedName("value")
        @Expose
        val value: String = ""
)

class FlightSearchIncluded(
        @SerializedName("type")
        @Expose
        val type: String = "",
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: Attribute = Attribute()
) {
    class Attribute(
            @SerializedName("name")
            @Expose
            val name: String = "",
            @SerializedName("shortName")
            @Expose
            val shortName: String = "",
            @SerializedName("logo")
            @Expose
            val logo: String = "",
            @SerializedName("city")
            @Expose
            val city: String = ""
    )
}