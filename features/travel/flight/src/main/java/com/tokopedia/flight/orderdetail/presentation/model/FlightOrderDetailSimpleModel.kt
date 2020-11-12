package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 13/11/2020
 */
@Parcelize
class FlightOrderDetailSimpleModel(
        val leftValue: String,
        val rightValue: String
) : Parcelable