package com.tokopedia.saldodetails.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.view.Window
import android.widget.DatePicker
import java.util.*

class SaldoDatePickerUtil(private val activity: Activity) {
    private var year: Int
    private var month: Int
    private var day: Int
    private val maxYear = 0
    private val minYear = 0
    private val maxDate: Long = 0
    private val minDate: Long = 0
    private var listener: OnDateSelectedListener? = null
    private val calendar: Calendar = Calendar.getInstance()

    init {
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH] + 1
        day = calendar[Calendar.DAY_OF_MONTH]
    }

    fun setDate(day: Int, month: Int, year: Int) {
        this.year = year
        this.month = month
        this.day = day
    }

    private fun showDatePickerSpinner(listener: OnDateSelectedListener?) {
        IS_PICKING_DATE = true
        this.listener = listener
        val dpd = DatePickerDialog(activity, { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int -> this.listener!!.onDateSelected(year, monthOfYear + 1, dayOfMonth) }, year, month - 1, day)
        dpd.setCanceledOnTouchOutside(false)
        dpd.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (maxYear != 0) {
            val maxDate = Calendar.getInstance()
            maxDate[maxDate[Calendar.YEAR] - maxYear, maxDate.getMaximum(Calendar.MONTH)] = maxDate.getMaximum(Calendar.DATE)
            dpd.datePicker.maxDate = maxDate.timeInMillis
        }
        if (minYear != 0) {
            val minDate = Calendar.getInstance()
            minDate[minDate[Calendar.YEAR] - minYear, minDate.getMinimum(Calendar.MONTH)] = minDate.getMinimum(Calendar.DATE)
            dpd.datePicker.minDate = minDate.timeInMillis - 1000
        }
        if (minDate != 0L) {
            dpd.datePicker.minDate = minDate
        }
        if (maxDate != 0L) {
            dpd.datePicker.maxDate = maxDate
        }
        dpd.setOnDismissListener { IS_PICKING_DATE = false }
        dpd.setOnCancelListener { IS_PICKING_DATE = false }
        dpd.setTitle(null)
        dpd.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dpd.show()
    }

    fun datePickerCalendar(listener: OnDateSelectedListener?) {
        if (!IS_PICKING_DATE) {
            showDatePickerSpinner(listener)
        }
    }

    interface OnDateSelectedListener {
        fun onDateSelected(year: Int, month: Int, dayOfMonth: Int)
    }

    companion object {
        var IS_PICKING_DATE = false
    }
}