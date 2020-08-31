package com.tokopedia.flight.cancellationV2.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 17/07/2020
 */
@Parcelize
data class FlightCancellationReasonAndAttachmentModel(val attachmentList: MutableList<FlightCancellationAttachmentModel> = arrayListOf(),
                                                      var reason: String = "",
                                                      var reasonId: String = "",
                                                      var estimateRefund: Long = 0,
                                                      var estimateFmt: String = "",
                                                      var showEstimateRefund: Boolean = true)
    : Parcelable {
}