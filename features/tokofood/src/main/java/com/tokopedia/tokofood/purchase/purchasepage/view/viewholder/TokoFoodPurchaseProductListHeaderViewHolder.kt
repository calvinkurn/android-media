package com.tokopedia.tokofood.purchase.purchasepage.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.databinding.ItemPurchaseProductListHeaderBinding
import com.tokopedia.tokofood.purchase.purchasepage.view.TokoFoodPurchaseActionListener
import com.tokopedia.tokofood.purchase.purchasepage.view.uimodel.TokoFoodPurchaseProductListHeaderUiModel
import com.tokopedia.tokofood.purchase.renderAlpha

class TokoFoodPurchaseProductListHeaderViewHolder(private val viewBinding: ItemPurchaseProductListHeaderBinding,
                                                  private val listener: TokoFoodPurchaseActionListener)
    : AbstractViewHolder<TokoFoodPurchaseProductListHeaderUiModel>(viewBinding.root) {

    companion object {
        val LAYOUT = R.layout.item_purchase_product_list_header
    }

    override fun bind(element: TokoFoodPurchaseProductListHeaderUiModel) {
        with(viewBinding) {
            textProductListHeader.text = element.title
            textProductListAction.text = element.action
            textProductListAction.setOnClickListener {
                if (!element.isUnavailableHeader) {
                    listener.onTextAddItemClicked()
                } else {
                    listener.onTextBulkDeleteUnavailableProductsClicked()
                }
            }

            itemView.renderAlpha(element)
        }
    }

}