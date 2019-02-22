package com.tokopedia.flight.orderlist.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.flight.search.data.api.single.response.StopDetailEntity

class RouteEntity(
        @SerializedName("departure_id")
        @Expose
        val departureAirportCode: String,
        @SerializedName("departure_time")
        @Expose
        val departureTime: String,
        @SerializedName("departure_airport_name")
        @Expose
        val departureAirportName: String,
        @SerializedName("departure_city_name")
        @Expose
        val departureCityName: String,
        @SerializedName("arrival_id")
        @Expose
        val arrivalAirportCode: String,
        @SerializedName("arrival_time")
        @Expose
        val arrivalTime: String,
        @SerializedName("arrival_airport_name")
        @Expose
        val arrivalAirportName: String,
        @SerializedName("arrival_city_name")
        @Expose
        val arrivalCityName: String,
        @SerializedName("airline_id")
        @Expose
        val airlineId: String,
        @SerializedName("airline_name")
        @Expose
        val airlineName: String,
        @SerializedName("airline_logo")
        @Expose
        val airlineLogo: String,
        @SerializedName("operator_airline_id")
        @Expose
        val operatorAirlineId: String,
        @SerializedName("flight_number")
        @Expose
        val flightNumber: String,
        @SerializedName("duration")
        @Expose
        val duration: String,
        @SerializedName("layover")
        @Expose
        val layover: String,
        @SerializedName("layover_minute")
        @Expose
        val layoverMinute: Int,
        @SerializedName("refundable")
        @Expose
        val isRefundable: Boolean,
        @SerializedName("departure_terminal")
        @Expose
        val departureTerminal: String,
        @SerializedName("arrival_terminal")
        @Expose
        val arrivalTerminal: String,
        @SerializedName("free_amenities")
        @Expose
        val freeAmenities: AmenityEntity,
        @SerializedName("pnr")
        @Expose
        val pnr: String,
        @SerializedName("stop")
        @Expose
        val stops: Int,
        @SerializedName("stop_detail")
        @Expose
        val stopDetailEntities: List<StopDetailEntity>)
