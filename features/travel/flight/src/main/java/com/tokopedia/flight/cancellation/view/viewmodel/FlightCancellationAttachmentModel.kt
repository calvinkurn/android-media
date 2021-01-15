package com.tokopedia.flight.cancellation.view.viewmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by alvarisi on 3/26/18.
 */
@Parcelize
data class FlightCancellationAttachmentModel(var filename: String = "",
                                             var filepath: String = "",
                                             var passengerId: String = "",
                                             var passengerName: String = "",
                                             var relationId: String = "",
                                             var journeyId: String = "",
                                             var docType: Int = 0,
                                             var percentageUpload: Long = 0,
                                             var isUploaded: Boolean = false)
    : Parcelable {


    override fun equals(other: Any?): Boolean {
        var isEqual = false

        if (other is FlightCancellationAttachmentModel) {
            isEqual = this.passengerId == other.passengerId &&
                    this.passengerName == other.passengerName
        }

        return isEqual
    }

}