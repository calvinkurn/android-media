package com.tokopedia.statistic.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.statistic.R
import com.tokopedia.statistic.view.model.DateFilterItem
import com.tokopedia.statistic.common.utils.DateFilterFormatUtil
import kotlinx.android.synthetic.main.item_stc_date_range_click.view.*

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateFilterClickViewHolder(
        itemView: View?,
        private val onClick: (DateFilterItem) -> Unit
) : AbstractViewHolder<DateFilterItem.Click>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_click
    }

    override fun bind(element: DateFilterItem.Click) {
        with(itemView) {
            setBackgroundColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Neutral_N0))

            tvStcDateRangeLabel.text = element.label
            tvStcDefaultDateRange.text = DateFilterFormatUtil.getDateRangeStr(element.startDate, element.endDate)
            radStcDefaultDateRange.isChecked = element.isSelected
            radStcDefaultDateRange.setOnClickListener {
                setOnSelected(element)
            }

            setOnClickListener {
                setOnSelected(element)
            }

            verLineStcDefault.visibility = if (element.showBottomBorder) View.VISIBLE else View.GONE
        }
    }

    private fun setOnSelected(element: DateFilterItem.Click) {
        element.isSelected = true
        onClick(element)
    }
}