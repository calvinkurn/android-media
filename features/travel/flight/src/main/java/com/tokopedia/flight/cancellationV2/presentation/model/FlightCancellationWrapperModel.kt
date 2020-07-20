package com.tokopedia.flight.cancellationV2.presentation.model

import android.os.Parcelable
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonAndAttachmentModel
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 17/07/2020
 */
@Parcelize
data class FlightCancellationWrapperModel(var cancellationReasonAndAttachmentModel: FlightCancellationReasonAndAttachmentModel = FlightCancellationReasonAndAttachmentModel(),
                                          var cancellationList: List<FlightCancellationModel> = arrayListOf(),
                                          var invoiceId: String = "") : Parcelable {
}