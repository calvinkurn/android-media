package com.tokopedia.flight.cancellationdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 06/01/2021
 */
@Parcelize
class FlightOrderCancellationListModel(
        val orderId: String = "",
        val cancellationDetail: FlightOrderCancellationDetailModel = FlightOrderCancellationDetailModel()
) : Parcelable