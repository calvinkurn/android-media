package com.tokopedia.shop_nib.presentation.datepicker

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tokopedia.campaign.utils.constant.LocaleConstant
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.toCalendar
import com.tokopedia.shop_nib.util.extension.extractHour
import com.tokopedia.shop_nib.util.extension.extractMinute
import java.util.*
import javax.inject.Inject

class NibDatePickerBottomSheet @Inject constructor(private val param: Param) {

    data class Param(
        val selectedDateFromCalendar: Date,
        val defaultDate: Date,
        val minimumDate: Date,
        val maximumDate: Date,
        val title: String,
        val buttonWording: String,
    )

    fun show(
        context: Context,
        fragmentManager: FragmentManager,
        onDateSelected: (Date) -> Unit
    ) {
        val minTime = Date(1970, 1, 1).toCalendar()
        val defaultTime = buildDefaultTime()
        val maxTime = Date().toCalendar()

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
            setInfoVisible(true)
            datePickerButton.text = param.buttonWording
            datePickerButton.setOnClickListener {
                val selectedDate = getDate().time
                val hour = selectedDate.extractHour()
                val minute = selectedDate.extractMinute()
                val dateTime = buildSelectedDateInstance(param.selectedDateFromCalendar, hour, minute)
                onDateSelected(dateTime)
                dismiss()
            }
        }
        dateTimePicker.show(fragmentManager, dateTimePicker.tag)
    }


    private fun buildSelectedDateInstance(date: Date, hour: Int, minute: Int): Date {
        val calendar = date.toCalendar()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, Int.ZERO)
        return calendar.time
    }


    private fun buildDefaultTime(): GregorianCalendar {
        val hour = param.defaultDate.extractHour()
        val minute = param.defaultDate.extractMinute()
        return GregorianCalendar(LocaleConstant.INDONESIA).apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
    }
}
