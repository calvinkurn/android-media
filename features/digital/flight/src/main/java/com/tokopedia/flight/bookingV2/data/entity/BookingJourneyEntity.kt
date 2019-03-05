package com.tokopedia.flight.bookingV2.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.flight.orderlist.data.cloud.entity.RouteEntity

/**
 * @author by furqan on 05/03/19
 */
class BookingJourneyEntity(
        @SerializedName("id")
        @Expose
        var id: String,
        @SerializedName("status")
        @Expose
        var status: Int,
        @SerializedName("departure_airport_id")
        @Expose
        var departureId: String,
        @SerializedName("departure_airport_name")
        @Expose
        var departureAirportName: String,
        @SerializedName("departure_terminal")
        @Expose
        var departureTerminal: String,
        @SerializedName("departure_city_name")
        @Expose
        var departureCityName: String,
        @SerializedName("arrival_airport_id")
        @Expose
        var arrivalId: String,
        @SerializedName("arrival_airport_name")
        @Expose
        var arrivalAirportName: String,
        @SerializedName("arrival_time")
        @Expose
        var arrivalTime: String,
        @SerializedName("arrival_terminal")
        @Expose
        var arrivalTerminal: String,
        @SerializedName("arrival_city_name")
        @Expose
        var arrivalCityName: String,
        @SerializedName("total_transit")
        @Expose
        var totalTransit: Int,
        @SerializedName("total_stop")
        @Expose
        var totalStop: Int,
        @SerializedName("add_day_arrival")
        @Expose
        var addDayarrival: Int,
        @SerializedName("duration")
        @Expose
        var duration: String,
        @SerializedName("duration_minute")
        @Expose
        var durationMinute: Int,
        @SerializedName("total_price_adult")
        @Expose
        var totalPriceAdult: Long,
        @SerializedName("fare")
        @Expose
        var fare: BookingFareEntity,
        @SerializedName("routes")
        @Expose
        var routes: List<RouteEntity>,
        @SerializedName("duration_long")
        @Expose
        var durationLong: String,
        @SerializedName("before_total")
        @Expose
        var beforeTotal: String,
        @SerializedName("total")
        @Expose
        var total: String,
        @SerializedName("total_numeric")
        @Expose
        var totalNumeric: Long,
        @SerializedName("search_term")
        @Expose
        var searchTerm: String,
        @SerializedName("total_per_pax")
        @Expose
        var totalPerPax: BookingTotalPerPax
)