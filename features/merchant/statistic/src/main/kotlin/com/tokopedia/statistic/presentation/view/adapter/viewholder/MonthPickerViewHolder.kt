package com.tokopedia.statistic.presentation.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.datepicker.LocaleUtils
import com.tokopedia.datepicker.OnDateChangedListener
import com.tokopedia.datepicker.datetimepicker.DateTimePickerUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.view.bottomsheet.DateFilterBottomSheet
import com.tokopedia.statistic.presentation.view.model.DateFilterItem
import kotlinx.android.synthetic.main.item_stc_month_picker.view.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created By @ilhamsuaib on 27/07/20
 */

class MonthPickerViewHolder(
        itemView: View?,
        private val fm: FragmentManager,
        private val onSelected: (DateFilterItem) -> Unit
) : AbstractViewHolder<DateFilterItem.MonthPickerItem>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_month_picker
    }

    private val dateFilterBottomSheet by lazy {
        fm.findFragmentByTag(DateFilterBottomSheet.TAG) as? DateFilterBottomSheet
    }

    override fun bind(element: DateFilterItem.MonthPickerItem) {
        with(itemView) {
            tvStcPerMonthLabel.text = element.label
            radStcPerMonth.isChecked = element.isSelected

            showSelectedMonth(element)
            showCustomForm(element.isSelected)

            edtStcPerMonth.setOnClickListener {
                showMonthPicker(element)
            }

            setOnClickListener {
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
        onSelected(element)
    }

    private fun showCustomForm(isShown: Boolean) = with(itemView) {
        if (isShown) {
            edtStcPerMonth.visible()
        } else {
            edtStcPerMonth.gone()
        }

        val lineMarginTop = if (isShown) {
            context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
        } else {
            context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
        }
        verLineStcCustom.setMargin(0, lineMarginTop.toInt(), 0, 0)
    }

    private fun showMonthPicker(element: DateFilterItem.MonthPickerItem) {
        val minDate = GregorianCalendar(LocaleUtils.getCurrentLocale(itemView.context)).apply {
            val last3months = TimeUnit.DAYS.toMillis(30).times(3)
            val minDateMillis = timeInMillis.minus(last3months)
            timeInMillis = minDateMillis
        }

        var selectedMonth: Date? = element.startDate

        val defaultDate = GregorianCalendar(LocaleUtils.getCurrentLocale(itemView.context)).apply {
            if (selectedMonth != null) {
                time = selectedMonth
            }
        }

        val maxDate = GregorianCalendar(LocaleUtils.getCurrentLocale(itemView.context))

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
                    element.startDate = selectedMonth
                    element.endDate = selectedMonth
                }
                showSelectedMonth(element)
                onSelected(element)
                dismiss()
            }

            setOnDismissListener {
                showDateFilterBottomSheet()
            }

            dismissDateFilterBottomSheet()
            show(fm, element.label)
        }
    }

    private fun showDateFilterBottomSheet() {
        dateFilterBottomSheet?.show(fm, DateFilterBottomSheet.TAG)
    }

    private fun dismissDateFilterBottomSheet() {
        dateFilterBottomSheet?.dismiss()
    }

    private fun showSelectedMonth(element: DateFilterItem.MonthPickerItem) {
        itemView.edtStcPerMonth.label = itemView.context.getString(R.string.stc_month)
        val selectedMonthFmt = DateTimeUtil.format(element.startDate?.time ?: return, "MMMM yyyy")
        itemView.edtStcPerMonth.valueStr = selectedMonthFmt
    }
}