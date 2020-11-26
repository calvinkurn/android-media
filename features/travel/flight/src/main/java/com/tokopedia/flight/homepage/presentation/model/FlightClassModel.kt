package com.tokopedia.flight.homepage.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 24/06/2020
 */
@Parcelize
data class FlightClassModel(val id: Int = 0,
                            val title: String = "")
    : Parcelable