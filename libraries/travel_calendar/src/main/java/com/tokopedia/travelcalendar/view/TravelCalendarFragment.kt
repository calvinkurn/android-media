package com.tokopedia.travelcalendar.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterView
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.view.model.CellDate
import com.tokopedia.travelcalendar.view.model.HolidayResult
import com.tokopedia.travelcalendar.view.presenter.TravelCalendarPresenter
import com.tokopedia.travelcalendar.view.widget.CalendarPickerView
import com.tokopedia.travelcalendar.view.widget.CustomQuickFilterMonthView
import com.tokopedia.travelcalendar.view.widget.HolidayWidgetView
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 14/05/18.
 */
class TravelCalendarFragment : BaseDaggerFragment(), TravelCalendarContract.View {

    private lateinit var calendarPickerView: CalendarPickerView
    private lateinit var actionListener: ActionListener
    private lateinit var monthQuickFilter: CustomViewQuickFilterView
    private lateinit var tabLayout: TabLayout
    private lateinit var holidayWidgetView: HolidayWidgetView
    private lateinit var progressBar: ProgressBar
    private lateinit var layoutCalendar: RelativeLayout

    private lateinit var selectedDate: Date
    private lateinit var maxDate: Date
    private lateinit var minDate: Date

    private var month: Int = 0
    private var year: Int = 0
    private var showHoliday: Boolean = false

    private val yearTabList = mutableListOf<Int>()
    private val quickFilterItemList = mutableListOf<QuickFilterItem>()
    private val mapDate = mutableMapOf<CustomViewQuickFilterItem, Int>()

    @Inject
    lateinit var presenter: TravelCalendarPresenter

