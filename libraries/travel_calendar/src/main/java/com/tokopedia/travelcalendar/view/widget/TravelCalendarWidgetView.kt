package com.tokopedia.travelcalendar.view.widget

import android.content.Context
import com.google.android.material.tabs.TabLayout
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterView
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.view.CALENDAR_MMMM
import com.tokopedia.travelcalendar.view.CALENDAR_YYYY
import com.tokopedia.travelcalendar.view.convertString
import com.tokopedia.travelcalendar.view.model.CellDate
import com.tokopedia.travelcalendar.view.model.HolidayResult
import java.util.*

/**
 * Created by nabillasabbaha on 14/01/19.
 */
class TravelCalendarWidgetView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
                                                         defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val calendarPickerView: CalendarPickerView
    private val tabLayout: TabLayout
    private val monthQuickFilter: CustomViewQuickFilterView
    private val holidayWidgetView: HolidayWidgetView

    val holidayDataList = mutableListOf<HolidayResult>()
    private val yearSetTabList = mutableSetOf<Int>()
    private val yearTabList = mutableListOf<Int>()
    private val quickFilterItemList = mutableListOf<QuickFilterItem>()
    private val mapDate = mutableMapOf<CustomViewQuickFilterItem, Int>()

    private lateinit var selectedDate: Date
    private lateinit var maxDate: Date
    private lateinit var minDate: Date

    private var month: Int = 0
    private var year: Int = 0
    private var actionListener: ActionListener? = null

    init {
        val view = View.inflate(context, R.layout.view_travel_calendar_widget, this)
        calendarPickerView = view.findViewById(R.id.calendar_picker_view) as CalendarPickerView
        tabLayout = view.findViewById(R.id.tab_layout) as TabLayout
        monthQuickFilter = view.findViewById(R.id.quick_filter_months) as CustomViewQuickFilterView
        holidayWidgetView = view.findViewById(R.id.holiday_widget_view) as HolidayWidgetView
    }

    fun setListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    /**
     * set initial data to render calendar
     * @param minDate minimum date on calendar
     * @param maxDate maximum date on calendar
     * @param selectedDate date selected that will have green color
     */
    fun renderViewTravelCalendar(minDate: Date, maxDate: Date, selectedDate: Date) {
        this.selectedDate = selectedDate
        this.maxDate = maxDate
        this.minDate = minDate

        val initCalendar = Calendar.getInstance()
        val calendarMinDate = initCalendar.clone() as Calendar
        calendarMinDate.time = minDate
        val monthMinDate = calendarMinDate.get(Calendar.MONTH)
        val yearMinDate = calendarMinDate.get(Calendar.YEAR)

        val calendarMaxDate = initCalendar.clone() as Calendar
        calendarMaxDate.time = maxDate
        val monthMaxDate = calendarMaxDate.get(Calendar.MONTH)
        val yearMaxDate = calendarMaxDate.get(Calendar.YEAR)

        val yearDeviation = ((yearMaxDate - yearMinDate) * 12) + 1
        val monthDeviation = yearDeviation + monthMaxDate - monthMinDate

        addActionListener()
        setDataTabCalendar(monthMinDate, yearMinDate, monthDeviation)
        renderInitalSelectedDateCalendar()
    }

    private fun addActionListener() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                year = Integer.parseInt(tab.text!!.toString())

                quickFilterItemList.clear()
                for ((key, value) in mapDate) {
                    key.isSelected = false
                    val monthView = CustomQuickFilterMonthView(context)
                    monthView.setTextMonth(key.name)
                    key.defaultView = monthView
                    key.selectedView = monthView
                    if (value == year) {
                        quickFilterItemList.add(key)
                    }
                }
                monthQuickFilter.renderFilter(quickFilterItemList)
                //always set to first position every change year calendar
                quickFilterItemList[0].isSelected = true
                monthQuickFilter.setDefaultItem(quickFilterItemList[0])
                monthQuickFilter.scrollToPosition(0)
                month = Integer.parseInt(quickFilterItemList[0].type)

                //render calendar
                renderGridCalendar()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        monthQuickFilter.setListener { typeFilter ->
            month = Integer.parseInt(typeFilter)
            renderGridCalendar()
        }

        calendarPickerView.setActionListener(object : CalendarPickerView.ActionListener {
            override fun onDateClicked(cellDate: CellDate) {
                actionListener!!.onClickDate(cellDate)
            }
        })
    }

    private fun setDataTabCalendar(month: Int, year: Int, monthDeviation: Int) {
        var month = month
        var year = year
        yearTabList.clear()
        mapDate.clear()
        val loopCalendar = Calendar.getInstance()
        loopCalendar.set(Calendar.DATE, 1)
        yearSetTabList.add(year)

        for (i in 0 until monthDeviation) {
            loopCalendar.set(Calendar.MONTH, month)
            loopCalendar.set(Calendar.YEAR, year)
            val monthName = loopCalendar.time.convertString(CALENDAR_MMMM)
            mapDate[convertQuickFilterItems(month, monthName)] = year
            yearSetTabList.add(year)

            if (month > 10) {
                month = 0
                year++
            } else {
                month++
            }
        }
        yearTabList.addAll(yearSetTabList)
        yearSetTabList.clear()
    }

    private fun convertQuickFilterItems(month: Int, monthName: String): CustomViewQuickFilterItem {
        val quickFilterItem = CustomViewQuickFilterItem()
        quickFilterItem.name = monthName
        quickFilterItem.type = month.toString()
        quickFilterItem.isSelected = false
        quickFilterItem.setColorBorder(com.tokopedia.unifyprinciples.R.color.Unify_G400)
        return quickFilterItem
    }

    /**
     * this method render first time calendar open and
     * coloring the date that selected green on calendar
     */
    private fun renderInitalSelectedDateCalendar() {
        val calendarNow = Calendar.getInstance()
        calendarNow.time = selectedDate

        var selected: Boolean
        val yearDateNow = calendarNow.time.convertString(CALENDAR_YYYY)
        val monthDateNow = calendarNow.time.convertString(CALENDAR_MMMM)
        this.year = Integer.valueOf(yearDateNow)
        for (i in yearTabList.indices) {
            selected = yearDateNow == yearTabList[i].toString()
            tabLayout.addTab(tabLayout.newTab().setText(yearTabList[i].toString()), selected)
        }

        quickFilterItemList.clear()
        for ((key, value) in mapDate) {
            val monthView = CustomQuickFilterMonthView(context)
            monthView.setTextMonth(key.name)
            key.defaultView = monthView
            key.selectedView = monthView

            //reset month selected from onTabSelectedListener
            key.isSelected = false

            if (key.name == monthDateNow) {
                key.isSelected = true
                monthQuickFilter.setDefaultItem(key)
                this.month = Integer.parseInt(key.type)
            }
            if (value == this.year) {
                quickFilterItemList.add(key)
            }
        }
        monthQuickFilter.renderFilter(quickFilterItemList)
        for (i in 0 until quickFilterItemList.size) {
            if (quickFilterItemList.get(i).isSelected) {
                monthQuickFilter.scrollToPosition(i)
            }
        }
        //render calendar
        renderGridCalendar()
    }

    /**
     * this method to update grid calendar every year & month updated
     */
    fun renderGridCalendar() {
        val cellDate = CellDate(selectedDate, true)
        val calendarMaxDate = Calendar.getInstance()
        calendarMaxDate.time = maxDate
        val calendarMinDate = Calendar.getInstance()
        calendarMinDate.time = minDate

        holidayWidgetView.setHolidayData(holidayDataList, month, year)
        calendarPickerView.setDateRange(cellDate, month, year, calendarMaxDate, calendarMinDate,
                holidayWidgetView.getCurrentHolidayList())
    }

    interface ActionListener {
        fun onClickDate(cellDate: CellDate)
    }
}