package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
class FlightOrderDetailPassengerCancelStatusModel(
        val status: Int,
        val statusStr: String,
        val statusType: String
) : Parcelable