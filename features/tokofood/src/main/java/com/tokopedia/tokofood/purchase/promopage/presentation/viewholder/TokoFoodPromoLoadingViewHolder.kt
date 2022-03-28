package com.tokopedia.tokofood.purchase.promopage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingViewholder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseLoadingBinding

class TokoFoodPromoLoadingViewHolder(private val viewBinding: ItemPurchaseLoadingBinding)
    : LoadingViewholder(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_loading
    }

}