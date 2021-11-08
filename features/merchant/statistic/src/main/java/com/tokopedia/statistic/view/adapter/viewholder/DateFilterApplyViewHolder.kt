package com.tokopedia.statistic.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.statistic.R
import com.tokopedia.statistic.databinding.ItemStcDateRangeApplyButtonBinding
import com.tokopedia.statistic.view.model.DateFilterItem

/**
 * Created By @ilhamsuaib on 23/06/20
 */

class DateFilterApplyViewHolder(
        itemView: View,
        private val apply: () -> Unit
) : AbstractViewHolder<DateFilterItem.ApplyButton>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_stc_date_range_apply_button
    }

    private val binding by lazy {
        ItemStcDateRangeApplyButtonBinding.bind(itemView)
    }

    override fun bind(element: DateFilterItem.ApplyButton) {
        with(binding) {
            btnStcApply.setOnClickListener {
                apply()
            }
        }
    }
}