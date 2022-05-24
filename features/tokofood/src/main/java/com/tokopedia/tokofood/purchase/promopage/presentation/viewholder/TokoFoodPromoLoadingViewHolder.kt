package com.tokopedia.tokofood.purchase.promopage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemTokofoodPromoLoadingBinding

class TokoFoodPromoLoadingViewHolder(private val viewBinding: ItemTokofoodPromoLoadingBinding)
    : LoadingViewholder(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_tokofood_promo_loading
    }

}