package com.tokopedia.sellerhomecommon.domain.mapper

import com.tokopedia.sellerhomecommon.common.const.ShcConst
import com.tokopedia.sellerhomecommon.data.WidgetLastUpdatedSharedPrefInterface
import com.tokopedia.sellerhomecommon.domain.model.CalendarEventModel
import com.tokopedia.sellerhomecommon.domain.model.GetCalendarDataResponse
import com.tokopedia.sellerhomecommon.presentation.model.CalendarDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarEventGroupUiModel
import com.tokopedia.sellerhomecommon.presentation.model.CalendarEventUiModel
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import java.util.*
import javax.inject.Inject
import kotlin.math.abs

/**
 * Created by @ilhamsuaib on 07/02/22.
 */

class CalendarMapper @Inject constructor(
    lastUpdatedSharedPref: WidgetLastUpdatedSharedPrefInterface,
    lastUpdatedEnabled: Boolean
) : BaseWidgetMapper(lastUpdatedSharedPref, lastUpdatedEnabled),
    BaseResponseMapper<GetCalendarDataResponse, List<CalendarDataUiModel>> {

    companion object {
        private const val NUM_ITEMS_PER_PAGE = 3
    }

    override fun mapRemoteDataToUiData(
        response: GetCalendarDataResponse,
        isFromCache: Boolean
    ): List<CalendarDataUiModel> {
        return response.calendarData.data.map { data ->
            CalendarDataUiModel(
                dataKey = data.dataKey,
                error = data.errorMsg,
                isFromCache = isFromCache,
                showWidget = data.showWidget,
                eventGroups = getEventGroups(data.events),
                lastUpdated = getLastUpdatedMillis(data.dataKey, isFromCache)
            )
        }
    }

    private fun getEventGroups(events: List<CalendarEventModel>): List<CalendarEventGroupUiModel> {
        val closestEventFromToday = getClosestEvent(events)
        return events.chunked(NUM_ITEMS_PER_PAGE).map { subEvents ->
            CalendarEventGroupUiModel(
                events = getEventList(subEvents),
                autoScrollToHere = subEvents.contains(closestEventFromToday)
            )
        }
    }

    private fun getClosestEvent(events: List<CalendarEventModel>): CalendarEventModel? {
        val todayMillis = Date().time
        val closestEvent = events.minByOrNull {
            val millis = DateTimeUtil.getTimeInMillis(it.startDate, DateTimeUtil.FORMAT_DD_MM_YYYY)
            return@minByOrNull abs(millis.minus(todayMillis))
        }
        return closestEvent
    }

    private fun getEventList(events: List<CalendarEventModel>): List<CalendarEventUiModel> {
        return events.map { event ->
            CalendarEventUiModel(
                eventName = event.eventName,
                description = event.description,
                label = event.label,
                startDate = event.startDate,
                endDate = event.endDate,
                appLink = event.appLink,
                isOnGoingEvent = getIsOnGoingEvent(event)
            )
        }
    }

    private fun getIsOnGoingEvent(event: CalendarEventModel): Boolean {
        val todayInMillis = Date().time
        val startDateCal = Calendar.getInstance().apply {
            timeInMillis = DateTimeUtil.getTimeInMillis(
                event.startDate, DateTimeUtil.FORMAT_DD_MM_YYYY
            )
            set(Calendar.HOUR_OF_DAY, ShcConst.INT_0)
            set(Calendar.MINUTE, ShcConst.INT_0)
            set(Calendar.SECOND, ShcConst.INT_0)
            set(Calendar.MILLISECOND, ShcConst.INT_0)
        }
        val endDateCal = Calendar.getInstance().apply {
            timeInMillis = DateTimeUtil.getTimeInMillis(
                event.endDate, DateTimeUtil.FORMAT_DD_MM_YYYY
            )
            set(Calendar.HOUR_OF_DAY, ShcConst.INT_24)
            set(Calendar.MINUTE, ShcConst.INT_0)
            set(Calendar.SECOND, ShcConst.INT_0)
            set(Calendar.MILLISECOND, ShcConst.INT_0)
        }

        return todayInMillis in startDateCal.timeInMillis..endDateCal.timeInMillis
    }
}