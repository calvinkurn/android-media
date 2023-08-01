package com.tokopedia.cartrevamp.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartDisabledAccordionRevampBinding
import com.tokopedia.cartrevamp.view.ActionListener
import com.tokopedia.cartrevamp.view.uimodel.DisabledAccordionHolderData

class DisabledAccordionViewHolder(private val binding: ItemCartDisabledAccordionRevampBinding, val actionListener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_accordion_revamp
    }

    fun bind(data: DisabledAccordionHolderData) {
        if (data.isCollapsed) {
            binding.imgChevron.rotation = 0f
            binding.textAccordion.text = data.showMoreWording
        } else {
            binding.imgChevron.rotation = 180f
            binding.textAccordion.text = data.showLessWording
        }

        itemView.setOnClickListener {
            actionListener?.onToggleUnavailableItemAccordion(data, binding.textAccordion.text.toString())
        }
    }
}
