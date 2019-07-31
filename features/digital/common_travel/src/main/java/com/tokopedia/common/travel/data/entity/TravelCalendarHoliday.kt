package com.tokopedia.common.travel.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TravelCalendarHoliday(
        @SerializedName("ID")
        @Expose
        val id: String = "",

        @SerializedName("attribute")
        @Expose
        val attribute: HolidayAttribute = HolidayAttribute()
) {
    data class HolidayAttribute(
            @SerializedName("date")
            @Expose
            val date: String = "",

            @SerializedName("label")
            @Expose
            val label: String = ""
    )

    data class Response(
            @SerializedName("TravelGetHoliday")
            @Expose
            val response: HolidayData = HolidayData()
    )

    data class HolidayData(
            @SerializedName("data")
            @Expose
            val data: List<TravelCalendarHoliday> = listOf()
    )
}