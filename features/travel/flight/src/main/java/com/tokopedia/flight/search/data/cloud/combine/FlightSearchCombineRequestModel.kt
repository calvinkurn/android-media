package com.tokopedia.flight.search.data.cloud.combine

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 14/04/2020
 */
@Parcelize
class FlightCombineRequestModel(
        @SerializedName("route")
        @Expose
        val routes: List<FlightCombineRouteRequest> = arrayListOf(),
        @SerializedName("adult")
        @Expose
        val adult: Int = 1,
        @SerializedName("child")
        @Expose
        val child: Int = 0,
        @SerializedName("infant")
        @Expose
        val infant: Int = 0,
        @SerializedName("class")
        @Expose
        val flightClass: Int = 1,
        @SerializedName("ipAddress")
        @Expose
        val ipAddress: String = "",
        @SerializedName("requestID")
        @Expose
        val requestId: String = "") : Parcelable

@Parcelize
class FlightCombineRouteRequest(
        @SerializedName("departure")
        @Expose
        val departure: String = "",
        @SerializedName("arrival")
        @Expose
        val arrival: String = "",
        @SerializedName("date")
        @Expose
        val date: String = "") : Parcelable