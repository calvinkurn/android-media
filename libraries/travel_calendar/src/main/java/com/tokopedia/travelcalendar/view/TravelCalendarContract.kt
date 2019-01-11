package com.tokopedia.travelcalendar.view

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.travelcalendar.view.model.HolidayResult

import java.util.Date

/**
 * Created by nabillasabbaha on 14/05/18.
 */
interface TravelCalendarContract {

    interface View : CustomerView {

        fun renderCalendarMonthList(monthMinDate: Int, yearMinDate: Int, monthDeviation: Int)

        fun renderAllHolidayEvent(holidayYearList: List<HolidayResult>)

        fun renderErrorMessage(throwable: Throwable)

        fun showLoading()

        fun hideLoading()
    }

    interface Presenter : CustomerPresenter<View> {

        fun getDataHolidayCalendar(showHoliday: Boolean)

        fun getMonthsCalendarList(minDate: Date, maxDate: Date)

        fun onDestroyView()
    }
}
