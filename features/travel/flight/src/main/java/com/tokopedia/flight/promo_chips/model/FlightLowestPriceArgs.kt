package com.tokopedia.flight.promo_chips.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlightLowestPriceArgs(val departureID: String = "",
                                 val arrivalID: String = "",
                                 val startDate: String = "",
                                 val endDate: String = "",
                                 val classID: Int = 1,
                                 val airlineID: String = "")
    :Parcelable
