package com.tokopedia.statistic.presentation.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.model.DateRangeItem
import com.tokopedia.statistic.presentation.view.bottomsheet.CalendarPicker
import kotlinx.android.synthetic.main.item_stc_date_range_custom.view.*

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

            edtStcStartFrom.setOnClickListener {
                CalendarPicker(context)
                        .init()
                        .showDatePicker(fm)
            }
            edtStcUntil.setOnClickListener {
                CalendarPicker(context)
                        .init()
                        .showDatePicker(fm)
            }
        }
    }

    private fun showCustomForm(isShown: Boolean) = with(itemView) {
        if (isShown) {
            tvStcLblStartFrom.visible()
            edtStcStartFrom.visible()
            tvStcLblUntil.visible()
            edtStcUntil.visible()
        } else {
            tvStcLblStartFrom.gone()
            edtStcStartFrom.gone()
            tvStcLblUntil.gone()
            edtStcUntil.gone()
        }
    }
}