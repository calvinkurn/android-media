package com.tokopedia.vouchercreation.shop.voucherlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.databinding.ItemMvcFilterGroupBinding
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseFilterUiModel.FilterGroup

/**
 * Created By @ilhamsuaib on 22/04/20
 */

class FilterGroupViewHolder(itemView: View?) : AbstractViewHolder<FilterGroup>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_filter_group
    }

    private var binding: ItemMvcFilterGroupBinding? by viewBinding()

    override fun bind(element: FilterGroup) {
        binding?.tvMvcFilterGroupTitle?.text = element.title
    }
}