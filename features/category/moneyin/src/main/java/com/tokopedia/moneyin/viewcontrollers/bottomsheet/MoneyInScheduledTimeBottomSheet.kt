package com.tokopedia.moneyin.viewcontrollers.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.datepicker.numberpicker.NumberPicker
import com.tokopedia.datepicker.numberpicker.OnValueChangeListener
import com.tokopedia.moneyin.adapter.DateAdapter
import com.tokopedia.moneyin.adapter.TimeAdapter
import com.tokopedia.moneyin.model.MoneyInScheduleOptionResponse.ResponseData.GetPickupScheduleOption.ScheduleDate
import com.tokopedia.moneyin.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

class MoneyInScheduledTimeBottomSheet : BottomSheetUnify() {
    private var contentView: View? = null
    private var scheduleDate: ArrayList<ScheduleDate> = arrayListOf()
    private val dateAdapter = DateAdapter()
    private val timeAdapter = TimeAdapter()
    private var currentTime: String? = null
    private var currentDate: String? = null
    private var timeSpinner: NumberPicker? = null
    private var dateSpinner: NumberPicker? = null
    private var actionListener: ActionListener? = null

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun init() {
        showCloseIcon = false
        showKnob = false
        setTitle(getString(R.string.select_fetch_time))
        contentView = View.inflate(context,
                R.layout.money_in_bottom_sheet_scheduled_time, null)
        if (arguments != null && arguments?.getParcelableArrayList<ScheduleDate>(KEY_SCHEDULE_DATA) != null) {
            scheduleDate = arguments!!.getParcelableArrayList(KEY_SCHEDULE_DATA)!!
        }
        dateAdapter.date.clear()
        for (date in scheduleDate) {
            dateAdapter.date.add(date.dateFmt)
        }
        dateAdapter.date.add("") //Added empty element for better UX
        dateSpinner = contentView?.findViewById(R.id.date_spinner)
        timeSpinner = contentView?.findViewById(R.id.time_spinner)
        dateSpinner?.apply {
            setAdapter(dateAdapter)
            setMaxValue(dateAdapter.getMaxIndex().minus(1))
            setMinValue(dateAdapter.getMinIndex())
            setWrapSelectorWheel(false)
            setWheelItemCount(3)
            setTextSize(30)
        }

        timeSpinner?.setAdapter(timeAdapter)
        currentDate = dateSpinner?.getCurrentItem()
        changeTimeByDate()
        currentTime = timeSpinner?.getCurrentItem()
        dateSpinner?.setOnValueChangedListener(object : OnValueChangeListener {
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
        val courierButton = contentView?.findViewById<UnifyButton>(R.id.courier_btn)
        courierButton?.setOnClickListener {
            scheduleDate.forEach {
                if (it.dateFmt == currentDate) {
                    it.scheduleTime.forEach { time ->
                        if (time.timeFmt == currentTime) {
                            actionListener?.onScheduleButtonClick(time, it.dateFmt)
                            dismiss()
                        }
                    }
                }
            }
        }
        setChild(contentView)
    }

    private fun changeTimeByDate() {
        scheduleDate.forEach {
            if (it.dateFmt == currentDate) {
                timeAdapter.time.clear()
                for (time in it.scheduleTime)
                    timeAdapter.time.add(time.timeFmt)
                timeAdapter.time.add("") //Added empty element for better UX
            }
        }
        timeSpinner?.setMaxValue(timeAdapter.getMaxIndex().minus(1))
        timeSpinner?.setMinValue(timeAdapter.getMinIndex())
        timeSpinner?.setWrapSelectorWheel(false)
        timeSpinner?.setWheelItemCount(3)
        timeSpinner?.setTextSize(30)
        timeAdapter.notifyDataSetChanged()
    }

    fun setActionListener(actionListener: ActionListener?) {
        this.actionListener = actionListener
    }

    interface ActionListener {
        fun onScheduleButtonClick(scheduleTime: ScheduleDate.ScheduleTime, dateFmt:String)
    }
}
