package com.tokopedia.travelcalendar.selectionrangecalendar

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import com.tokopedia.travelcalendar.domain.TravelCalendarHolidayUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectionRangeCalendarViewModel @Inject constructor(val dispatcher: CoroutineDispatcher,
                                                          private val useCase: TravelCalendarHolidayUseCase)
    : BaseViewModel(dispatcher) {

    val holidayResult = MutableLiveData<Result<TravelCalendarHoliday.HolidayData>>()

    fun getTravelHolidayDate() {
        launch {
            holidayResult.value = useCase.execute()
        }
    }
}