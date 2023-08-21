package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.common.DateFilterUtil
import com.tokopedia.sellerhomecommon.databinding.ItemShcDateRangeClickBinding
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

class DateFilterClickViewHolder(
    itemView: View,
    private val onClick: (DateFilterItem) -> Unit
) : AbstractViewHolder<DateFilterItem.Click>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_shc_date_range_click
    }

    private val binding by lazy {
        ItemShcDateRangeClickBinding.bind(itemView)
    }

    override fun bind(element: DateFilterItem.Click) {
        with(binding) {
            root.setBackgroundColor(root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN0))

            tvStcDateRangeLabel.text = element.label
            tvStcDefaultDateRange.text =
                DateFilterUtil.getDateRangeStr(element.startDate, element.endDate)
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