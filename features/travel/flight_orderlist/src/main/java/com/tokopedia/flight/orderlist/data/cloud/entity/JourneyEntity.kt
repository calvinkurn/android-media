package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JourneyEntity(
        @SerializedName("id")
        @Expose
        val id: Long = 0,
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("departure_id")
        @Expose
        val departureAirportId: String = "",
        @SerializedName("departure_time")
        @Expose
        val departureTime: String = "",
        @SerializedName("departure_airport_name")
        @Expose
        val departureAirportName: String = "",
        @SerializedName("departure_terminal")
        @Expose
        val departureTerminal: String = "",
        @SerializedName("departure_city_name")
        @Expose
        val departureCityName: String = "",
        @SerializedName("arrival_id")
        @Expose
        val arrivalAirportId: String = "",
        @SerializedName("arrival_time")
        @Expose
        val arrivalTime: String = "",
        @SerializedName("arrival_airport_name")
        @Expose
        val arrivalAirportName: String = "",
        @SerializedName("arrival_terminal")
        @Expose
        val arrivalTerminal: String = "",
        @SerializedName("arrival_city_name")
        @Expose
        val arrivalCityName: String = "",
        @SerializedName("total_transit")
        @Expose
        val totalTransit: Int = 0,
        @SerializedName("total_stop")
        @Expose
        val totalStop: Int = 0,
        @SerializedName("routes")
        @Expose
        val routes: List<RouteEntity>,
        @SerializedName("duration")
        @Expose
        val duration: String = "",
        @SerializedName("add_day_arrival")
        @Expose
        val addDayArrival: String = "")
