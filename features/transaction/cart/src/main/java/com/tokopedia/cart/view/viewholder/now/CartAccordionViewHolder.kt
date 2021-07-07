package com.tokopedia.cart.view.viewholder.now

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartAccordionBinding
import com.tokopedia.cart.view.uimodel.now.CartAccordionHolderData

class CartAccordionViewHolder(val viewBinding: ItemCartAccordionBinding) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        var LAYOUT = R.layout.item_cart_accordion
    }

    fun bind(cartAccordionHolderData: CartAccordionHolderData) {
        if (cartAccordionHolderData.isCollapsed) {
            viewBinding.textAccordion.text = cartAccordionHolderData.showMoreWording
        } else {
            viewBinding.textAccordion.text = cartAccordionHolderData.showLessWording
        }
    }

}