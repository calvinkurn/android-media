package com.tokopedia.flight.booking.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlightPriceDetailEntity(
    val label: String = "",
    val price: String = "",
    val priceNumeric: Int = 0,
    val priceDetailId: String = "",
    val popUpTitle: String = "",
    val popUpDescription: String = ""
): Parcelable
