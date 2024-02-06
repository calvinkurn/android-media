package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartDisabledAccordionRevampBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.DisabledAccordionHolderData
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

class DisabledAccordionViewHolder(
    private val binding: ItemCartDisabledAccordionRevampBinding,
    val actionListener: ActionListener?
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        val LAYOUT = R.layout.item_cart_disabled_accordion_revamp

        const val DEFAULT_ITEM_PADDING = 16
    }

    fun bind(data: DisabledAccordionHolderData) {
        val defaultPadding = DEFAULT_ITEM_PADDING.dpToPx(itemView.resources.displayMetrics)
        if (data.isCollapsed) {
            binding.imgChevron.rotation = 0f
            binding.textAccordion.text = data.showMoreWording
            binding.dividerAccordion.gone()
            binding.cartDisabledAccordionContainer.setPadding(
                defaultPadding,
                0,
                defaultPadding,
                defaultPadding
            )
        } else {
            binding.imgChevron.rotation = 180f
            binding.textAccordion.text = data.showLessWording
            binding.dividerAccordion.show()
            binding.cartDisabledAccordionContainer.setPadding(
                defaultPadding,
                defaultPadding,
                defaultPadding,
                defaultPadding
            )
        }

        itemView.setOnClickListener {
            actionListener?.onToggleUnavailableItemAccordion(
                data,
                binding.textAccordion.text.toString()
            )
        }
    }
}