    companion object {

        fun newInstance(selectedDate: Date, maxDate: Date,
                        minDate: Date, showHoliday: Boolean): Fragment {
            val fragment = TravelCalendarFragment()
            val bundle = Bundle()
            bundle.putSerializable(TravelCalendarActivity.EXTRA_INITAL_DATE, selectedDate)
            bundle.putSerializable(TravelCalendarActivity.EXTRA_MAX_DATE, maxDate)
            bundle.putSerializable(TravelCalendarActivity.EXTRA_MIN_DATE, minDate)
            bundle.putSerializable(TravelCalendarActivity.EXTRA_SHOW_HOLIDAY, showHoliday)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun initInjector() {
        GraphqlClient.init(activity!!)
        val travelCalendarComponent = TravelCalendarComponentInstance
                .getComponent(activity!!.application)
        travelCalendarComponent.inject(this)
        presenter.attachView(this)
    }

    override fun getScreenName(): String? {
        return null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_travel_calendar, container, false)
        calendarPickerView = view.findViewById(R.id.calendar_picker_view)
        tabLayout = view.findViewById(R.id.tab_layout)
        monthQuickFilter = view.findViewById(R.id.quick_filter_months)
        holidayWidgetView = view.findViewById(R.id.holiday_widget_view)
        layoutCalendar = view.findViewById(R.id.layout_calendar)
        progressBar = view.findViewById(R.id.loading_progress_bar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getDataFromBundle()
        presenter.getMonthsCalendarList(minDate, maxDate)
        setActionListener()
    }

    private fun getDataFromBundle() {
        //get current month
        month = arguments!!.getInt(TravelCalendarActivity.EXTRA_MONTH)
        year = arguments!!.getInt(TravelCalendarActivity.EXTRA_YEAR)
        selectedDate = arguments!!.getSerializable(TravelCalendarActivity.EXTRA_INITAL_DATE) as Date
        maxDate = arguments!!.getSerializable(TravelCalendarActivity.EXTRA_MAX_DATE) as Date
        minDate = arguments!!.getSerializable(TravelCalendarActivity.EXTRA_MIN_DATE) as Date
        showHoliday = arguments!!.getBoolean(TravelCalendarActivity.EXTRA_SHOW_HOLIDAY, false)
    }

    private fun setDataTabCalendar(month: Int, year: Int, monthDeviation: Int) {
        var month = month
        var year = year
        yearTabList.clear()
        mapDate.clear()
        val loopCalendar = DateCalendarUtil.getCalendar()
        loopCalendar.set(Calendar.DATE, 1)
        yearTabList.add(year)

        for (i in 0 until monthDeviation) {
            loopCalendar.set(Calendar.MONTH, month)
            loopCalendar.set(Calendar.YEAR, year)
            val monthName = SimpleDateFormat("MMMM", DateCalendarUtil.DEFAULT_LOCALE).format(loopCalendar.time)
            mapDate[convertQuickFilterItems(month, monthName)] = year

            if (month > 10) {
                month = 0
                year++
                yearTabList.add(year)
            } else {
                month++
            }
        }
    }

    private fun renderInitalSelectedDateCalendar() {
        val calendarNow = DateCalendarUtil.getCalendar()
        calendarNow.time = selectedDate

        var selected: Boolean
        val yearDateNow = SimpleDateFormat("yyyy", DateCalendarUtil.DEFAULT_LOCALE).format(calendarNow.time)
        val monthDateNow = SimpleDateFormat("MMMM", DateCalendarUtil.DEFAULT_LOCALE).format(calendarNow.time)
        this.year = Integer.valueOf(yearDateNow)
        for (i in yearTabList.indices) {
            selected = yearDateNow == yearTabList[i].toString()
            tabLayout.addTab(tabLayout.newTab().setText(yearTabList[i].toString()), selected)
        }

        var quickFilterItemInitial: CustomViewQuickFilterItem? = null
        quickFilterItemList.clear()
        for ((key, value) in mapDate) {
            val monthView = CustomQuickFilterMonthView(activity!!)
            monthView.setTextMonth(key.name)
            key.defaultView = monthView
            key.selectedView = monthView

            if (key.name == monthDateNow) {
                key.isSelected = true
                quickFilterItemInitial = key
            }
            if (value == this.year) {
                quickFilterItemList.add(key)
            }
        }
        monthQuickFilter.renderFilter(quickFilterItemList)
        monthQuickFilter.setDefaultItem(quickFilterItemInitial)
        this.month = Integer.parseInt(quickFilterItemInitial!!.type)
        presenter.getDataHolidayCalendar(showHoliday)
    }

    private fun convertQuickFilterItems(month: Int, monthName: String): CustomViewQuickFilterItem {
        val quickFilterItem = CustomViewQuickFilterItem()
        quickFilterItem.name = monthName
        quickFilterItem.type = month.toString()
        quickFilterItem.isSelected = false
        quickFilterItem.setColorBorder(R.color.tkpd_main_green)
        return quickFilterItem
    }

    private fun setActionListener() {
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                year = Integer.parseInt(tab.text!!.toString())

                quickFilterItemList.clear()
                for ((key, value) in mapDate) {
                    key.isSelected = false
                    val monthView = CustomQuickFilterMonthView(activity!!)
                    monthView.setTextMonth(key.name)
                    key.defaultView = monthView
                    key.selectedView = monthView
                    if (value == year) {
                        quickFilterItemList.add(key)
                    }
                }
                monthQuickFilter.renderFilter(quickFilterItemList)
                quickFilterItemList[0].isSelected = true
                monthQuickFilter.setDefaultItem(quickFilterItemList[0])
                month = Integer.parseInt(quickFilterItemList[0].type)
                presenter.getDataHolidayCalendar(showHoliday)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        monthQuickFilter.setListener { typeFilter ->
            month = Integer.parseInt(typeFilter)
            presenter.getDataHolidayCalendar(showHoliday)
        }

        calendarPickerView.setActionListener(object : CalendarPickerView.ActionListener {
            override fun onDateClicked(cellDate: CellDate) {
                actionListener.onClickDate(cellDate)
            }
        })
    }

    override fun renderCalendarMonthList(monthMinDate: Int, yearMinDate: Int, monthDeviation: Int) {
        setDataTabCalendar(monthMinDate, yearMinDate, monthDeviation)
        renderInitalSelectedDateCalendar()
    }

    override fun renderAllHolidayEvent(holidayYearList: List<HolidayResult>) {
        //date selected from outside
        val cellDate = CellDate(selectedDate, true)
        val calendarMaxDate = DateCalendarUtil.getCalendar()
        calendarMaxDate.time = maxDate
        val calendarMinDate = DateCalendarUtil.getCalendar()
        calendarMinDate.time = minDate

        if (holidayYearList.isEmpty()) {
            holidayWidgetView.visibility = View.GONE
        } else {
            holidayWidgetView.visibility = View.VISIBLE
            holidayWidgetView.setHolidayData(holidayYearList, month, year)
        }

        calendarPickerView.setDateRange(cellDate, month, year, calendarMaxDate, calendarMinDate,
                holidayWidgetView.getCurrentHolidayList())
    }

    override fun renderErrorMessage(throwable: Throwable) {
        progressBar.visibility = View.GONE
        val errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
        NetworkErrorHelper.createSnackbarWithAction(activity, errorMessage) {
            showHoliday = false
            renderInitalSelectedDateCalendar()
        }.showRetrySnackbar()
    }

    override fun showLoading() {
        progressBar.visibility = View.VISIBLE
        layoutCalendar.visibility = View.GONE
    }

    override fun hideLoading() {
        progressBar.visibility = View.GONE
        layoutCalendar.visibility = View.VISIBLE
    }

    override fun onAttachActivity(context: Context) {
        if (context is ActionListener) {
            actionListener = context
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyView()
    }

    interface ActionListener {
        fun onClickDate(cellDate: CellDate)
    }
}
