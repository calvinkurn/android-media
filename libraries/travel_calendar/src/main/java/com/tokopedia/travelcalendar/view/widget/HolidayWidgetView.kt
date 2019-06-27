package com.tokopedia.travelcalendar.view.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.view.adapter.HolidayAdapter
import com.tokopedia.travelcalendar.view.model.HolidayResult
import java.util.*


/**
 * Created by nabillasabbaha on 26/12/18.
 */
class HolidayWidgetView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                  defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val recyclerViewHoliday: RecyclerView
    private val currentHolidayList = mutableListOf<HolidayResult>()

    init {
        val view = View.inflate(context, R.layout.view_holiday_calendar, this)
        recyclerViewHoliday = view.findViewById(R.id.recycler_view_holiday) as RecyclerView
    }

    fun setHolidayData(holidayYearList: List<HolidayResult>, month: Int, year: Int) {
        currentHolidayList.clear()
        for (i in holidayYearList.indices) {
            val calendarHoliday = Calendar.getInstance()
            calendarHoliday.time = holidayYearList[i].attributes.dateHoliday

            if (calendarHoliday.get(Calendar.MONTH) == month && calendarHoliday.get(Calendar.YEAR) == year) {
                currentHolidayList.add(holidayYearList[i])
            }
        }

        recyclerViewHoliday.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        recyclerViewHoliday.layoutManager = linearLayoutManager
        recyclerViewHoliday.setNestedScrollingEnabled(false);

        val holidayAdapter = HolidayAdapter(currentHolidayList)
        holidayAdapter.notifyDataSetChanged()
        recyclerViewHoliday.adapter = holidayAdapter

        if (currentHolidayList.isEmpty()) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
        }
    }

    fun getCurrentHolidayList(): List<HolidayResult> {
        return currentHolidayList
    }
}
