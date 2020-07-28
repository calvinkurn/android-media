package com.tokopedia.statistic.presentation.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.OnDateChangedListener
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.model.DateFilterItem
import kotlinx.android.synthetic.main.item_stc_month_picker.view.*
import java.util.*

/**
 * Created By @ilhamsuaib on 27/07/20
 */

class MonthPickerViewHolder(
        itemView: View?,
        private val fm: FragmentManager
) : AbstractViewHolder<DateFilterItem.MonthPickerItem>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_month_picker
    }

    override fun bind(element: DateFilterItem.MonthPickerItem) {
        with(itemView) {
            tvStcPerMonthLabel.text = element.label
            radStcPerMonth.isChecked = element.isSelected

            setOnClickListener {
                showMonthPicker(element)
            }
        }
    }

    private fun showMonthPicker(element: DateFilterItem.MonthPickerItem) {
        val minDate = GregorianCalendar(LocaleUtils.getCurrentLocale(itemView.context)).apply {
            val minMonth = get(Calendar.MONTH).minus(2)
            set(Calendar.MONTH, minMonth)
        }
        val defaultDate = GregorianCalendar(LocaleUtils.getCurrentLocale(itemView.context))
        val maxDate = GregorianCalendar(LocaleUtils.getCurrentLocale(itemView.context))
        val listener = object : OnDateChangedListener {
            override fun onDateChanged(date: Long) {

            }
        }
        val dateTimePicker = DateTimePickerUnify(
                itemView.context,
                minDate,
                defaultDate,
                maxDate,
                listener,
                DateTimePickerUnify.TYPE_DATEPICKER
        )
        dateTimePicker.showDay = false
        dateTimePicker.show(fm, element.label)
    }
}