package com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.LocaleConstant
import com.tokopedia.shop.flashsale.common.extension.extractHour
import com.tokopedia.shop.flashsale.common.extension.extractMinute
import com.tokopedia.shop.flashsale.common.extension.toCalendar
import java.util.*
import javax.inject.Inject

class TimePickerHandler @Inject constructor(private val param: Param) {

    data class Param(
        val selectedDateFromCalendar: Date,
        val defaultDate: Date,
        val minimumDate: Date,
        val maximumDate: Date,
        val title: String,
        val info: String,
        val buttonWording: String
    )

    fun show(
        context: Context,
        fragmentManager: FragmentManager,
        onTimePicked: (Date) -> Unit
    ) {
        val minTime = buildMinTime()
        val defaultTime = buildDefaultTime()
        val maxTime = buildMaxTime()

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
        return if (param.selectedDateFromCalendar.before(param.minimumDate)) {
            GregorianCalendar(LocaleConstant.INDONESIA).apply {
                set(Calendar.HOUR_OF_DAY, param.minimumDate.extractHour())
                set(Calendar.MINUTE, param.minimumDate.extractMinute())
            }
        } else {
            GregorianCalendar(LocaleConstant.INDONESIA).apply {
                set(Calendar.HOUR_OF_DAY, Constant.ZERO)
                set(Calendar.MINUTE, Constant.ZERO)
            }
        }
    }

    private fun buildDefaultTime(): GregorianCalendar {
        return GregorianCalendar(LocaleConstant.INDONESIA).apply {
            set(Calendar.HOUR_OF_DAY, param.defaultDate.extractHour())
            set(Calendar.MINUTE, param.defaultDate.extractMinute())
            set(Calendar.SECOND, 0)
        }
    }

    private fun buildMaxTime(): GregorianCalendar {
        return GregorianCalendar(LocaleConstant.INDONESIA).apply {
            set(Calendar.HOUR_OF_DAY, param.maximumDate.extractHour())
            set(Calendar.MINUTE, param.maximumDate.extractMinute())
            set(Calendar.SECOND, 0)
        }
    }
}