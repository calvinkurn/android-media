package com.tokopedia.flight.search.data.cloud.single

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 21/05/2021
 */
@Parcelize
data class Route(
        @SerializedName("airline")
        @Expose
        var airline: String = "",
        @SerializedName("departure_airport")
        @Expose
        var departureAirport: String = "",
        @SerializedName("departure_timestamp")
        @Expose
        var departureTimestamp: String = "",
        @SerializedName("arrival_airport")
        @Expose
        var arrivalAirport: String = "",
        @SerializedName("arrival_timestamp")
        @Expose
        var arrivalTimestamp: String = "",
        @SerializedName("duration")
        @Expose
        var duration: String = "",
        @SerializedName("layover")
        @Expose
        var layover: String = "",
        @SerializedName("infos")
        @Expose
        var infos: List<Info> = arrayListOf(),
        @SerializedName("flight_number")
        @Expose
        var flightNumber: String = "",
        @SerializedName("is_refundable")
        @Expose
        var isRefundable: Boolean = false,
        @SerializedName("amenities")
        @Expose
        var amenities: List<Amenity> = arrayListOf(),
        @SerializedName("stop")
        @Expose
        var stops: Int = 0,
        @SerializedName("stop_detail")
        @Expose
        var stopDetails: List<StopDetailEntity> = arrayListOf(),
        var airlineName: String = "",
        var airlineLogo: String = "",
        var departureAirportName: String = "",
        var departureAirportCity: String = "",
        var arrivalAirportName: String = "",
        var arrivalAirportCity: String = "",
        @SerializedName("operating_airline")
        @Expose
        var operatingAirline: String = ""
) : Parcelable