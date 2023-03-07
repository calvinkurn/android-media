package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.OnDateChangedListener
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.DateFilterUtil
import com.tokopedia.sellerhomecommon.common.const.ShcConst
import com.tokopedia.sellerhomecommon.databinding.ItemShcMonthPickerBinding
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem
import com.tokopedia.sellerhomecommon.presentation.adapter.listener.DateFilterListener
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import java.util.*

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

class MonthPickerViewHolder(
    itemView: View,
    private val listener: DateFilterListener
) : AbstractViewHolder<DateFilterItem.MonthPickerItem>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_shc_month_picker
    }

    private val binding by lazy {
        ItemShcMonthPickerBinding.bind(itemView)
    }

    override fun bind(element: DateFilterItem.MonthPickerItem) {
        with(binding) {
            tvStcPerMonthLabel.text = itemView.context.getString(R.string.shc_per_month_outer)
            radStcPerMonth.isChecked = element.isSelected

            showSelectedMonth(element)
            showCustomForm(element.isSelected)

            edtStcPerMonth.setOnClickListener {
                showMonthPicker(element)
            }

            root.setOnClickListener {
                onItemClickListener(element)
            }

            radStcPerMonth.setOnClickListener {
                onItemClickListener(element)
            }
        }
    }

    private fun onItemClickListener(element: DateFilterItem.MonthPickerItem) {
        element.isSelected = true
        showCustomForm(true)
        listener.onItemDateRangeClick(element)
    }

    private fun showCustomForm(isShown: Boolean) = with(binding) {
        if (isShown) {
            edtStcPerMonth.visible()
        } else {
            edtStcPerMonth.gone()
        }

        val lineMarginTop = if (isShown) {
            root.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
        } else {
            root.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
        }
        verLineStcCustom.setMargin(0, lineMarginTop.toInt(), 0, 0)
    }

    private fun showMonthPicker(element: DateFilterItem.MonthPickerItem) {
        val minDate = GregorianCalendar(LocaleUtils.getCurrentLocale(itemView.context)).apply {
            element.monthPickerMinDate?.let {
                time = it
            }
        }

        var selectedMonth: Date? = element.startDate

        val defaultDate = GregorianCalendar(LocaleUtils.getCurrentLocale(itemView.context)).apply {
            selectedMonth?.let {
                time = it
            }
        }

        val maxDate = GregorianCalendar(LocaleUtils.getCurrentLocale(itemView.context)).apply {
            element.monthPickerMaxDate?.let {
                time = it
            }
        }

        val listener = object : OnDateChangedListener {
            override fun onDateChanged(date: Long) {
                selectedMonth = Date(date)
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
        with(dateTimePicker) {
            setTitle(element.label)
            showDay = false

            datePickerButton.setOnClickListener {
                selectedMonth?.let { selectedMonth ->
                    val (startDate, endDate) = DateFilterUtil.getStartAndEndDateInAMonth(
                        selectedMonth
                    )
                    element.startDate = startDate
                    element.endDate = endDate
                }
                showSelectedMonth(element)
                this@MonthPickerViewHolder.listener.onItemDateRangeClick(element)
                dismiss()
            }

            setOnDismissListener {
                showDateFilterBottomSheet()
            }

            this@MonthPickerViewHolder.listener.dismissDateFilterBottomSheet()
            this@MonthPickerViewHolder.listener.showDateTimePickerBottomSheet(
                this@with,
                ShcConst.BottomSheet.TAG_MONTH_PICKER
            )
        }
    }

    private fun showDateFilterBottomSheet() {
        listener.showDateFilterBottomSheet()
    }

    private fun showSelectedMonth(element: DateFilterItem.MonthPickerItem) {
        binding.edtStcPerMonth.label = itemView.context.getString(R.string.shc_month)
        val selectedMonthFmt = DateTimeUtil.format(element.startDate?.time ?: return, DateTimeUtil.FORMAT_MMMM_YYYY)
        binding.edtStcPerMonth.valueStr = selectedMonthFmt
    }
}