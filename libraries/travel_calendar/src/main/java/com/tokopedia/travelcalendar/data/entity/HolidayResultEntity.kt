package com.tokopedia.travelcalendar.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HolidayResultEntity(
    @SerializedName("id")
    @Expose
    val id: String,

    @SerializedName("attributes")
    @Expose
    val attributes: HolidayDetailEntity)
