package com.tokopedia.statistic.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.statistic.R
import com.tokopedia.statistic.view.model.DateFilterItem
import kotlinx.android.synthetic.main.item_stc_date_range_apply_button.view.*

/**
 * Created By @ilhamsuaib on 23/06/20
 */

class DateFilterApplyViewHolder(
        itemView: View?,
        private val apply: () -> Unit
) : AbstractViewHolder<DateFilterItem.ApplyButton>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_apply_button
    }

    override fun bind(element: DateFilterItem.ApplyButton) {
        with(itemView) {
            btnStcApply.setOnClickListener {
                apply()
            }
        }
    }
}