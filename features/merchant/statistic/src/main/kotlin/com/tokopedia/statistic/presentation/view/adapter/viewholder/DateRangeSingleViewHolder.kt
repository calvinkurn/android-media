package com.tokopedia.statistic.presentation.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.calendar.CalendarPickerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.model.DateRangeItem
import com.tokopedia.statistic.presentation.view.bottomsheet.CalendarPicker
import kotlinx.android.synthetic.main.item_stc_date_range_single.view.*
import java.util.*

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateRangeSingleViewHolder(
        itemView: View?,
        private val fm: FragmentManager,
        private val onApply: (DateRangeItem) -> Unit,
        private val onClick: (DateRangeItem) -> Unit
) : AbstractViewHolder<DateRangeItem.Single>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_single
    }

    private var element: DateRangeItem.Single? = null
    private val datePicker: CalendarPicker by lazy {
        val picker = CalendarPicker(this.itemView.context)
        if (element?.type == DateRangeItem.Single.TYPE_PER_WEEK) {
            val title = itemView?.context?.getString(R.string.stc_per_week).orEmpty()
            picker.init(CalendarPickerView.SelectionMode.RANGE)
            picker.setTitle(title)
        } else if (element?.type == DateRangeItem.Single.TYPE_PER_DAY) {
            val title = itemView?.context?.getString(R.string.stc_per_day).orEmpty()
            picker.init(CalendarPickerView.SelectionMode.SINGLE)
            picker.setTitle(title)
        }
        return@lazy picker
    }

    override fun bind(element: DateRangeItem.Single) {
        this.element = element
        with(itemView) {
            tvStcSingleLabel.text = element.label
            radStcSingleDateRange.isChecked = element.isSelected

            showCustomForm(element.isSelected)

            setOnClickListener {
                element.isSelected = true
                showCustomForm(true)
                onClick(element)
            }


            setupDatePicker()
        }
    }

    private fun setupDatePicker() = with(itemView) {
        edtStcSingle.label = context.getString(R.string.stc_start_from)
        edtStcSingle.setOnClickListener {
            showDatePicker()
        }

        datePicker.setOnDismissListener {
            showSelectedDate(datePicker.selectedDates.firstOrNull(), datePicker.selectedDates.lastOrNull())
        }
    }

    private fun showDatePicker() {
        element?.let {
            when (it.type) {
                DateRangeItem.Single.TYPE_PER_DAY, DateRangeItem.Single.TYPE_PER_WEEK -> {
                    datePicker.showDatePicker(fm)
                }
                DateRangeItem.Single.TYPE_PER_MONTH -> {
                    //show month picker
                }
            }
        }
    }

    private fun showSelectedDate(start: Date?, end: Date?) {
        itemView.edtStcSingle.run {
            if (null != start) {
                //valueStr = DateTimeUtil.format(date.time, "dd MMM yyyy")
            } else {
                reset()
            }
        }
    }

    private fun showCustomForm(isShown: Boolean) = with(itemView) {
        if (isShown) {
            edtStcSingle.visible()
        } else {
            edtStcSingle.gone()
        }
    }
}