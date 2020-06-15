package com.tokopedia.statistic.presentation.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.model.DateRangeItem
import kotlinx.android.synthetic.main.item_stc_date_range_custom.view.*

/**
 * Created By @ilhamsuaib on 15/06/20
 */

class DateRangeCustomViewHolder(
        itemView: View?,
        private val onClick: (DateRangeItem) -> Unit
) : AbstractViewHolder<DateRangeItem.Default>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_custom
    }

    override fun bind(element: DateRangeItem.Default) {
        with(itemView) {
            tvStcCustomLabel.text = element.label
            setOnClickListener {
                onClick(element)
            }
        }
    }
}