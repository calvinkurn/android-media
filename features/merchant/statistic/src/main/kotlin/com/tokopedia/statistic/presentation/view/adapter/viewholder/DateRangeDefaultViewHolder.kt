package com.tokopedia.statistic.presentation.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.model.DateRangeItem
import com.tokopedia.statistic.utils.DateRangeFormatUtil
import kotlinx.android.synthetic.main.item_stc_date_range_default.view.*
import java.util.*

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateRangeDefaultViewHolder(
        itemView: View?,
        private val onClick: (DateRangeItem) -> Unit
) : AbstractViewHolder<DateRangeItem.Default>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_default
    }

    override fun bind(element: DateRangeItem.Default) {
        with(itemView) {
            setBackgroundColor(context.getResColor(R.color.Neutral_N0))

            tvStcDateRangeLabel.text = element.label
            tvStcDefaultDateRange.text = DateRangeFormatUtil.getDateRangeStr(element.startDate, element.endDate)
            radStcDefaultDateRange.isChecked = element.isSelected
            radStcDefaultDateRange.setOnClickListener {
                setOnSelected(element)
            }

            setOnClickListener {
                setOnSelected(element)
            }
        }
    }

    private fun setOnSelected(element: DateRangeItem.Default) {
        element.isSelected = true
        onClick(element)
    }
}