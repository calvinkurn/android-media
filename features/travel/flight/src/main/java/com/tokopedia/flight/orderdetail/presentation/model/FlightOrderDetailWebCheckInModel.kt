package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailWebCheckInModel(
        val title: String,
        val subtitle: String,
        val startTime: String,
        val endTime: String,
        val iconUrl: String,
        val appUrl: String,
        val webUrl: String
) : Parcelable