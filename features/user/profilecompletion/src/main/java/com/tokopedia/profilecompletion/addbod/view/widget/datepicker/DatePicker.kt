package com.tokopedia.profilecompletion.addbod.view.widget.datepicker

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addbod.view.widget.common.LocaleUtils
import com.tokopedia.profilecompletion.addbod.view.widget.numberpicker.NumberPicker
import com.tokopedia.profilecompletion.addbod.view.widget.numberpicker.OnValueChangeListener
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
    private var maxDate: Calendar = Calendar.getInstance()
    private var minDate: Calendar = Calendar.getInstance()
    private var tempDate: Calendar = Calendar.getInstance()

    private var onDateChangedListener: OnDateChangedListener? = null
    private var isDatePickerEnabled: Boolean = true
    private var numberOfMonths: Int = 0
    private var shortMonths: Array<String> = arrayOf()

    private val monthAdapter = MonthAdapter()
    private val dayAdapter = DayAdapter()

    private val view: View by lazy { View.inflate(context, R.layout.custom_datepicker, this) }
    private val daySpinner: NumberPicker by lazy { view.daySpinner}
    private val monthSpinner: NumberPicker by lazy { view.monthSpinner}
    private val yearSpinner: NumberPicker by lazy { view.yearSpinner}

    init {

        setCurrentLocale(LocaleUtils.getCurrentLocale(context))
        monthAdapter.setLocale(LocaleUtils.getIDLocale())

        daySpinner.setOnValueChangedListener(object : OnValueChangeListener {
            override fun onValueChange(oldVal: String, newVal: String) {
                tempDate.timeInMillis = currentDate.timeInMillis
                currentDate.set(tempDate.get(Calendar.YEAR), tempDate.get(Calendar.MONTH), newVal.toInt())
            }
        })

        monthSpinner.setOnValueChangedListener(object : OnValueChangeListener {
            override fun onValueChange(oldVal: String, newVal: String) {
                clampDaysByMonth()
                tempDate.timeInMillis = currentDate.timeInMillis
                val months = DateFormatSymbols.getInstance(LocaleUtils.getIDLocale()).months
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

    fun init(currentDate: Long, minDate: Long, maxDate: Long, onDateChangedListener: OnDateChangedListener){
        setMinDate(minDate)
        setMaxDate(maxDate)
        setDate(currentDate)
        this.onDateChangedListener = onDateChangedListener

        daySpinner.setAdapter(dayAdapter)
        monthSpinner.setAdapter(monthAdapter)

        clampDaysByMonth()
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

        daySpinner.setMinValue(dayAdapter.getMinIndex())
        daySpinner.setMaxValue(dayAdapter.getMaxIndex())
        daySpinner.setValue(selectedDay)

        daySpinner.smoothScrollToValue(selectedDay)
        dayAdapter.notifyDataSetChanged()
    }

    fun setMinDate(date: Long) {
        tempDate.timeInMillis = date

        yearSpinner.setMinValue(tempDate.get(Calendar.YEAR))
        monthSpinner.setMinValue(tempDate.get(Calendar.MONTH))
        daySpinner.setMinValue(tempDate.get(Calendar.DAY_OF_MONTH))

        if (tempDate.get(Calendar.YEAR) == minDate.get(Calendar.YEAR) &&
                tempDate.get(Calendar.DAY_OF_YEAR) == minDate.get(Calendar.DAY_OF_YEAR)) {
            return
        }

        minDate.timeInMillis = date
        if (currentDate.before(minDate)) {
            currentDate.timeInMillis = minDate.timeInMillis
        }
    }

    fun setMaxDate(date: Long) {
        tempDate.timeInMillis = date

        yearSpinner.setMaxValue(tempDate.get(Calendar.YEAR))
        monthSpinner.setMaxValue(tempDate.get(Calendar.MONTH))
        daySpinner.setMaxValue(tempDate.get(Calendar.DAY_OF_MONTH))

        if (tempDate.get(Calendar.YEAR) == maxDate.get(Calendar.YEAR) &&
                tempDate.get(Calendar.DAY_OF_YEAR) == maxDate.get(Calendar.DAY_OF_YEAR)) {
            return
        }
        maxDate.timeInMillis = date
        if (currentDate.after(maxDate)) {
            currentDate.timeInMillis = maxDate.timeInMillis
        }
    }

    private fun setDate(date: Long) {
        currentDate.timeInMillis = date
        if (currentDate.before(minDate)) {
            currentDate.timeInMillis = minDate.timeInMillis
        } else if (currentDate.after(maxDate)) {
            currentDate.timeInMillis = maxDate.timeInMillis
        }

        yearSpinner.scrollTo(currentDate.get(Calendar.YEAR))
        monthSpinner.scrollTo(currentDate.get(Calendar.MONTH))
        daySpinner.scrollTo(currentDate.get(Calendar.DAY_OF_MONTH) - 1)
    }

    internal fun setOnDateChangedListener(onDateChangedListener: OnDateChangedListener){
        this.onDateChangedListener = onDateChangedListener
    }

    private fun usingNumericMonths(): Boolean {
        return Character.isDigit(shortMonths!![Calendar.JANUARY][0])
    }

    private fun setCurrentLocale(locale: Locale) {
        tempDate = getCalendarForLocale(tempDate, locale)
        minDate = getCalendarForLocale(minDate, locale)
        maxDate = getCalendarForLocale(maxDate, locale)
        currentDate = getCalendarForLocale(currentDate, locale)

        numberOfMonths = tempDate.getActualMaximum(Calendar.MONTH) + 1
        shortMonths = DateFormatSymbols.getInstance(LocaleUtils.getIDLocale()).shortMonths

        if (usingNumericMonths()) {
            for (i in 0 until numberOfMonths) {
                shortMonths[i] = String.format("%d", i + 1)
            }
        }
    }

    private fun getCalendarForLocale(oldCalendar: Calendar?, locale: Locale): Calendar {
        return if (oldCalendar == null) {
            Calendar.getInstance(locale)
        } else {
            val currentTimeMillis = oldCalendar.timeInMillis
            val newCalendar = Calendar.getInstance(locale)
            newCalendar.timeInMillis = currentTimeMillis
            newCalendar
        }
    }
}