package com.tokopedia.minicart.cartlist.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.minicart.R
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductBundleRecomShimmeringUiModel
import com.tokopedia.minicart.databinding.ItemMiniCartProductBundleRecomShimmeringBinding

class MiniCartProductBundleRecomShimmeringViewHolder(
    viewBinding: ItemMiniCartProductBundleRecomShimmeringBinding
) : AbstractViewHolder<MiniCartProductBundleRecomShimmeringUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_mini_cart_product_bundle_recom_shimmering
    }

    override fun bind(element: MiniCartProductBundleRecomShimmeringUiModel?) { /* nothing to do */ }
}
