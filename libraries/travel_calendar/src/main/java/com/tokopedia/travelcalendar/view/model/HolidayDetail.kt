package com.tokopedia.travelcalendar.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class HolidayDetail constructor(val date: String, val label: String,
                    val dateHoliday: Date) : Parcelable
