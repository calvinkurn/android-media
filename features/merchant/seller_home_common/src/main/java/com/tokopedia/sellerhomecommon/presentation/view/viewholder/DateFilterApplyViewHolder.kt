package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ItemShcDateRangeApplyButtonBinding
import com.tokopedia.sellerhomecommon.presentation.model.DateFilterItem

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

class DateFilterApplyViewHolder(
    itemView: View,
    private val apply: () -> Unit
) : AbstractViewHolder<DateFilterItem.ApplyButton>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_shc_date_range_apply_button
    }

    private val binding by lazy {
        ItemShcDateRangeApplyButtonBinding.bind(itemView)
    }

    override fun bind(element: DateFilterItem.ApplyButton) {
        with(binding) {
            btnStcApply.setOnClickListener {
                apply()
            }
        }
    }
}