package com.tokopedia.flight.searchV4.data.cloud.combine

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 14/04/2020
 */
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
        val requestId: String = "") : Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.createTypedArrayList(FlightCombineRouteRequest),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readString(),
                parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeTypedList(routes)
                parcel.writeInt(adult)
                parcel.writeInt(child)
                parcel.writeInt(infant)
                parcel.writeInt(flightClass)
                parcel.writeString(ipAddress)
                parcel.writeString(requestId)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<FlightCombineRequestModel> {
                override fun createFromParcel(parcel: Parcel): FlightCombineRequestModel {
                        return FlightCombineRequestModel(parcel)
                }

                override fun newArray(size: Int): Array<FlightCombineRequestModel?> {
                        return arrayOfNulls(size)
                }
        }

}

class FlightCombineRouteRequest(
        @SerializedName("departure")
        @Expose
        val departure: String = "",
        @SerializedName("arrival")
        @Expose
        val arrival: String = "",
        @SerializedName("date")
        @Expose
        val date: String = "") : Parcelable {

        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.readString(),
                parcel.readString()) {}

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(departure)
                parcel.writeString(arrival)
                parcel.writeString(date)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<FlightCombineRouteRequest> {
                override fun createFromParcel(parcel: Parcel): FlightCombineRouteRequest {
                        return FlightCombineRouteRequest(parcel)
                }

                override fun newArray(size: Int): Array<FlightCombineRouteRequest?> {
                        return arrayOfNulls(size)
                }
        }

}