package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseFilterUiModel.FilterGroup
import kotlinx.android.synthetic.main.item_mvc_filter_group.view.*

/**
 * Created By @ilhamsuaib on 22/04/20
 */

class FilterGroupViewHolder(itemView: View?) : AbstractViewHolder<FilterGroup>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_filter_group
    }

    override fun bind(element: FilterGroup) {
        with(itemView) {
            tvMvcFilterGroupTitle.text = element.title
        }
    }
}