package com.tokopedia.statistic.presentation.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.model.DateRangeItem
import com.tokopedia.statistic.presentation.view.bottomsheet.CalendarPicker
import com.tokopedia.statistic.presentation.view.customview.DateTextFieldView
import kotlinx.android.synthetic.main.item_stc_date_range_custom.view.*
import java.util.*

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateRangeCustomViewHolder(
        itemView: View?,
        private val fm: FragmentManager,
        private val onApply: (DateRangeItem) -> Unit,
        private val onClick: (DateRangeItem) -> Unit
) : AbstractViewHolder<DateRangeItem.Custom>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_custom
    }

    private val datePicker: CalendarPicker by lazy {
        CalendarPicker(this.itemView.context).init()
    }

    override fun bind(element: DateRangeItem.Custom) {
        with(itemView) {
            tvStcCustomLabel.text = element.label
            radStcCustomDateRange.isChecked = element.isSelected

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
        edtStcStart.label = context.getString(R.string.stc_start_from)
        edtStcStart.setOnClickListener {
            datePicker.showDatePicker(fm)
        }

        edtStcUntil.label = context.getString(R.string.stc_until)
        edtStcUntil.setOnClickListener {
            datePicker.showDatePicker(fm)
        }

        datePicker.setOnDismissListener {
            showSelectedDate(edtStcStart, datePicker.selectedDates.firstOrNull())
            if (datePicker.selectedDates.size > 1) {
                showSelectedDate(edtStcUntil, datePicker.selectedDates.lastOrNull())
            } else {
                showSelectedDate(edtStcUntil, null)
            }
        }
    }

    private fun showSelectedDate(edt: DateTextFieldView, date: Date?) {
        edt.run {
            if (null != date) {
                valueStr = DateTimeUtil.format(date.time, "dd MMM yyyy")
            } else {
                reset()
            }
        }
    }

    private fun showCustomForm(isShown: Boolean) = with(itemView) {
        if (isShown) {
            edtStcStart.visible()
            edtStcUntil.visible()
            verLineStcCustom.visible()
        } else {
            edtStcStart.gone()
            edtStcUntil.gone()
            verLineStcCustom.gone()
        }
    }
}