package com.tokopedia.sellerhomecommon.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

data class GetCalendarDataResponse(
    @Expose
    @SerializedName("fetchCalendarWidgetData")
    val calendarData: FetchCalendarWidgetDataModel = FetchCalendarWidgetDataModel()
)

data class FetchCalendarWidgetDataModel(
    @Expose
    @SerializedName("data")
    val data: List<CalendarWidgetDataModel> = emptyList()
)

data class CalendarWidgetDataModel(
    @Expose
    @SerializedName("dataKey")
    val dataKey: String = "",
    @Expose
    @SerializedName("events")
    val events: List<CalendarEventModel> = emptyList(),
    @Expose
    @SerializedName("emptyState")
    val emptyState: CalendarEmptyStateModel = CalendarEmptyStateModel(),
    @Expose
    @SerializedName("errorMsg")
    val errorMsg: String = "",
    @Expose
    @SerializedName("showWidget")
    val showWidget: Boolean = false
)

data class CalendarEventModel(
    @Expose
    @SerializedName("eventName")
    val eventName: String = "",
    @Expose
    @SerializedName("description")
    val description: String = "",
    @Expose
    @SerializedName("label")
    val label: String = "",
    @Expose
    @SerializedName("startDate")
    val startDate: String = "",
    @Expose
    @SerializedName("endDate")
    val endDate: String = "",
    @Expose
    @SerializedName("applink")
    val appLink: String = ""
)

data class CalendarEmptyStateModel(
    @Expose
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @Expose
    @SerializedName("title")
    val title: String = "",
    @Expose
    @SerializedName("description")
    val description: String = ""
)