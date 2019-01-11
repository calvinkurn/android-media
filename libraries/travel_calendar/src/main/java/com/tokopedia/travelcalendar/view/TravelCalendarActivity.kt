package com.tokopedia.travelcalendar.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.view.model.CellDate

import java.util.Date

class TravelCalendarActivity : BaseSimpleActivity(), TravelCalendarFragment.ActionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTitleToolbar()
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_travel_calendar
    }

    private fun updateTitleToolbar() {
        val scheduleType = intent.getIntExtra(EXTRA_SCHEDULE_TYPE, 0)
        if (scheduleType == DEPARTURE_TYPE) {
            updateTitle(resources.getString(R.string.travel_calendar_label_choose_departure_trip_date))
        } else if (scheduleType == RETURN_TYPE) {
            updateTitle(resources.getString(R.string.travel_calendar_label_choose_return_trip_date))
        } else {
            updateTitle(resources.getString(R.string.travel_calendar_label_choose_date))
        }
    }

    override fun isShowCloseButton(): Boolean {
        return true
    }

    override fun getNewFragment(): Fragment {
        return TravelCalendarFragment.newInstance(
                intent.getSerializableExtra(EXTRA_INITAL_DATE) as Date,
                intent.getSerializableExtra(EXTRA_MAX_DATE) as Date,
                intent.getSerializableExtra(EXTRA_MIN_DATE) as Date,
                intent.getBooleanExtra(EXTRA_SHOW_HOLIDAY, false))
    }

    override fun onClickDate(cellDate: CellDate) {
        val intentDate = Intent()
        intentDate.putExtra(DATE_SELECTED, cellDate.date)
        setResult(RESULT_OK, intentDate)
        finish()
    }

    companion object {

        val DATE_SELECTED = "date_selected"
        val EXTRA_INITAL_DATE = "initial_date"
        val EXTRA_MONTH = "month"
        val EXTRA_YEAR = "year"
        val EXTRA_MIN_DATE = "min_date"
        val EXTRA_MAX_DATE = "max_date"
        val EXTRA_SHOW_HOLIDAY = "show_holiday"
        val DEPARTURE_TYPE = 1
        val RETURN_TYPE = 2
        val DEFAULT_TYPE = 0
        val EXTRA_SCHEDULE_TYPE = "schedule_type"

        fun newInstance(context: Context, initialDate: Date, minDate: Date, maxDate: Date,
                        scheduleType: Int, showHoliday: Boolean): Intent {
            val intent = Intent(context, TravelCalendarActivity::class.java)
            intent.putExtra(EXTRA_INITAL_DATE, initialDate)
            intent.putExtra(EXTRA_MIN_DATE, minDate)
            intent.putExtra(EXTRA_MAX_DATE, maxDate)
            intent.putExtra(EXTRA_SCHEDULE_TYPE, scheduleType)
            intent.putExtra(EXTRA_SHOW_HOLIDAY, showHoliday)
            return intent
        }
    }
}