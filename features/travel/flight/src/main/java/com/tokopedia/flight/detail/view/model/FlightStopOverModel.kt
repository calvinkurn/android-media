package com.tokopedia.flight.detail.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlightStopOverModel(
    val airportCode: String = "",
    val cityName: String = ""
) : Parcelable {

    override fun toString(): String = cityName
}