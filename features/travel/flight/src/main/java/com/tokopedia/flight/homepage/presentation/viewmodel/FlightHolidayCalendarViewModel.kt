package com.tokopedia.flight.homepage.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.calendar.Legend
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import com.tokopedia.travelcalendar.domain.TravelCalendarHolidayUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.date.DateUtil
import com.tokopedia.utils.date.toDate
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class FlightHolidayCalendarViewModel @Inject constructor(private val useCase: TravelCalendarHolidayUseCase,
                                                         dispatcherProvider: CoroutineDispatchers)
    : BaseViewModel(dispatcherProvider.io) {

    val holidayCalendarData = MutableLiveData<ArrayList<Legend>>()

    fun getCalendarHoliday() {
        launch {
            useCase.execute().also {
                when (it) {
                    is Success -> {
                        holidayCalendarData.postValue(mappingHolidayData(it.data))
                    }
                    is Fail -> {
                        holidayCalendarData.postValue(arrayListOf())
                    }
                }
            }
        }
    }

    private fun mappingHolidayData(holidayData: TravelCalendarHoliday.HolidayData): ArrayList<Legend> {
        val legendList = arrayListOf<Legend>()
        for (holiday in holidayData.data) {
            legendList.add(Legend(holiday.attribute.date.toDate(DateUtil.YYYY_MM_DD),
                    holiday.attribute.label))
        }
        return legendList
    }
}