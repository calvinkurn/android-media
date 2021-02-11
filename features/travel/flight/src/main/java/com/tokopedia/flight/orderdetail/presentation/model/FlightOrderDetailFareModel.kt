package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailFareModel(
        val adultNumeric: Long,
        val childNumeric: Long,
        val infantNumeric: Long
) : Parcelable