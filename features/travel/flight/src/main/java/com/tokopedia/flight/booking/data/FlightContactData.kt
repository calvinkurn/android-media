package com.tokopedia.flight.booking.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author by jessica on 2019-12-11
 */
@Parcelize
data class FlightContactData (
        val name: String = "",
        val email: String = "",
        val phone: String = "",
        val country: String = "ID",
        val countryCode: Int = 62
): Parcelable