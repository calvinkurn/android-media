package com.tokopedia.flight.detail.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by furqan on 1/17/22.
 */
@Parcelize
data class FlightOrderDetailInfoModel(
    val label: String,
    val value: String
) : Parcelable
