package com.tokopedia.hotel.common.presentation.widget.hotelcalendar

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.data.entity.TravelCalendarHoliday
import com.tokopedia.common.travel.domain.TravelCalendarHolidayUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class HotelCalendarDialogViewModel @Inject constructor(val dispatcher: CoroutineDispatcher,
                                                       private val useCase: TravelCalendarHolidayUseCase)
    : BaseViewModel(dispatcher) {

    val holidayResult = MutableLiveData<Result<TravelCalendarHoliday.HolidayData>>()

    fun getTravelHolidayDate() {
        launch {
            holidayResult.value = useCase.execute()
        }
    }
}