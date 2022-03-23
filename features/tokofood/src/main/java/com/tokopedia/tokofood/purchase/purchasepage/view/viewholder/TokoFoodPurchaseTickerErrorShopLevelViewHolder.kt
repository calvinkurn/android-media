package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseTickerErrorShopLevelBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseTickerErrorShopLevelUiModel
import com.tokopedia.tokofood.purchase.renderAlpha
import com.tokopedia.unifycomponents.ticker.TickerCallback

class TokoFoodPurchaseTickerErrorShopLevelViewHolder(private val viewBinding: ItemPurchaseTickerErrorShopLevelBinding,
                                                     private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseTickerErrorShopLevelUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_ticker_error_shop_level
    }

    override fun bind(element: TokoFoodPurchaseTickerErrorShopLevelUiModel) {
        with(viewBinding) {
            tickerErrorShopLevel.setHtmlDescription(element.message)
            tickerErrorShopLevel.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    listener.onTextShowUnavailableItemClicked()
                }

                override fun onDismiss() {
                    // No-op
                }
            })

            itemView.renderAlpha(element)
        }
    }

}