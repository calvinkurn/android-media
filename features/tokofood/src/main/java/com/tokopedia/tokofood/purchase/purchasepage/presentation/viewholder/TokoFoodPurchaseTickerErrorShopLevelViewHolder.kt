package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseTickerErrorShopLevelBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTickerErrorShopLevelUiModel

class TokoFoodPurchaseTickerErrorShopLevelViewHolder(private val viewBinding: ItemPurchaseTickerErrorShopLevelBinding,
                                                     private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseTickerErrorShopLevelUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_ticker_error_shop_level
    }

    override fun bind(element: TokoFoodPurchaseTickerErrorShopLevelUiModel) {
        with(viewBinding) {

        }
    }

}