package com.tokopedia.travelcalendar.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 14/05/18.
 */
class HolidayEntity(

    @SerializedName("flightHoliday")
    @Expose
    val holidayResultEntities: List<HolidayResultEntity>)
