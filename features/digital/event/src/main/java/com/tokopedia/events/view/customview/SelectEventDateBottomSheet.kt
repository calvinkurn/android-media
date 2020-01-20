package com.tokopedia.events.view.customview

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.events.R
import com.tokopedia.events.di.*
import com.tokopedia.events.view.viewmodel.LocationDateModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.travelcalendar.data.entity.TravelCalendarHoliday
import com.tokopedia.unifycomponents.bottomsheet.RoundedBottomSheetDialogFragment
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.select_event_date_bottomsheet.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class SelectEventDateBottomSheet : RoundedBottomSheetDialogFragment(), HasComponent<EventViewModelComponent> {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var holidayViewModel: HolidayViewModel
    private var selectedDatesListener: SelectedDates? = null

    var minDate: Date? = null
    var maxDate: Date? = null
    var isFirstTime: Boolean = true
    var locationModels = ArrayList<LocationDateModel>()
    val selectedDates = ArrayList<Date>()
    val dateFormatArg = SimpleDateFormat("dd-MM-yyyy", Locale("in", "ID", ""))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            holidayViewModel = viewModelProvider.get(HolidayViewModel::class.java)
        }
    }

    override fun getComponent(): EventViewModelComponent {
     return DaggerEventViewModelComponent.builder().baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
             .build()
     }

    fun setSelectedDatesListener(selectedDates: SelectedDates) {
        selectedDatesListener = selectedDates
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.select_event_date_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loading_progress_bar.visible()

        holidayViewModel?.getTravelHolidayDate()
        holidayViewModel?.holidayResult?.observe(this, androidx.lifecycle.Observer {
            loading_progress_bar.hide()
            when (it) {
                is Success -> {
                    if (isFirstTime && it.data.data.isNotEmpty()) {
                        renderCalender(mappingHolidayData(it.data))
                        isFirstTime = false
                    }
                }
                is Fail -> {
                    renderCalender(arrayListOf())
                }
            }
        })
        btn_close.setOnClickListener({ view1 -> dismiss() })
    }


    private fun renderCalender(legends: ArrayList<Legend>) {
        val calendar = calendar_unify.calendarPickerView

        val nextYear = Calendar.getInstance()
        nextYear.add(Calendar.YEAR, 1)

        arguments?.let {
            locationModels = it.getParcelableArrayList<LocationDateModel>(ARG_ACTIVE_DATES)
            if (locationModels?.size > 0) {
                for (element in locationModels) {
                    val date = dateFormatArg.parse(element.date)
                    selectedDates.add(date)
                }
            }
        }

        calendar?.init(selectedDates[0], nextYear.time, legends, selectedDates)?.inMode(CalendarPickerView.SelectionMode.SINGLE)
        calendar?.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date) {
                selectedDatesListener?.selectedScheduleDate(date)

                GlobalScope.launch {
                    delay(300)
                    dismiss()
                }
            }
            override fun onDateUnselected(date: Date) {
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

    private fun mappingHolidayData(holidayData: TravelCalendarHoliday.HolidayData): ArrayList<Legend> {
        val legendList = arrayListOf<Legend>()
        legendList.clear()
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