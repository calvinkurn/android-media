package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailConditionalInfoModel(
        val type: String,
        val title: String,
        val text: String,
        val border: String,
        val background: String
) : Parcelable