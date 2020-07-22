package com.tokopedia.flight.cancellationV2.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 21/07/2020
 */
class FlightCancellationEstimateEntity(
        @SerializedName("details")
        @Expose
        val details: List<FlightCancellationEstimateDetail> = arrayListOf(),
        @SerializedName("totalValue")
        @Expose
        val totalValue: String = "",
        @SerializedName("totalValueNumeric")
        @Expose
        val totalValueNumeric: Long = 0
) {
    class Response(@SerializedName("flightEstimated")
                   @Expose
                   val flightEstimated: FlightCancellationEstimateEntity = FlightCancellationEstimateEntity())
}

class FlightCancellationEstimateDetail(
        @SerializedName("journeyID")
        @Expose
        val journeyId: Long = 0,
        @SerializedName("passengerID")
        @Expose
        val passengerId: Long = 0,
        @SerializedName("estimatedRefund")
        @Expose
        val estimatedRefund: Long = 0
)