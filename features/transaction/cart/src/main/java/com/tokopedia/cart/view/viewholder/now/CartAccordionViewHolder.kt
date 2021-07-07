package com.tokopedia.cart.view.viewholder.now

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartAccordionBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.now.CartAccordionHolderData

class CartAccordionViewHolder(val viewBinding: ItemCartAccordionBinding, val actionListener: ActionListener) : RecyclerView.ViewHolder(viewBinding.root) {

    companion object {
        var LAYOUT = R.layout.item_cart_accordion
    }

    fun bind(cartAccordionHolderData: CartAccordionHolderData) {
        if (cartAccordionHolderData.isCollapsed) {
            viewBinding.imageChevron.rotation = 0f
            viewBinding.textAccordion.text = cartAccordionHolderData.showMoreWording
        } else {
            viewBinding.imageChevron.rotation = 180f
            viewBinding.textAccordion.text = cartAccordionHolderData.showLessWording
        }

        itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (cartAccordionHolderData.isCollapsed) {
                    actionListener.onExpandAvailableItem(position, cartAccordionHolderData.cartString)
                } else {
                    actionListener.onCollapseAvailableItem(position, cartAccordionHolderData.cartString)
                }
            }
        }
    }

}