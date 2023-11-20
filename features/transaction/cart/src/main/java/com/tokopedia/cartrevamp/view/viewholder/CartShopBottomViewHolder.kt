package com.tokopedia.cartrevamp.view.viewholder

import android.view.ViewGroup.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartShopBottomRevampBinding
import com.tokopedia.cartrevamp.view.ActionListener
import com.tokopedia.cartrevamp.view.uimodel.CartShopBottomHolderData
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

class CartShopBottomViewHolder(
    private val binding: ItemCartShopBottomRevampBinding,
    private val actionListener: ActionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(cartShopBottomHolderData: CartShopBottomHolderData) {
        renderAccordion(cartShopBottomHolderData)
    }

    private fun renderAccordion(cartShopBottomHolderData: CartShopBottomHolderData) {
        val layoutParams = binding.llShopContainer.layoutParams
        if (!cartShopBottomHolderData.shopData.isError && cartShopBottomHolderData.shopData.isCollapsible) {
            val showMoreWording = itemView.context.getString(R.string.cart_new_default_wording_show_more)
            val showLessWording = itemView.context.getString(R.string.cart_new_default_wording_show_less)
            if (cartShopBottomHolderData.shopData.isCollapsed) {
                binding.imageChevron.rotation = CHEVRON_ROTATION_0
                binding.textAccordion.text = showMoreWording
            } else {
                binding.imageChevron.rotation = CHEVRON_ROTATION_180
                binding.textAccordion.text = showLessWording
            }

            binding.llShopContainer.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (cartShopBottomHolderData.shopData.isCollapsed) {
                        actionListener.onExpandAvailableItem(position)
                    } else {
                        actionListener.onCollapseAvailableItem(position)
                    }
                }
            }

            layoutParams.height = LayoutParams.WRAP_CONTENT
            binding.llShopContainer.layoutParams = layoutParams
            binding.llShopContainer.show()
        } else {
            layoutParams.height = 0
            binding.llShopContainer.layoutParams = layoutParams
            binding.llShopContainer.gone()
        }
    }

    companion object {
        val LAYOUT = R.layout.item_cart_shop_bottom_revamp

        private const val CHEVRON_ROTATION_0 = 0f
        private const val CHEVRON_ROTATION_180 = 180f
    }
}
