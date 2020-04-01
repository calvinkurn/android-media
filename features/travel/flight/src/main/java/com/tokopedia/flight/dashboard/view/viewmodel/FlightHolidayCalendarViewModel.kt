package com.tokopedia.flight.dashboard.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.calendar.Legend
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import com.tokopedia.travelcalendar.domain.TravelCalendarHolidayUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class FlightHolidayCalendarViewModel @Inject constructor(private val useCase: TravelCalendarHolidayUseCase,
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
            legendList.add(Legend(TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, holiday.attribute.date),
                    holiday.attribute.label))
        }
        return legendList
    }
}