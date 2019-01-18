package com.tokopedia.travelcalendar.view.bottomsheet

import android.os.Bundle
import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.travelcalendar.R
import com.tokopedia.travelcalendar.view.TravelCalendarComponentInstance
import com.tokopedia.travelcalendar.view.TravelCalendarContract
import com.tokopedia.travelcalendar.view.bottomsheet.TravelCalendarBottomSheet.Builder
import com.tokopedia.travelcalendar.view.model.CellDate
import com.tokopedia.travelcalendar.view.model.HolidayResult
import com.tokopedia.travelcalendar.view.presenter.TravelCalendarPresenter
import com.tokopedia.travelcalendar.view.widget.TravelCalendarWidgetView
import java.util.*
import javax.inject.Inject

/**
 * Created by nabillasabbaha on 11/01/19.
 * Just need to use these methods:
 * @property Builder
 * @property setListener
 * @property show
 */
class TravelCalendarBottomSheet : BottomSheets(), TravelCalendarContract.View {

    private lateinit var travelCalendarWidgetView: TravelCalendarWidgetView
    private lateinit var actionListener: ActionListener
    private lateinit var title: String
    private lateinit var minDate: Date
    private lateinit var maxDate: Date
    private lateinit var selectedDate: Date
    private lateinit var bottomSheetsState: BottomSheetsState
    private var showHoliday: Boolean = false

    @Inject
    lateinit var presenter: TravelCalendarPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    private fun initInjector() {
        GraphqlClient.init(activity!!)
        val travelCalendarComponent = TravelCalendarComponentInstance
                .getComponent(activity!!.application)
        travelCalendarComponent.inject(this)
        presenter.attachView(this)
    }

    private fun setAllData(showHoliday: Boolean, title: String, bottomSheetsState: BottomSheetsState,
                           minDate: Date, maxDate: Date, selectedDate: Date) {
        this.showHoliday = showHoliday
        this.bottomSheetsState = bottomSheetsState
        this.title = title
        this.minDate = minDate
        this.maxDate = maxDate
        this.selectedDate = selectedDate
    }

    override fun getLayoutResourceId(): Int {
        return R.layout.bottom_sheet_travel_calendar
    }

    override fun initView(view: View?) {
        travelCalendarWidgetView = view?.findViewById(R.id.travel_calendar_widget_view) as TravelCalendarWidgetView
        travelCalendarWidgetView.holidayDataList.clear()
        travelCalendarWidgetView.setListener(object : TravelCalendarWidgetView.ActionListener {
            override fun onClickDate(cellDate: CellDate) {
                actionListener.onClickDate(cellDate.date)
                dismiss()
            }
        })
        travelCalendarWidgetView.renderViewTravelCalendar(minDate, maxDate, selectedDate)
        presenter.getDataHolidayCalendar(showHoliday)
    }

    override fun title(): String {
        return title
    }

    override fun state(): BottomSheetsState {
        return bottomSheetsState
    }

    override fun renderAllHolidayEvent(holidayYearList: List<HolidayResult>) {
        travelCalendarWidgetView.holidayDataList.addAll(holidayYearList)
        travelCalendarWidgetView.renderGridCalendar()
    }

    override fun renderErrorMessage(throwable: Throwable) {
        // do nothing
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroyView()
    }

    interface ActionListener {
        fun onClickDate(dateSelected: Date)
    }

    /**
     * @property actionListener should be set for the first time
     * after create instance this class
     */
    fun setListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    /**
     * Default builder will show calendar for 4 months after minimum date
     * @property setShowHoliday to show holiday or not (true/false)
     * @property setBottomSheetState to set height peek bottom sheet, set it with
     * BottomSheetsState.FULL (default) or BottomSheetsState.NORMAL
     * @property setTitle to set title bottom sheet
     * @property setMinDate to set minimum date calendar - default today
     * @property setMaxDate to set maximum date calendar - default 4 months after today
     * @property setSelectedDate to set date has been selected on calendar - default today
     */
    class Builder {

        private var showHoliday: Boolean = false
        private var bottomSheetState: BottomSheetsState = BottomSheetsState.FULL
        private var title: String = "Pilih Tanggal"
        private var minDate: Date = Date()
        private val _date: Date
            get() {
                val now = Calendar.getInstance()
                now.add(Calendar.DAY_OF_MONTH, 90)
                return now.time
            }
        private var maxDate: Date = _date
        private var selectedDate: Date = Date()

        fun setShowHoliday(showHoliday: Boolean) = apply { this.showHoliday = showHoliday }
        fun setTitle(title: String) = apply { this.title = title }
        fun setMinDate(minDate: Date) = apply { this.minDate = minDate }
        fun setMaxDate(maxDate: Date?) = apply {
            this.maxDate = maxDate ?: _date
        }

        fun setSelectedDate(selectedDate: Date) = apply { this.selectedDate = selectedDate }
        fun setBottomSheetState(bottomSheetsState: BottomSheetsState) =
                apply { this.bottomSheetState = bottomSheetState }

        fun build() = TravelCalendarBottomSheet().also {
            it.setAllData(showHoliday, title, bottomSheetState,
                    minDate, maxDate, selectedDate)
        }

    }
}
