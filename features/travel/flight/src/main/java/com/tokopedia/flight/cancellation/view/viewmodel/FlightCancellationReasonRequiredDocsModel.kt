package com.tokopedia.flight.cancellation.view.viewmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 15/06/2020
 */
@Parcelize
data class FlightCancellationReasonRequiredDocsModel(val docId: Int,
                                                     val docTitle: String)
    : Parcelable