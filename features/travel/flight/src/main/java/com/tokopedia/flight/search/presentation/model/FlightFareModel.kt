package com.tokopedia.flight.search.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 25/06/2020
 */
@Parcelize
data class FlightFareModel(val adult: String,
                           val adultCombo: String,
                           val child: String,
                           val childCombo: String,
                           val infant: String,
                           val infantCombo: String,
                           val adultNumeric: Int,
                           val adultNumericCombo: Int,
                           val childNumeric: Int,
                           val childNumericCombo: Int,
                           val infantNumeric: Int,
                           val infantNumericCombo: Int) : Parcelable