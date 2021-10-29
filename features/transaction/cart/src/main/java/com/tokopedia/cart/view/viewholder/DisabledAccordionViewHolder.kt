package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartDisabledAccordionBundleBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData

class DisabledAccordionViewHolder(private val binding: ItemCartDisabledAccordionBundleBinding, val actionListener: ActionListener?) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_accordion_bundle
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