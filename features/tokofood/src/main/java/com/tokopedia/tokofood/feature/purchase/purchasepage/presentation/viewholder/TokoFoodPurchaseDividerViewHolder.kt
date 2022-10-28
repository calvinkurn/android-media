package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseDividerBinding
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseDividerTokoFoodPurchaseUiModel

class TokoFoodPurchaseDividerViewHolder(viewBinding: ItemPurchaseDividerBinding)
    : AbstractViewHolder<TokoFoodPurchaseDividerTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_divider
    }

    override fun bind(element: TokoFoodPurchaseDividerTokoFoodPurchaseUiModel) {}

}