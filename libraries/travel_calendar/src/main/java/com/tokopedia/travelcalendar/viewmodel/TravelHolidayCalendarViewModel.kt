package com.tokopedia.travelcalendar.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.calendar.Legend
import com.tokopedia.travelcalendar.TRAVEL_CAL_YYYY_MM_DD
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import com.tokopedia.travelcalendar.domain.TravelCalendarHolidayUseCase
import com.tokopedia.travelcalendar.stringToDate
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class TravelHolidayCalendarViewModel @Inject constructor(private val useCase: TravelCalendarHolidayUseCase,
                                                         val dispatcher: CoroutineDispatcher)
    : BaseViewModel(dispatcher) {

    val holidayCalendarData = MutableLiveData<ArrayList<Legend>>()

    fun getCalendarHoliday() {
        launch {
            useCase.execute().also {
                when (it) {
                    is Success -> {
                        holidayCalendarData.value = mappingHolidayData(it.data)
                    }
                    is Fail -> {
                        holidayCalendarData.value = arrayListOf()
                    }
                }
            }
        }
    }

    private fun mappingHolidayData(holidayData: TravelCalendarHoliday.HolidayData): ArrayList<Legend> {
        val legendList = arrayListOf<Legend>()
        for (holiday in holidayData.data) {
            legendList.add(Legend(holiday.attribute.date.stringToDate(TRAVEL_CAL_YYYY_MM_DD),
                    holiday.attribute.label))
        }
        return legendList
    }
}