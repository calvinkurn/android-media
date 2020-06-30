package com.tokopedia.flight.searchV4.data.cloud.single

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 06/04/2020
 */
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(departure)
        parcel.writeString(arrival)
        parcel.writeString(date)
        parcel.writeInt(adult)
        parcel.writeInt(child)
        parcel.writeInt(infant)
        parcel.writeInt(flightClass)
        parcel.writeStringList(excludedAirlines)
        parcel.writeString(ipAddress)
        parcel.writeString(requestId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlightSearchRequestModel> {
        override fun createFromParcel(parcel: Parcel): FlightSearchRequestModel {
            return FlightSearchRequestModel(parcel)
        }

        override fun newArray(size: Int): Array<FlightSearchRequestModel?> {
            return arrayOfNulls(size)
        }
    }

}