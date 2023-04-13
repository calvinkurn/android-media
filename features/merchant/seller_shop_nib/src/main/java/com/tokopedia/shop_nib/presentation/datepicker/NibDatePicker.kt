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

class NibDatePicker @Inject constructor(private val param: Param) {

    companion object{
        private const val MIN_YEAR = 1970
        private const val MIN_MONTH = 0
        private const val MIN_DATE = 1
    }

    data class Param(
        val defaultDate: Date,
        val title: String,
        val buttonWording: String,
    )

    fun show(
        context: Context,
        fragmentManager: FragmentManager,
        onDateSelected: (Date) -> Unit
    ) {
        val dateTimePicker = DateTimePickerUnify(
            context,
            buildMinDate(),
            buildDefaultDate(),
            buildMaxDate(),
            null,
            DateTimePickerUnify.TYPE_DATEPICKER
        )

        dateTimePicker.apply {
            setTitle(param.title)
            setInfoVisible(true)
            datePickerButton.text = param.buttonWording
            datePickerButton.setOnClickListener {
                val selectedDate = getDate().time
                onDateSelected(selectedDate)
                dismiss()
            }
        }
        dateTimePicker.show(fragmentManager, dateTimePicker.tag)
    }

    private fun buildDefaultDate(): Calendar {
        return this.param.defaultDate.toCalendar()
    }

    private fun buildMaxDate(): Calendar {
        return Date().toCalendar()
    }

    private fun buildMinDate(): Calendar {
        val minDate = Calendar.getInstance()
        minDate.set(Calendar.YEAR, MIN_YEAR)
        minDate.set(Calendar.MONTH, MIN_MONTH)
        minDate.set(Calendar.DAY_OF_MONTH, MIN_DATE)
        return minDate
    }
}
