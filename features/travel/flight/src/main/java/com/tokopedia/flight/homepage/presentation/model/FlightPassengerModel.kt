package com.tokopedia.flight.homepage.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 24/06/2020
 */
@Parcelize
data class FlightPassengerModel(var adult: Int = 0,
                                var children: Int = 0,
                                var infant: Int = 0)
    : Parcelable, Cloneable {

    public override fun clone(): Any {
        return super.clone()
    }

}