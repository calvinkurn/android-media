package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 13/11/2020
 */
@Parcelize
class FlightOrderDetailSimpleModel(
        val leftValue: String,
        val rightValue: String,
        val isLeftBold: Boolean,
        val isRightBold: Boolean,
        val isLeftStriked: Boolean,
        val isRightStriked: Boolean,
        val isRightAlign: Boolean = false
) : Parcelable