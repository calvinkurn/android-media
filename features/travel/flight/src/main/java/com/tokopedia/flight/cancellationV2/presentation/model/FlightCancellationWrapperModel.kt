package com.tokopedia.flight.cancellationV2.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 17/07/2020
 */
@Parcelize
data class FlightCancellationWrapperModel(var cancellationReasonAndAttachmentModel: FlightCancellationReasonAndAttachmentModel = FlightCancellationReasonAndAttachmentModel(),
                                          var cancellationList: MutableList<FlightCancellationModel> = arrayListOf(),
                                          var invoiceId: String = "") : Parcelable {
}