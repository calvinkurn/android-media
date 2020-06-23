package com.tokopedia.statistic.presentation.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.statistic.R
import com.tokopedia.statistic.presentation.model.DateRangeItem
import kotlinx.android.synthetic.main.item_stc_date_range_apply_button.view.*

/**
 * Created By @ilhamsuaib on 23/06/20
 */

class DateRangeApplyViewHolder(
        itemView: View?,
        private val apply: () -> Unit
) : AbstractViewHolder<DateRangeItem.ApplyButton>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_apply_button
    }

    override fun bind(element: DateRangeItem.ApplyButton) {
        with(itemView) {
            btnStcApply.setOnClickListener {
                apply()
            }
        }
    }
}