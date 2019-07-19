package com.tokopedia.profilecompletion.addbod.view.widget.datepicker

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addbod.view.widget.wheelpicker.NumberPicker
import com.tokopedia.profilecompletion.addbod.view.widget.wheelpicker.OnValueChangeListener
import kotlinx.android.synthetic.main.custom_datepicker.view.*
import java.text.DateFormatSymbols
import java.time.Year
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

    private var tempDate: Calendar = Calendar.getInstance()
    private var minDate: Calendar = Calendar.getInstance()
    private var maxDate: Calendar = Calendar.getInstance()
    private var currentDate: Calendar = Calendar.getInstance()
    private var shortMonths: Array<String> = emptyArray()

    private var onDateChangedListener: OnDateChangedListener? = null
    private var isDayShown: Boolean = true
    private var isDatePickerEnabled: Boolean = true
    private var numberOfMonths: Int = 0

    private val monthAdapter = MonthAdapter()
    private var dayAdapter = DayAdapter()

    private val view: View by lazy { View.inflate(context, R.layout.custom_datepicker, this) }
    private val daySpinner: NumberPicker by lazy { view.daySpinner}
    private val monthSpinner: NumberPicker by lazy { view.monthSpinner}
    private val yearSpinner: NumberPicker by lazy { view.yearSpinner}

    init {
        setCurrentLocale(Locale.getDefault())

//        daySpinner.setAdapter(dayAdapter)
        daySpinner.setOnValueChangedListener(object : OnValueChangeListener {
            override fun onValueChange(oldVal: Int, newVal: Int) {
                tempDate.timeInMillis = currentDate.timeInMillis
                val maxDayOfMonth = tempDate.getActualMaximum(Calendar.DAY_OF_MONTH)
                if (oldVal == maxDayOfMonth && newVal == 1) {
                    tempDate.add(Calendar.DAY_OF_MONTH, 1)
                } else if (oldVal == 1 && newVal == maxDayOfMonth) {
                    tempDate.add(Calendar.DAY_OF_MONTH, -1)
                } else {
                    tempDate.add(Calendar.DAY_OF_MONTH, newVal - oldVal)
                }

                updateDate(tempDate.time)
            }
        })

//        monthSpinner.setAdapter(monthAdapter)
        monthSpinner.setOnValueChangedListener(object : OnValueChangeListener {
            override fun onValueChange(oldVal: Int, newVal: Int) {
                tempDate.timeInMillis = currentDate.timeInMillis
                if (oldVal == 11 && newVal == 0) {
                    tempDate.add(Calendar.MONTH, 1)
                } else if (oldVal == 0 && newVal == 11) {
                    tempDate.add(Calendar.MONTH, -1)
                } else {
                    tempDate.add(Calendar.MONTH, newVal - oldVal)
                }

                updateDate(tempDate.time)
//                dayAdapter.notifyDataSetChanged()
            }
        })
        monthSpinner.setMinValue(0)
        monthSpinner.setMaxValue(numberOfMonths - 1)

        yearSpinner.setOnValueChangedListener(object : OnValueChangeListener {
            override fun onValueChange(oldVal: Int, newVal: Int) {
                tempDate.timeInMillis = currentDate.timeInMillis
                tempDate.set(Calendar.YEAR, newVal)
                updateDate(tempDate.time)
//                dayAdapter.notifyDataSetChanged()
            }
        })

        currentDate.timeInMillis = System.currentTimeMillis()

        if (importantForAccessibility == View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES
        }
    }

    fun init(date: Date, isDayShown: Boolean){
        this.isDayShown = isDayShown
        setDate(date)
        updateSpinners()
    }

    fun init(date: Date, isDayShown: Boolean,
             onDateChangedListener: OnDateChangedListener){
        this.isDayShown = isDayShown
        this.onDateChangedListener = onDateChangedListener
        setDate(date)
        updateSpinners()
        notifyDateChanged()
    }

    override fun setEnabled(enabled: Boolean) {
        daySpinner.isEnabled = enabled
        monthSpinner.isEnabled = enabled
        yearSpinner.isEnabled = enabled
        isDatePickerEnabled = enabled
    }

    override fun isEnabled(): Boolean = isDatePickerEnabled

    private fun isNewDate(
            year: Int,
            month: Int,
            dayOfMonth: Int
    ): Boolean = (currentDate.get(Calendar.YEAR) != year
                || currentDate.get(Calendar.MONTH) != month
                || currentDate.get(Calendar.DAY_OF_MONTH) != dayOfMonth)

    private fun setDate(date: Date) {
        currentDate.time = date
        if (currentDate.before(minDate)) {
            currentDate.timeInMillis = minDate.timeInMillis
        } else if (currentDate.after(maxDate)) {
            currentDate.timeInMillis = maxDate.timeInMillis
        }
    }

    private fun updateDate(date: Date) {
        val calendar = Calendar.getInstance()
        calendar.time = date
        if (isNewDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))) {
            setDate(date)
            updateSpinners()
            notifyDateChanged()
        }
    }

    fun setMinDate(date: Long){
        tempDate.timeInMillis = date
        if(tempDate.get(Calendar.YEAR) == minDate.get(Calendar.YEAR) &&
                tempDate.get(Calendar.YEAR) == minDate.get(Calendar.DAY_OF_YEAR)){
            return
        }

        minDate.timeInMillis = date
        if(currentDate.before(minDate)){
            currentDate.timeInMillis = minDate.timeInMillis
        }

        updateSpinners()
    }

    fun setMaxDate(date: Long){
        tempDate.timeInMillis = date
        if(tempDate.get(Calendar.YEAR) == maxDate.get(Calendar.YEAR) &&
                tempDate.get(Calendar.YEAR) == maxDate.get(Calendar.DAY_OF_YEAR)){
            return
        }

        maxDate.timeInMillis = date
        if(currentDate.after(maxDate)){
            currentDate.timeInMillis = maxDate.timeInMillis
        }

        updateSpinners()
    }

    fun getYear(): Int = currentDate.get(Calendar.YEAR)

    fun getMonth(): Int = currentDate.get(Calendar.MONTH)

    fun getDayOfMonth(): Int = currentDate.get(Calendar.DAY_OF_MONTH)

    private fun setCurrentLocale(locale: Locale) {
        tempDate = getCalendarForLocale(tempDate, locale)
        minDate = getCalendarForLocale(minDate, locale)
        maxDate = getCalendarForLocale(maxDate, locale)
        currentDate = getCalendarForLocale(currentDate, locale)

        numberOfMonths = tempDate.getActualMaximum(Calendar.MONTH) + 1
        shortMonths = DateFormatSymbols().shortMonths

        if (usingNumericMonths()) {
            for (i in 0 until numberOfMonths) {
                shortMonths[i] = String.format("%d", i + 1)
            }
        }

        val datePlusOneMonth = currentDate.time

        Calendar.getInstance().time = datePlusOneMonth
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

    private fun usingNumericMonths(): Boolean {
        return Character.isDigit(shortMonths[Calendar.JANUARY][0])
    }

    private fun updateSpinners(){
        daySpinner.visibility = if(isDayShown) View.VISIBLE else View.GONE
        when (currentDate) {
            minDate -> {
                daySpinner.setMinValue(currentDate.get(Calendar.DAY_OF_MONTH))
                daySpinner.setMaxValue(currentDate.getActualMaximum(Calendar.DAY_OF_MONTH))
                daySpinner.setWrapSelectorWheel(false)
                monthSpinner.setMinValue(currentDate.get(Calendar.MONTH))
                monthSpinner.setMaxValue(currentDate.getActualMaximum(Calendar.MONTH))
                monthSpinner.setWrapSelectorWheel(false)
            }
            maxDate -> {
                daySpinner.setMinValue(currentDate.getActualMinimum(Calendar.DAY_OF_MONTH))
                daySpinner.setMaxValue(currentDate.get(Calendar.DAY_OF_MONTH))
                daySpinner.setWrapSelectorWheel(false)
                monthSpinner.setMinValue(currentDate.getActualMinimum((Calendar.MONTH)))
                monthSpinner.setMaxValue(currentDate.get(Calendar.MONTH))
                monthSpinner.setWrapSelectorWheel(false)
            }
            else -> {
                daySpinner.setMinValue(1)
                daySpinner.setMaxValue(currentDate.getActualMinimum(Calendar.DAY_OF_MONTH))
                daySpinner.setWrapSelectorWheel(true)
                monthSpinner.setMinValue(0)
                monthSpinner.setMaxValue(11)
                monthSpinner.setWrapSelectorWheel(true)
            }
        }
    }

    private fun notifyDateChanged(){
        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_SELECTED)
        onDateChangedListener?.onDateChanged(currentDate.time)

        yearSpinner.scrollToValue(currentDate.get(Calendar.YEAR).toString())
        monthSpinner.scrollToValue(currentDate.get(Calendar.MONTH).toString())
        daySpinner.scrollToValue(currentDate.get(Calendar.DAY_OF_MONTH).toString())
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return if(superState != null)
            SavedState(superState, currentDate, minDate, maxDate, isDayShown)
        else null
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        currentDate = Calendar.getInstance()
        currentDate.timeInMillis = ss.currentDate
        minDate = Calendar.getInstance()
        minDate.timeInMillis = ss.minDate
        maxDate = Calendar.getInstance()
        maxDate.timeInMillis = ss.maxDate
        updateSpinners()
    }

    private class SavedState : View.BaseSavedState {
        internal val currentDate: Long
        internal val minDate: Long
        internal val maxDate: Long
        internal val isDaySpinnerShown: Boolean

        internal constructor(superState: Parcelable,
                             currentDate: Calendar,
                             minDate: Calendar,
                             maxDate: Calendar,
                             isDaySpinnerShown: Boolean) : super(superState) {
            this.currentDate = currentDate.timeInMillis
            this.minDate = minDate.timeInMillis
            this.maxDate = maxDate.timeInMillis
            this.isDaySpinnerShown = isDaySpinnerShown
        }

        private constructor(parcel: Parcel) : super(parcel) {
            this.currentDate = parcel.readLong()
            this.minDate = parcel.readLong()
            this.maxDate = parcel.readLong()
            this.isDaySpinnerShown = parcel.readByte().toInt() != 0
        }

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeLong(currentDate)
            dest.writeLong(minDate)
            dest.writeLong(maxDate)
            dest.writeByte(if (isDaySpinnerShown) 1.toByte() else 0.toByte())
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {

                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}