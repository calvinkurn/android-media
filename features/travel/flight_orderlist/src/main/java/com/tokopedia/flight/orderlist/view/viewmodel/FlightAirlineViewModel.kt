package com.tokopedia.flight.orderlist.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Rizky on 23/10/18.
 */
@Parcelize
data class FlightAirlineViewModel(val id: String, val name: String, val shortName: String, val logo: String): Parcelable