package com.tokopedia.events.view.customview

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.common.travel.data.entity.TravelCalendarHoliday
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.events.R
import com.tokopedia.events.di.*
import com.tokopedia.events.view.viewmodel.LocationDateModel
import com.tokopedia.unifycomponents.bottomsheet.RoundedBottomSheetDialogFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.select_event_date_bottomsheet.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class SelectEventDateBottomSheet : RoundedBottomSheetDialogFragment(), HasComponent<EventComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    var holidayViewModel: HolidayViewModel? = null
    private var selectedDatesListener: SelectedDates? = null

    var minDate: Date? = null
    var maxDate: Date? = null
    var locationModels = ArrayList<LocationDateModel>()
    val selectedDates = ArrayList<Date>()
    val dateFormatArg = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.selectEventDateBottomSheet

        activity?.run {
            holidayViewModel = ViewModelProviders.of(this, viewModelFactory).get(HolidayViewModel::class.java)
        }
    }

    override fun getComponent(): EventComponent
            = DaggerEventComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .eventModule(EventModule(context))
            .build()

    fun setSelectedDatesListener(selectedDates: SelectedDates) {
        selectedDatesListener = selectedDates
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.select_event_date_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        holidayViewModel?.getTravelHolidayDate()


        holidayViewModel?.holidayResult?.observe(this, android.arch.lifecycle.Observer {

            when (it) {
                is Success -> {
                    if (it.data.data.isNotEmpty()) {
                        renderCalender(mappingHolidayData(it.data))
                    }
                }

                is Fail -> {
                    renderCalender(arrayListOf())
                }
            }
        })
        btn_close.setOnClickListener({ view1 -> dismissAllowingStateLoss() })
    }


    fun renderCalender(legends: ArrayList<Legend>) {
        val calendar = calendar_unify.calendarPickerView

        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)

        val yesterday = Calendar.getInstance()
        yesterday.add(Calendar.DATE, 1)
        yesterday.set(Calendar.HOUR_OF_DAY, 0)
        yesterday.set(Calendar.MINUTE, 0)
        yesterday.set(Calendar.SECOND, 0)
        yesterday.set(Calendar.MILLISECOND, 0)

        arguments?.let {
            locationModels = it.getParcelableArrayList<LocationDateModel>(ARG_ACTIVE_DATES)
            if (locationModels?.size > 0) {
                for (element in locationModels) {
                    val date = dateFormatArg.parse(element.date)
                    selectedDates.add(date)
                }
            }
        }

        calendar?.init(yesterday.time, nextYear.time, legends, selectedDates)?.inMode(CalendarPickerView.SelectionMode.SINGLE)
        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                selectedDatesListener?.selectedScheduleDate(date)
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDateUnselected(date: Date) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    companion object {

        const val ARG_ACTIVE_DATES = "arg_active_dates"

        fun getInstance(models: List<LocationDateModel>): SelectEventDateBottomSheet =
                SelectEventDateBottomSheet().also {
                    it.arguments = Bundle().apply {
                        putParcelableArrayList(ARG_ACTIVE_DATES, models as ArrayList)
                    }
                }
    }

    fun mappingHolidayData(holidayData: TravelCalendarHoliday.HolidayData): ArrayList<Legend> {
        val legendList = arrayListOf<Legend>()
        for (holiday in holidayData.data) {
            legendList.add(Legend(TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, holiday.attribute.date),
                    holiday.attribute.label))
        }
        return legendList
    }

    interface SelectedDates {
        fun selectedScheduleDate(date: Date)
    }
}