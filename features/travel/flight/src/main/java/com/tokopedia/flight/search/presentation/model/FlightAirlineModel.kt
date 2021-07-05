package com.tokopedia.flight.search.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rizky on 23/10/18.
 */
@Parcelize
data class FlightAirlineModel(val id: String,
                              val name: String,
                              val shortName: String,
                              val logo: String)
    : Parcelable