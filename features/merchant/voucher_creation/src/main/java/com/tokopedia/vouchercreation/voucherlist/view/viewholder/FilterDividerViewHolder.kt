package com.tokopedia.vouchercreation.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.BaseFilterUiModel.FilterGroup

/**
 * Created By @ilhamsuaib on 22/04/20
 */

class FilterDividerViewHolder(itemView: View?) : AbstractViewHolder<FilterGroup>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_filter_divider
    }

    override fun bind(element: FilterGroup) {}
}