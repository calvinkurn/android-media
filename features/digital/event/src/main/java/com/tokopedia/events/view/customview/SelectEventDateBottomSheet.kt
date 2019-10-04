package com.tokopedia.events.view.customview

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.calendar.Legend
import com.tokopedia.common.travel.data.entity.TravelCalendarHoliday
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.events.R
import com.tokopedia.events.view.viewmodel.LocationDateModel
import com.tokopedia.unifycomponents.bottomsheet.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.select_event_date_bottomsheet.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class SelectEventDateBottomSheet : RoundedBottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var holidayViewModel: HolidayViewModel

    var minDate : Date? = null
    var maxDate : Date? = null
    var locationModels = ArrayList<LocationDateModel>()
    val selectedDates = ArrayList<Date>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        activity?.run {
//            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
//            holidayViewModel = viewModelProvider.get(HolidayViewModel::class.java)
//        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.select_event_date_bottomsheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        holidayViewModel.getTravelHolidayDate()

        renderCalender()
//        holidayViewModel.holidayResult.observe(this, Observer {
//            when (it) {
//                is Success -> {
//                    if (it.data.data.isNotEmpty()) renderCalendar(mappingHolidayData(it.data))
//                }
//                is Fail -> {
//                    renderCalendar(arrayListOf())
//                }
//            }
//        })
    }


    fun renderCalender() {
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
            if (locationModels.size > 0) {
                for (element in locationModels) {
                    selectedDates.add(element.date)
                }
            }
        }

        calendar?.init(yesterday.time, nextYear.time, arrayListOf(), selectedDates)?.inMode(CalendarPickerView.SelectionMode.SINGLE)
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

    fun mappingHolidayData(holidayData: TravelCalendarHoliday.HolidayData): List<Legend> {
        val legendList = arrayListOf<Legend>()
        for (holiday in holidayData.data) {
            legendList.add(Legend(TravelDateUtil.stringToDate(TravelDateUtil.YYYY_MM_DD, holiday.attribute.date),
                    holiday.attribute.label))
        }
        return legendList
    }
}