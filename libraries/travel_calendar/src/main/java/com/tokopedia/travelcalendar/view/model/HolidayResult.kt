package com.tokopedia.travelcalendar.view.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class HolidayResult(val id: String,
                    val attributes: HolidayDetail) : Parcelable

