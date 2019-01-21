package com.tokopedia.travelcalendar.view

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.travelcalendar.view.model.HolidayResult

/**
 * Created by nabillasabbaha on 14/05/18.
 */
interface TravelCalendarContract {

    interface View : CustomerView {

        fun renderAllHolidayEvent(holidayYearList: List<HolidayResult>)

        fun renderErrorMessage(throwable: Throwable)
    }

    interface Presenter : CustomerPresenter<View> {

        fun getDataHolidayCalendar(showHoliday: Boolean)

        fun onDestroyView()
    }
}
