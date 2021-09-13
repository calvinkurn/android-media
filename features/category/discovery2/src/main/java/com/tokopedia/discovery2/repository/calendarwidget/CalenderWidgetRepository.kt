package com.tokopedia.discovery2.repository.calendarwidget

import com.tokopedia.discovery2.data.ComponentsItem

interface CalenderWidgetRepository {
    suspend fun getCalendarWidget(componentId: String, queryParamterMap: MutableMap<String, Any>, pageEndPoint: String, calendarComponentName: String?): Pair<ArrayList<ComponentsItem>,String?>
}