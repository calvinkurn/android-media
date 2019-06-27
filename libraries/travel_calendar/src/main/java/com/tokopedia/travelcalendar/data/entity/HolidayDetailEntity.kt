package com.tokopedia.travelcalendar.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HolidayDetailEntity(
    @SerializedName("date")
    @Expose
    val date: String,

    @SerializedName("label")
    @Expose
    val label: String)

