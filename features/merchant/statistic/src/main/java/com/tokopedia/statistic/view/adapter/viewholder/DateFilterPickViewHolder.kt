package com.tokopedia.statistic.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.common.utils.DateFilterFormatUtil
import com.tokopedia.statistic.view.bottomsheet.CalendarPicker
import com.tokopedia.statistic.view.model.DateFilterItem
import kotlinx.android.synthetic.main.item_stc_date_range_pick.view.*
import java.util.*

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateFilterPickViewHolder(
        itemView: View?,
        private val fm: FragmentManager,
        private val onClick: (DateFilterItem) -> Unit
) : AbstractViewHolder<DateFilterItem.Pick>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_pick
    }

    private var element: DateFilterItem.Pick? = null
    private var datePicker: CalendarPicker? = null

    override fun bind(element: DateFilterItem.Pick) {
        this.element = element
        initCalendarPicker(element)
        with(itemView) {
            tvStcSingleLabel.text = element.label
            radStcSingleDateRange.isChecked = element.isSelected

            showCustomForm(element.isSelected)

            setOnClickListener {
                onItemClickListener(element)
            }

            radStcSingleDateRange.setOnClickListener {
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
            if (element.type == DateFilterItem.TYPE_PER_WEEK) {
                val title = itemView.context?.getString(R.string.stc_per_week).orEmpty()
                setMode(CalendarPickerView.SelectionMode.RANGE)
                setTitle(title)
            } else if (element.type == DateFilterItem.TYPE_PER_DAY) {
                val title = itemView.context?.getString(R.string.stc_per_day).orEmpty()
                setMode(CalendarPickerView.SelectionMode.SINGLE)
                setTitle(title)
            }
        }
    }

    private fun onItemClickListener(element: DateFilterItem.Pick) {
        element.isSelected = true
        showCustomForm(true)
        onClick(element)
    }

    private fun setupDatePicker() = with(itemView) {
        edtStcSingle.label = context.getString(R.string.stc_date)
        edtStcSingle.setOnClickListener {
            datePicker?.showDatePicker(fm)
        }

        datePicker?.setOnDismissListener {
            setSelectedDate(datePicker?.selectedDates?.firstOrNull(), datePicker?.selectedDates?.lastOrNull())
        }
    }

    private fun setSelectedDate(startDate: Date?, endDate: Date?) {
        if (startDate != null && endDate != null) {
            element?.startDate = startDate
            element?.endDate = endDate
            itemView.edtStcSingle.valueStr = if (element?.type == DateFilterItem.TYPE_PER_DAY) {
                DateTimeUtil.format(startDate.time, "dd MMM yyyy")
            } else {
                DateFilterFormatUtil.getDateRangeStr(startDate, endDate)
            }
        }
    }

    private fun showCustomForm(isShown: Boolean) = with(itemView) {
        if (isShown) {
            edtStcSingle.visible()
        } else {
            edtStcSingle.gone()
        }

        val lineMarginTop = if (isShown) {
            context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl3)
        } else {
            context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
        }
        verLineStcCustom.setMargin(0, lineMarginTop.toInt(), 0, 0)
    }
}