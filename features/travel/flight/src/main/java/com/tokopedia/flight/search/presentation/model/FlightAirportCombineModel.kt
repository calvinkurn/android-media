package com.tokopedia.flight.search.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 25/06/2020
 */
@Parcelize
data class FlightAirportCombineModel(var departureAirport: String,
                                     var arrivalAirport: String,
                                     var hasLoad: Boolean = false,
                                     var isNeedRefresh: Boolean = true,
                                     var noOfRetry: Int = 0,
                                     var airlines: MutableList<String> = arrayListOf())
    : Parcelable