package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.DateFilterUtil
import com.tokopedia.sellerhomecommon.common.const.ShcConst
import com.tokopedia.sellerhomecommon.databinding.ItemShcDateRangePickBinding
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem
import com.tokopedia.sellerhomecommon.presentation.view.bottomsheet.CalendarPicker
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import java.util.*

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

class DateFilterPickViewHolder(
    itemView: View,
    private val fm: FragmentManager,
    private val onClick: (DateFilterItem) -> Unit
) : AbstractViewHolder<DateFilterItem.Pick>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_shc_date_range_pick
    }

    private val binding by lazy {
        ItemShcDateRangePickBinding.bind(itemView)
    }

    private var element: DateFilterItem.Pick? = null
    private var datePicker: CalendarPicker? = null

    override fun bind(element: DateFilterItem.Pick) {
        this.element = element
        initCalendarPicker(element)
        with(binding) {
            tvShcSingleLabel.text = element.label
            radShcSingleDateRange.isChecked = element.isSelected

            showCustomForm(element.isSelected)

            root.setOnClickListener {
                onItemClickListener(element)
            }

            radShcSingleDateRange.setOnClickListener {
                onItemClickListener(element)
            }

            setupDatePicker()

            if (element.startDate != null && element.endDate != null) {
                datePicker?.selectedDates = listOf(element.startDate!!, element.endDate!!)
                setSelectedDate(element.startDate, element.endDate)
            }
        }
    }

    private fun initCalendarPicker(element: DateFilterItem.Pick) {
        if (datePicker != null) return

        datePicker = CalendarPicker.newInstance(element).apply {
            when (element.type) {
                DateFilterItem.TYPE_PER_WEEK -> {
                    val title = itemView.context?.getString(R.string.shc_per_week).orEmpty()
                    setMode(CalendarPickerView.SelectionMode.RANGE)
                    setTitle(title)
                }
                DateFilterItem.TYPE_PER_DAY -> {
                    val title = itemView.context?.getString(R.string.shc_per_day).orEmpty()
                    setMode(CalendarPickerView.SelectionMode.SINGLE)
                    setTitle(title)
                }
                DateFilterItem.TYPE_CUSTOM, DateFilterItem.TYPE_CUSTOM_SAME_MONTH -> {
                    val title = itemView.context?.getString(R.string.shc_custom_lbl).orEmpty()
                    setMode(CalendarPickerView.SelectionMode.RANGE)
                    setTitle(title)
                }
            }
        }
    }

    private fun onItemClickListener(element: DateFilterItem.Pick) {
        element.isSelected = true
        showCustomForm(true)
        onClick(element)
    }

    private fun setupDatePicker() = with(binding) {
        edtShcSingle.label = root.context.getString(R.string.shc_date)
        edtShcSingle.setOnClickListener {
            datePicker?.showDatePicker(fm)
        }

        datePicker?.setOnDismissListener {
            setSelectedDate(
                datePicker?.selectedDates?.firstOrNull(),
                datePicker?.selectedDates?.lastOrNull()
            )
        }
    }

    private fun setSelectedDate(startDate: Date?, endDate: Date?) {
        if (startDate != null && endDate != null) {
            element?.startDate = startDate
            element?.endDate = endDate
            binding.edtShcSingle.valueStr = if (element?.type == DateFilterItem.TYPE_PER_DAY) {
                DateTimeUtil.format(startDate.time, ShcConst.FORMAT_DD_MM_YYYY)
            } else {
                DateFilterUtil.getDateRangeStr(startDate, endDate)
            }
        }
    }

    private fun showCustomForm(isShown: Boolean) = with(binding) {
        edtShcSingle.isVisible = isShown

        val lineMarginTop = if (isShown) {
            root.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
        } else {
            root.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
        }
        verLineShcCustom.setMargin(
            ShcConst.INT_0, lineMarginTop.toInt(), ShcConst.INT_0, ShcConst.INT_0
        )
    }
}