package com.tokopedia.flight.orderdetail.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 16/10/2020
 */
@Parcelize
data class FlightOrderDetailAmenityModel(
        val departureId: String,
        val arrivalId: String,
        val type: Int,
        val price: String,
        val priceNumeric: Long,
        val detail: String
) : Parcelable