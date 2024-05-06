package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemProductBuyAgainBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartBuyAgainItemHolderData
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.unifycomponents.UnifyButton

class CartBuyAgainItemViewHolder(
    private val binding: ItemProductBuyAgainBinding,
    private val listener: ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_product_buy_again
    }

    fun bind(element: CartBuyAgainItemHolderData) {
        binding.productCardView.apply {
            setProductModel(
                element.recommendationItem.toProductCardModel(
                    true,
                    UnifyButton.Type.MAIN
                ).copy(
                    shopBadgeList = emptyList()
                )
            )
            setOnClickListener {
                listener?.onBuyAgainProductClicked(element)
            }
            setAddToCartOnClickListener {
                listener?.onBuyAgainButtonAddToCartClicked(element)
            }
        }
    }
}
