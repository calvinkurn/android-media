package com.tokopedia.profilecompletion.addbod.view.widget.datepicker

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addbod.view.widget.wheelpicker.NumberPicker
import com.tokopedia.profilecompletion.addbod.view.widget.wheelpicker.OnValueChangeListener
import kotlinx.android.synthetic.main.custom_datepicker.view.*
import java.text.DateFormatSymbols
import java.util.*

/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 */

class DatePicker: FrameLayout{

    @JvmOverloads
    constructor(
            context: Context,
            attrs: AttributeSet? = null,
            defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr)

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int,
            defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    private var currentDate: Calendar = Calendar.getInstance()
    private var tempDate: Calendar = Calendar.getInstance()

    private var onDateChangedListener: OnDateChangedListener? = null
    private var isDatePickerEnabled: Boolean = true

    private val monthAdapter = MonthAdapter()
    private var dayAdapter = DayAdapter()

    private val view: View by lazy { View.inflate(context, R.layout.custom_datepicker, this) }
    private val daySpinner: NumberPicker by lazy { view.daySpinner}
    private val monthSpinner: NumberPicker by lazy { view.monthSpinner}
    private val yearSpinner: NumberPicker by lazy { view.yearSpinner}

    init {

        daySpinner.setAdapter(dayAdapter)
        daySpinner.setOnValueChangedListener(object : OnValueChangeListener {
            override fun onValueChange(oldVal: String, newVal: String) {
                tempDate.timeInMillis = currentDate.timeInMillis
                currentDate.set(tempDate.get(Calendar.YEAR), tempDate.get(Calendar.MONTH), newVal.toInt())
            }
        })

        monthSpinner.setAdapter(monthAdapter)
        monthSpinner.setOnValueChangedListener(object : OnValueChangeListener {
            override fun onValueChange(oldVal: String, newVal: String) {
                clampDaysByMonth()
                tempDate.timeInMillis = currentDate.timeInMillis
                val months = DateFormatSymbols.getInstance().months
                currentDate.set(tempDate.get(Calendar.YEAR), months.indexOf(newVal), tempDate.get(Calendar.DAY_OF_MONTH))
            }
        })

        yearSpinner.setOnValueChangedListener(object : OnValueChangeListener {
            override fun onValueChange(oldVal: String, newVal: String) {
                clampDaysByMonth()
                tempDate.timeInMillis = currentDate.timeInMillis
                currentDate.set(newVal.toInt(), tempDate.get(Calendar.MONTH), tempDate.get(Calendar.DAY_OF_MONTH))
            }
        })

        currentDate.timeInMillis = System.currentTimeMillis()

        if (importantForAccessibility == View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        }
    }

    fun init(onDateChangedListener: OnDateChangedListener){
        this.onDateChangedListener = onDateChangedListener
    }

    override fun setEnabled(enabled: Boolean) {
        daySpinner.isEnabled = enabled
        monthSpinner.isEnabled = enabled
        yearSpinner.isEnabled = enabled
        isDatePickerEnabled = enabled
    }

    override fun isEnabled(): Boolean = isDatePickerEnabled

    fun getYear(): Int = currentDate.get(Calendar.YEAR)

    fun getMonth(): Int = currentDate.get(Calendar.MONTH)

    fun getDayOfMonth(): Int = currentDate.get(Calendar.DAY_OF_MONTH)

    private fun clampDaysByMonth() {
        val selectedDay = daySpinner.getCurrentItem()

        val daysInMonth = GregorianCalendar(yearSpinner.getCurrentItem().toInt(), monthAdapter.getPosition(monthSpinner.getCurrentItem()), 1)
                .getActualMaximum(Calendar.DAY_OF_MONTH)

        dayAdapter.days.clear()
        dayAdapter.days.addAll((1..daysInMonth).toMutableList())

        daySpinner.setMaxValue(dayAdapter.getMaxIndex())
        daySpinner.setValue(selectedDay)

        daySpinner.smoothScrollToValue(selectedDay)
        dayAdapter.notifyDataSetChanged()
    }
}