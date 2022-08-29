package com.tokopedia.shopdiscount.common.bottomsheet.datepicker

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.utils.extension.parseTo
import com.tokopedia.shopdiscount.utils.extension.toCalendar
import com.tokopedia.utils.date.DateUtil
import java.util.*

object ShopDiscountDatePicker {

    interface Callback {
        fun onDatePickerSubmitted(selectedDate: Date)
    }

    fun show(
        context: Context,
        fragmentManager: FragmentManager,
        title: String,
        selectedDate: Date,
        minDate: Date,
        maxDate: Date,
        vpsPackageName: String,
        callback: Callback
    ) {
        val dateTimePicker = DateTimePickerUnify(
            context,
            minDate.toCalendar(),
            selectedDate.toCalendar(),
            maxDate.toCalendar(),
            null,
            DateTimePickerUnify.TYPE_DATETIMEPICKER
        )
        setupDateTimePicker(context, dateTimePicker, title, vpsPackageName, maxDate, callback)
        dateTimePicker.show(fragmentManager, dateTimePicker.tag)
    }

    private fun setupDateTimePicker(
        context: Context,
        dateTimePicker: DateTimePickerUnify,
        title: String,
        vpsPackageName: String,
        maxDate: Date,
        callback: Callback
    ) {
        val applyText = context.getString(R.string.shop_discount_date_picker_button_apply_text)
        val infoText = if (vpsPackageName.isNotEmpty()) {
            String.format(
                context.getString(R.string.shop_discount_date_picker_button_info_format),
                vpsPackageName,
                maxDate.parseTo(DateUtil.DEFAULT_VIEW_FORMAT)
            )
        } else {
            ""
        }
        dateTimePicker.apply {
            setTitle(title)
            if (infoText.isNotEmpty()) {
                datePickerInfo.show()
                datePickerInfo.setTextColor(
                    MethodChecker.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN600
                    )
                )
                setInfo(MethodChecker.fromHtml(infoText))
            } else {
                datePickerInfo.hide()
            }
            setInfoVisible(true)
            datePickerButton.text = applyText
            datePickerButton.setOnClickListener {
                callback.onDatePickerSubmitted(getDate().time)
                dismiss()
            }
        }
    }

}