package com.tokopedia.shopdiscount.common.bottomsheet.datepicker

import android.content.Context
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.utils.extension.toCalendar
import java.util.*

object ShopDiscountDatePicker {

//    private const val COUPON_START_DATE_OFFSET_IN_HOUR = 3
    private const val EXTRA_DAYS_COUPON = 31
//    private const val TIME_PICKER_TIME_INTERVAL_IN_MINUTE = 30

    interface Callback {
        fun onDatePickerSubmitted(selectedDate: Date)
    }

    private fun getCouponStartDate(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.add(
            Calendar.HOUR_OF_DAY,
            Int.ZERO
        )
        return calendar
    }

    private fun getCouponEndDate(currentStartDate: Date): Calendar {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = currentStartDate.time
        calendar.add(
            Calendar.DAY_OF_MONTH,
            EXTRA_DAYS_COUPON
        )
        return calendar
    }

    fun show(
        context: Context,
        fragmentManager: FragmentManager,
        title: String,
        selectedDate: Date,
        callback: Callback
    ) {
//        val formattedStartDate = getCouponStartDate().time.parseTo(DateTimeUtils.DATE_FORMAT)
//        val info = "INFO"
        val buttonText = "Pilih"

        val dateTimePicker = DateTimePickerUnify(
            context,
            getCouponStartDate(),
            selectedDate.toCalendar(),
            getCouponEndDate(selectedDate),
            null,
            DateTimePickerUnify.TYPE_DATETIMEPICKER
        )

        dateTimePicker.apply {
            setTitle(title)
//            setInfo(info)
            setInfoVisible(true)
            setStyle(DialogFragment.STYLE_NORMAL, R.style.ShopDiscountDialogStyle)
            datePickerButton.text = buttonText
            datePickerButton.setOnClickListener {
                callback.onDatePickerSubmitted(getDate().time)
                dismiss()
            }
        }
        dateTimePicker.show(fragmentManager, dateTimePicker.tag)
    }

}