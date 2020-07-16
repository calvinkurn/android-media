package com.tokopedia.flight.cancellationV2.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 06/07/2020
 */
data class FlightCancellationPassengerEntity(
        @SerializedName("passengers")
        @Expose
        val passengers: List<Passenger> = arrayListOf(),
        @SerializedName("nonCancellables")
        @Expose
        val nonCancellablePassengers: List<Passenger> = arrayListOf(),
        @SerializedName("reasons")
        @Expose
        val reasons: List<String> = arrayListOf(),
        @SerializedName("formattedReasons")
        @Expose
        val formattedReasons: List<Reason> = arrayListOf(),
        @SerializedName("included")
        @Expose
        val included: List<Included> = arrayListOf()
) {
    class Response(
            @SerializedName("flightCancelPassenger")
            @Expose
            val flightCancelPassenger: FlightCancellationPassengerEntity = FlightCancellationPassengerEntity())

    data class Passenger(
            @SerializedName("relationID")
            @Expose
            val relationId: String = "",
            @SerializedName("journeyID")
            @Expose
            val journeyId: String = "",
            @SerializedName("passengerID")
            @Expose
            val passengerId: String = "",
            @SerializedName("type")
            @Expose
            val type: Int = 0,
            @SerializedName("title")
            @Expose
            val title: Int = 1,
            @SerializedName("firstName")
            @Expose
            val firstName: String = "",
            @SerializedName("lastName")
            @Expose
            val lastName: String = "",
            @SerializedName("dob")
            @Expose
            val dateOfBirth: String = "",
            @SerializedName("nationality")
            @Expose
            val nationality: String = "",
            @SerializedName("passportNo")
            @Expose
            val passportNo: String = "",
            @SerializedName("passportCountry")
            @Expose
            val passportCountry: String = "",
            @SerializedName("passportExpiry")
            @Expose
            val passportExpiry: String = "",
            @SerializedName("relations")
            @Expose
            val relations: List<String> = arrayListOf(),
            @SerializedName("status")
            @Expose
            val status: Int = 0,
            @SerializedName("statusStr")
            @Expose
            val statusString: String = ""
    )

    data class Reason(
            @SerializedName("id")
            @Expose
            val id: String = "",
            @SerializedName("title")
            @Expose
            val title: String = "",
            @SerializedName("requiredDocs")
            @Expose
            val requiredDocs: List<String> = arrayListOf(),
            @SerializedName("formattedRequiredDocs")
            @Expose
            val formattedRequiredDocs: List<RequiredDoc> = arrayListOf()
    )

    data class RequiredDoc(
            @SerializedName("id")
            @Expose
            val id: String = "",
            @SerializedName("title")
            @Expose
            val title: String = ""
    )

    data class Included(
            @SerializedName("type")
            @Expose
            val type: String = "",
            @SerializedName("key")
            @Expose
            val key: String = "",
            @SerializedName("attributes")
            @Expose
            val attributes: Attribute = Attribute()

    ) {
        data class Attribute(
                @SerializedName("requiredDocs")
                @Expose
                val requiredDocs: List<String> = arrayListOf(),
                @SerializedName("title")
                @Expose
                val title: String = ""
        )
    }
}