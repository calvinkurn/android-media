package com.tokopedia.statistic.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.statistic.R
import com.tokopedia.statistic.common.utils.DateFilterFormatUtil
import com.tokopedia.statistic.databinding.ItemStcDateRangeClickBinding
import com.tokopedia.statistic.view.model.DateFilterItem

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateFilterClickViewHolder(
    itemView: View,
    private val onClick: (DateFilterItem) -> Unit
) : AbstractViewHolder<DateFilterItem.Click>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_click
    }

    private val binding by lazy {
        ItemStcDateRangeClickBinding.bind(itemView)
    }

    override fun bind(element: DateFilterItem.Click) {
        with(binding) {
            root.setBackgroundColor(root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N0))

            tvStcDateRangeLabel.text = element.label
            tvStcDefaultDateRange.text =
                DateFilterFormatUtil.getDateRangeStr(element.startDate, element.endDate)
            radStcDefaultDateRange.isChecked = element.isSelected
            radStcDefaultDateRange.setOnClickListener {
                setOnSelected(element)
            }

            root.setOnClickListener {
                setOnSelected(element)
            }

            verLineStcDefault.isVisible = element.showBottomBorder
        }
    }

    private fun setOnSelected(element: DateFilterItem.Click) {
        element.isSelected = true
        onClick(element)
    }
}