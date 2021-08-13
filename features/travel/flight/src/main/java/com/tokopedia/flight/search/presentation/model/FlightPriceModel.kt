package com.tokopedia.flight.search.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 25/06/2020
 */
@Parcelize
data class FlightPriceModel(var departurePrice: FlightFareModel? = null,
                            var returnPrice: FlightFareModel? = null,
                            var comboKey: String = "")
    : Parcelable