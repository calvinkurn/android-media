package com.tokopedia.tradein.view.viewcontrollers

import android.os.Bundle
import android.view.View
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.profilecompletion.addbod.view.widget.numberpicker.NumberPicker
import com.tokopedia.profilecompletion.addbod.view.widget.numberpicker.OnValueChangeListener
import com.tokopedia.tradein.R
import com.tokopedia.tradein.adapter.DateAdapter
import com.tokopedia.tradein.adapter.TimeAdapter
import com.tokopedia.tradein.model.MoneyInScheduleOptionResponse.ResponseData.GetPickupScheduleOption.ScheduleDate

class MoneyInScheduledTimeBottomSheet : BottomSheets() {
    private var scheduleDate: ArrayList<ScheduleDate> = arrayListOf()
    private val dateAdapter = DateAdapter()
    private val timeAdapter = TimeAdapter()
    private var currentTime: String? = null
    private var currentDate: String? = null

    companion object {
        private const val KEY_SCHEDULE_DATA = "KEY_SCHEDULE_DATA"

        @JvmStatic
        fun newInstance(scheduleDate: ArrayList<ScheduleDate>): MoneyInScheduledTimeBottomSheet {
            return MoneyInScheduledTimeBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(KEY_SCHEDULE_DATA, scheduleDate)
                }
            }
        }
    }

    override fun title(): String = getString(R.string.select_fetch_time)

    override fun state(): BottomSheetsState = BottomSheetsState.FLEXIBLE

    override fun getLayoutResourceId(): Int = R.layout.money_in_bottom_sheet_scheduled_time

    override fun initView(view: View?) {
        if (arguments != null) {
            scheduleDate = arguments!!.getParcelableArrayList(KEY_SCHEDULE_DATA)
        }
        for (date in scheduleDate) {
            dateAdapter.date.add(date.dateFmt)
        }
        val daySpinner: NumberPicker? = view?.findViewById(R.id.date_spinner)
        val timeSpinner: NumberPicker? = view?.findViewById(R.id.time_spinner)
        daySpinner?.setAdapter(dateAdapter)
        timeSpinner?.setAdapter(timeAdapter)
        currentDate = daySpinner?.getCurrentItem()
        changeTimeByDate()
        daySpinner?.setOnValueChangedListener(object : OnValueChangeListener {
            override fun onValueChange(oldVal: String, newVal: String) {
                currentDate = newVal
                changeTimeByDate()
            }

        })
        timeSpinner?.setOnValueChangedListener(object : OnValueChangeListener {
            override fun onValueChange(oldVal: String, newVal: String) {
                currentTime = newVal
            }
        })
        updateHeight()
    }

    private fun changeTimeByDate() {
        scheduleDate.forEach {
            if (it.dateFmt == currentDate)
                for (time in it.scheduleTime)
                    timeAdapter.time.add(time.timeFmt)
        }
    }
}