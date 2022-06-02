package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseTickerErrorShopLevelBinding
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel.TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel
import com.tokopedia.tokofood.feature.purchase.renderAlpha
import com.tokopedia.unifycomponents.ticker.TickerCallback

class TokoFoodPurchaseTickerErrorShopLevelViewHolder(private val viewBinding: ItemPurchaseTickerErrorShopLevelBinding,
                                                     private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_ticker_error_shop_level
    }

    override fun bind(element: TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel) {
        with(viewBinding) {
            tickerErrorShopLevel.setHtmlDescription(element.message.appendWithSeeAction())
            tickerErrorShopLevel.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    listener.onTextShowUnavailableItemClicked()
                }

                override fun onDismiss() {
                    // No-op
                }
            })

            tickerErrorShopLevel.renderAlpha(element)
        }
    }

    private fun String.appendWithSeeAction(): String {
        return itemView.context?.getString(R.string.text_purchase_ticker_unavailable_product, this).orEmpty()
    }

}