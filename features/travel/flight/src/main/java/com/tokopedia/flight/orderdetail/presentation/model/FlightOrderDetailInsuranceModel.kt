package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailInsuranceModel(
        val id: String,
        val title: String,
        val tagline: String,
        val paidAmount: String,
        val paidAmountNumeric: Long
) : Parcelable