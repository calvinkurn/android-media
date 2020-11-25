package com.tokopedia.tradein.view.viewcontrollers.bottomsheet

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.View
import android.widget.Button
import com.tokopedia.datepicker.numberpicker.NumberPicker
import com.tokopedia.datepicker.numberpicker.OnValueChangeListener
import com.tokopedia.design.component.BottomSheets
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

    override fun title(): String = getString(R.string.select_fetch_time)

    override fun state(): BottomSheetsState = BottomSheetsState.FLEXIBLE

    override fun getLayoutResourceId(): Int = R.layout.money_in_bottom_sheet_scheduled_time

    override fun initView(view: View?) {
        if (arguments != null && arguments?.getParcelableArrayList<ScheduleDate>(KEY_SCHEDULE_DATA) != null) {
            scheduleDate = arguments!!.getParcelableArrayList(KEY_SCHEDULE_DATA)!!
        }
        dateAdapter.date.clear()
        for (date in scheduleDate) {
            dateAdapter.date.add(date.dateFmt)
        }
        dateAdapter.date.add("") //Added empty element for better UX
        dateSpinner = view?.findViewById(R.id.date_spinner)
        timeSpinner = view?.findViewById(R.id.time_spinner)
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
        val courierButton = view?.findViewById<Button>(R.id.courier_btn)
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
        updateHeight()
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

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        val myDialog: BottomSheetDialog = dialog as BottomSheetDialog
        val dField = myDialog.javaClass.getDeclaredField("behavior") //This is the correct name of the variable in the BottomSheetDialog class
        dField.isAccessible = true
        val behavior = dField.get(dialog) as BottomSheetBehavior<*>
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(p0: View, p1: Float) {
            }

            override fun onStateChanged(view: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        })
    }

    fun setActionListener(actionListener: ActionListener?) {
        this.actionListener = actionListener
    }

    interface ActionListener {
        fun onScheduleButtonClick(scheduleTime: ScheduleDate.ScheduleTime, dateFmt:String)
    }
}
