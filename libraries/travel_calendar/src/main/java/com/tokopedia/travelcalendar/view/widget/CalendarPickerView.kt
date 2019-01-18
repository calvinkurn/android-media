package com.tokopedia.travelcalendar.view.widget

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.view.adapter.GridCalendarAdapter
import com.tokopedia.travelcalendar.view.getZeroTime
import com.tokopedia.travelcalendar.view.model.CellDate
import com.tokopedia.travelcalendar.view.model.HolidayResult
import java.util.*

/**
 * Created by nabillasabbaha on 07/05/18.
 */

class CalendarPickerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                   defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    private val calendarGrid: RecyclerView
    private lateinit var adapter: GridCalendarAdapter
    private lateinit var actionListener: ActionListener
    private lateinit var holidayResultList: List<HolidayResult>
    private lateinit var cellDateUser: CellDate

    init {
        orientation = LinearLayout.VERTICAL
        val view = LayoutInflater.from(context).inflate(R.layout.view_calendar_picker, this, true)
        calendarGrid = view.findViewById(R.id.calendar_grid) as RecyclerView
    }

    fun setActionListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    private fun renderCalendar(month: Int, year: Int, maxDateCal: Calendar, minDateCal: Calendar) {
        val calendar = Calendar.getInstance()
        calendar.time = cellDateUser.date
        val cells = mutableListOf<CellDate>()

        val mCal = Calendar.getInstance()
        mCal.set(Calendar.DAY_OF_MONTH, 1)
        mCal.set(Calendar.MONTH, month)
        mCal.set(Calendar.YEAR, year)

        val firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - FIRST_DAY_INITIAL
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth)

        while (cells.size < DAYS_COUNT) {
            if (Calendar.getInstance().getZeroTime(calendar.time).compareTo(
                            Calendar.getInstance().getZeroTime(mCal.time)) == 0) {
                cells.add(cellDateUser)
            } else {
                cells.add(CellDate(mCal.time, false))
            }
            mCal.add(Calendar.DAY_OF_MONTH, 1)
        }
        adapter = GridCalendarAdapter(cells, mCal, maxDateCal,
                minDateCal, holidayResultList, gridActionListener(cells))
        calendarGrid.layoutManager = GridLayoutManager(context, 7)
        calendarGrid.adapter = adapter
    }

    private fun gridActionListener(cells: MutableList<CellDate>): GridCalendarAdapter.ActionListener {
        return object : GridCalendarAdapter.ActionListener {
            override fun onClickDate(cellDate: CellDate) {
                for (i in cells.indices) {
                    cells[i].isSelected = (cellDate.date === cells[i].date)
                }
                adapter.notifyDataSetChanged()
                cellDateUser = cellDate
                actionListener.onDateClicked(cellDate)
            }
        }
    }

    fun setDateRange(cellDate: CellDate, month: Int, year: Int, maxDateCal: Calendar,
                     minDateCal: Calendar, holidayResultList: List<HolidayResult>) {
        this.cellDateUser = cellDate
        this.holidayResultList = holidayResultList
        renderCalendar(month, year, maxDateCal, minDateCal)
    }

    interface ActionListener {
        fun onDateClicked(dateRange: CellDate)
    }

    companion object {

        /**
         * set 1 if sunday is the first day of gridview
         * set 2 if sunday is the last day of gridview
         */
        private val FIRST_DAY_INITIAL = 2

        private val DAYS_COUNT = 42
    }
}