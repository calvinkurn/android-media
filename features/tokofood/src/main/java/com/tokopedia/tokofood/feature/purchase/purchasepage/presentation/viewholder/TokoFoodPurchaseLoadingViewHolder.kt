package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseLoadingBinding

class TokoFoodPurchaseLoadingViewHolder(viewBinding: ItemPurchaseLoadingBinding)
    : LoadingViewholder(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_tokofood_promo_loading
    }

}
