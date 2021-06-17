package com.tokopedia.flight.cancellation.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 21/07/2020
 */
class FlightCancellationRequestEntity(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("details")
        @Expose
        val details: List<FlightCancellationRequestDetail> = arrayListOf()
) {
    class Response(@SerializedName("flightCancelRequest")
                   @Expose
                   val flightCancelRequest: FlightCancellationRequestEntity = FlightCancellationRequestEntity())
}

class FlightCancellationRequestDetail(
        @SerializedName("id")
        @Expose
        val id: Long = 0,
        @SerializedName("journeyID")
        @Expose
        val journeyId: Long = 0,
        @SerializedName("passengerID")
        @Expose
        val passengerId: Long = 0)