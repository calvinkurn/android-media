package com.tokopedia.flight.search.data.cloud.single

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 06/04/2020
 */
@Parcelize
class FlightSearchRequestModel(
        @SerializedName("Departure")
        @Expose
        val departure: String = "",
        @SerializedName("Arrival")
        @Expose
        val arrival: String = "",
        @SerializedName("Date")
        @Expose
        val date: String = "",
        @SerializedName("Adult")
        @Expose
        val adult: Int = 0,
        @SerializedName("Child")
        @Expose
        val child: Int = 0,
        @SerializedName("Infant")
        @Expose
        val infant: Int = 0,
        @SerializedName("Class")
        @Expose
        val flightClass: Int = 1,
        @SerializedName("ExcludedAirlines")
        @Expose
        val excludedAirlines: List<String> = arrayListOf(),
        @SerializedName("IPAddress")
        @Expose
        val ipAddress: String = "",
        @SerializedName("RequestID")
        @Expose
        val requestId: String = ""
) : Parcelable