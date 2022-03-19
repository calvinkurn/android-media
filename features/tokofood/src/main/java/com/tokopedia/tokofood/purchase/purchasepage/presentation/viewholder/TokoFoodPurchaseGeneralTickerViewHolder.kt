package com.tokopedia.tokofood.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseGeneralTickerBinding
import com.tokopedia.tokofood.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseGeneralTickerUiModel

class TokoFoodPurchaseGeneralTickerViewHolder(private val viewBinding: ItemPurchaseGeneralTickerBinding,
                                              private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseGeneralTickerUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_general_ticker
    }

    override fun bind(element: TokoFoodPurchaseGeneralTickerUiModel) {
        with(viewBinding) {

        }
    }

}