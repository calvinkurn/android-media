package com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.LocaleConstant
import com.tokopedia.shop.flashsale.common.extension.extractHour
import com.tokopedia.shop.flashsale.common.extension.extractMinute
import com.tokopedia.shop.flashsale.common.extension.toCalendar
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import javax.inject.Inject

class TimePickerHandler @Inject constructor(private val param: Param) {

    companion object {
        private const val LAST_HOUR_OF_A_DAY = 23
        private const val LAST_MINUTE = 59
    }


    data class Param(
        val mode : TimePickerSelectionMode,
        val selectedDateFromCalendar: Date,
        val defaultDate: Date,
        val minimumDate: Date,
        val maximumDate: Date,
        val title: String,
        val info: String,
        val buttonWording: String,
        val isUsingVpsPackage: Boolean
    )

    fun show(
        context: Context,
        fragmentManager: FragmentManager,
        onTimePicked: (Date) -> Unit
    ) {
        val minTime = buildMinTime()
        val defaultTime = buildDefaultTime()
        val maxTime = buildMaxTime(param.isUsingVpsPackage)

        val dateTimePicker = DateTimePickerUnify(
            context,
            minTime,
            defaultTime,
            maxTime,
            null,
            DateTimePickerUnify.TYPE_TIMEPICKER
        )

        dateTimePicker.apply {
            setTitle(param.title)
            setInfo(param.info)
            setInfoVisible(true)
            datePickerButton.text = param.buttonWording
            datePickerButton.setOnClickListener {
                val selectedDate = getDate().time
                val hour = selectedDate.extractHour()
                val minute = selectedDate.extractMinute()
                val dateTime = buildSelectedDateInstance(param.selectedDateFromCalendar, hour, minute)
                onTimePicked(dateTime)
                dismiss()
            }
        }
        dateTimePicker.show(fragmentManager, dateTimePicker.tag)
    }


    private fun buildSelectedDateInstance(date: Date, hour: Int, minute: Int): Date {
        val calendar = date.toCalendar()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, Constant.ZERO)
        return calendar.time
    }

    private fun buildMinTime(): GregorianCalendar {
        val isSameDay = isSameDay(param.minimumDate, param.selectedDateFromCalendar)
        return if (isSameDay) {
            GregorianCalendar(LocaleConstant.INDONESIA).apply {
                set(Calendar.HOUR_OF_DAY, param.minimumDate.extractHour())
                set(Calendar.MINUTE, param.minimumDate.extractMinute())
            }
        } else {
            GregorianCalendar(LocaleConstant.INDONESIA).apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
            }
        }
    }

    private fun buildDefaultTime(): GregorianCalendar {
        val hour = param.defaultDate.extractHour()
        val minute = param.defaultDate.extractMinute()
        return GregorianCalendar(LocaleConstant.INDONESIA).apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
    }

    private fun buildMaxTime(isUsingVpsPackage: Boolean): Calendar {
        return if (isUsingVpsPackage) {
            handleVpsPackageMaxTime()
        } else {
            GregorianCalendar(LocaleConstant.INDONESIA).apply {
                set(Calendar.HOUR_OF_DAY, LAST_HOUR_OF_A_DAY)
                set(Calendar.MINUTE, LAST_MINUTE)
            }
        }
    }

    private fun isSameDay(startDate: Date, endDate: Date): Boolean {
        val startDateCalendar = Calendar.getInstance()
        startDateCalendar.time = startDate
        val startDateDayOfYear = startDateCalendar.get(Calendar.DAY_OF_YEAR)

        val endDateCalendar = Calendar.getInstance()
        endDateCalendar.time = endDate
        val endDateDayOfYear = endDateCalendar.get(Calendar.DAY_OF_YEAR)

        return startDateDayOfYear == endDateDayOfYear
    }

    private fun handleVpsPackageMaxTime(): GregorianCalendar {
        val isSelectedDateIsTheLastVpsPackageDayActive = isSameDay(param.maximumDate, param.selectedDateFromCalendar)
        return if (isSelectedDateIsTheLastVpsPackageDayActive) {
            val maxHour = param.maximumDate.extractHour()
            val maxMinute = param.maximumDate.extractMinute()
            GregorianCalendar(LocaleConstant.INDONESIA).apply {
                set(Calendar.HOUR_OF_DAY, maxHour)
                set(Calendar.MINUTE, maxMinute)
            }
        } else {
            GregorianCalendar(LocaleConstant.INDONESIA).apply {
                set(Calendar.HOUR_OF_DAY, LAST_HOUR_OF_A_DAY)
                set(Calendar.MINUTE, LAST_MINUTE)
            }
        }

    }
}
