package com.tokopedia.discovery2.usecase.calendarwidget

import com.tokopedia.discovery2.datamapper.getComponent
import com.tokopedia.discovery2.repository.calendarwidget.CalenderWidgetRepository
import javax.inject.Inject

class CalenderWidgetUseCase @Inject constructor(private val calenderWidgetRepository : CalenderWidgetRepository){
    suspend fun loadFirstPageComponents(componentId: String, pageEndPoint: String): Boolean {
        val component = getComponent(componentId, pageEndPoint)
        if (component?.noOfPagesLoaded == 1) return false
        component?.let {
            val (calendarWidgetData,nextPage) = calenderWidgetRepository.getCalendarWidget(
                componentId,
                mutableMapOf<String, Any>(),
                pageEndPoint, it.name)
            it.showVerticalLoader = calendarWidgetData.isNotEmpty()
            it.verticalProductFailState = false
            return true
        }
        return false
    }

}