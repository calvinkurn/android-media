package com.tokopedia.shop.flashsale.presentation.creation.information

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.constant.LocaleConstant
import com.tokopedia.shop.flashsale.common.extension.toCalendar
import java.util.*
import javax.inject.Inject

class TimePickerHandler @Inject constructor(private val param : Param)  {

    data class Param(val date: Date, val title: String, val info: String, val buttonWording: String)

    fun show(
        context: Context,
        fragmentManager: FragmentManager,
        onTimePicked: (Date) -> Unit
    ) {

        val minTime = GregorianCalendar(LocaleConstant.INDONESIA).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
        }
        val defaultTime = GregorianCalendar(LocaleConstant.INDONESIA)
        val maxTime = GregorianCalendar(LocaleConstant.INDONESIA).apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
        }


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
                val dateTime = buildSelectedDateInstance(param.date, hour, minute)
                onTimePicked(dateTime)
                dismiss()
            }
        }
        dateTimePicker.show(fragmentManager, dateTimePicker.tag)
    }

    private fun Date.extractHour(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    private fun Date.extractMinute(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = this
        return calendar.get(Calendar.MINUTE)
    }

    private fun buildSelectedDateInstance(date: Date, hour: Int, minute: Int): Date {
        val calendar = date.toCalendar()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, Constant.ZERO)
        return calendar.time
    }

}