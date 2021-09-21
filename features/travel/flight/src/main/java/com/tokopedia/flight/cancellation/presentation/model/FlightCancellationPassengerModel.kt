package com.tokopedia.flight.cancellation.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 14/07/2020
 */
@Parcelize
data class FlightCancellationPassengerModel(
        var passengerId: String = "",
        var type: Int = 0,
        var title: Int = 0,
        var titleString: String = "",
        var firstName: String = "",
        var lastName: String = "",
        var relationId: String = "",
        var relations: List<String> = arrayListOf(),
        var status: Int = 0,
        var statusString: String = ""
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        var isEquals: Boolean = false

        other?.let {
            if (it is FlightCancellationPassengerModel) {
                isEquals = this.relationId == it.relationId &&
                        this.passengerId == it.passengerId &&
                        this.firstName == it.firstName &&
                        this.lastName == it.lastName
            }
        }

        return isEquals
    }
}