package com.tokopedia.cartrevamp.view.viewholder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartShopBottomRevampBinding
import com.tokopedia.cartrevamp.view.ActionListener
import com.tokopedia.cartrevamp.view.uimodel.CartShopBottomHolderData
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

class CartShopBottomViewHolder(
    private val binding: ItemCartShopBottomRevampBinding,
    private val actionListener: ActionListener
) : RecyclerView.ViewHolder(binding.root) {

    // variable to hold identifier
    private var cartString: String = ""

    fun bindData(cartShopBottomHolderData: CartShopBottomHolderData) {
        renderAccordion(cartShopBottomHolderData)
        cartString = cartShopBottomHolderData.shopData.cartString
    }

    private fun renderAccordion(cartShopBottomHolderData: CartShopBottomHolderData) {
        if (!cartShopBottomHolderData.shopData.isError && cartShopBottomHolderData.shopData.isCollapsible) {
            val showMoreWording = itemView.context.getString(R.string.cart_new_default_wording_show_more)
            val showLessWording = itemView.context.getString(R.string.cart_new_default_wording_show_less)
            val separatorAccordionLayoutParams = binding.separatorAccordion.layoutParams as ViewGroup.MarginLayoutParams
            if (cartShopBottomHolderData.shopData.isCollapsed) {
                binding.imageChevron.rotation = CHEVRON_ROTATION_0
                binding.textAccordion.text = showMoreWording
                separatorAccordionLayoutParams.topMargin = SEPARATOR_MARGIN_TOP.dpToPx(itemView.resources.displayMetrics)
            } else {
                binding.imageChevron.rotation = CHEVRON_ROTATION_180
                binding.textAccordion.text = showLessWording
                separatorAccordionLayoutParams.topMargin = 0
            }

            binding.layoutAccordion.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (cartShopBottomHolderData.shopData.isCollapsed) {
                        actionListener.onExpandAvailableItem(position)
                    } else {
                        actionListener.onCollapseAvailableItem(position)
                    }
                }
            }

            binding.layoutAccordion.show()
            binding.separatorAccordion.show()
        } else {
            binding.layoutAccordion.gone()
            binding.separatorAccordion.gone()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_cart_shop_bottom_revamp

        private const val CHEVRON_ROTATION_0 = 0f
        private const val CHEVRON_ROTATION_180 = 180f

        private const val SEPARATOR_MARGIN_TOP = 16
    }
}
