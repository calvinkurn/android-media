package com.tokopedia.oldminicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.databinding.ItemMiniCartLoadingBinding

class MiniCartLoadingViewHolder(private val viewBinding: ItemMiniCartLoadingBinding)
    : LoadingViewholder(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_loading
    }

}