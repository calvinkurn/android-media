package com.tokopedia.addongifting.addonbottomsheet.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.addongifting.R
import com.tokopedia.addongifting.databinding.ItemAddOnLoadingBinding

class AddOnLoadingViewHolder(private val viewBinding: ItemAddOnLoadingBinding)
    : LoadingViewholder(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_add_on_loading
    }

}