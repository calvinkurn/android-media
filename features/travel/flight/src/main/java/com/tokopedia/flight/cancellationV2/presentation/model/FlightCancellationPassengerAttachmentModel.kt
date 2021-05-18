package com.tokopedia.flight.cancellationV2.presentation.model

/**
 * @author by furqan on 17/06/2020
 */
data class FlightCancellationPassengerAttachmentModel(
        val passengerId: String,
        val passengerName: String,
        val passengerRelation: String) {

    override fun equals(other: Any?): Boolean {
        var isEqual = false

        if (other is FlightCancellationPassengerAttachmentModel) {
            isEqual = this.passengerId == other.passengerId &&
                    this.passengerName == other.passengerName &&
                    this.passengerRelation == other.passengerRelation
        }

        return isEqual
    }

}