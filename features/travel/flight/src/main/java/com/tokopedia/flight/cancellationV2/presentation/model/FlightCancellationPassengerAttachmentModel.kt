package com.tokopedia.flight.cancellationV2.presentation.model

import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerModel

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
        } else if (other is FlightCancellationPassengerModel) {
            isEqual = this.passengerId == other.passengerId &&
                    this.passengerRelation == other.relationId
        }

        return isEqual
    }

}