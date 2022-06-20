package com.tokopedia.shop.flashsale.presentation.creation.highlight.adapter

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.seller_shop_flash_sale.databinding.SsfsItemProductHighlightBinding
import com.tokopedia.shop.flashsale.common.extension.disable
import com.tokopedia.shop.flashsale.common.extension.enable
import com.tokopedia.shop.flashsale.domain.entity.HighlightableProduct

class HighlightedProductViewHolder(private val binding: SsfsItemProductHighlightBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val ALPHA_DISABLED = 0.5f
        private const val ALPHA_ENABLED = 1.0f
    }

    fun bind(
        position: Int,
        product: HighlightableProduct,
        onProductClicked: (HighlightableProduct, Int) -> Unit,
        onProductSelectionChange: (HighlightableProduct, Boolean) -> Unit,
        isLoading: Boolean
    ) {
        binding.imgProduct.loadImage(product.imageUrl)
        binding.tpgProductName.text = product.name
        binding.labelDiscountPercentage.text = product.discountPercentage
        binding.tpgDiscountedPrice.text = product.discountedPrice
        binding.root.setOnClickListener { onProductClicked(product, position) }
        binding.loader.isVisible = isLoading
        binding.tpgPrice.text = product.originalPrice
        handleCheckboxAppearance(product, onProductSelectionChange)
        handleCardSelectable(product.disableClick, product.disabled)
    }

    private fun handleCheckboxAppearance(
        product: HighlightableProduct,
        onProductSelectionChange: (HighlightableProduct, Boolean) -> Unit
    ) {
        binding.switchUnify.isChecked = product.isSelected
        binding.switchUnify.isClickable = !product.disableClick && !product.disabled
        binding.switchUnify.setOnCheckedChangeListener { _, isSelected ->
            onProductSelectionChange(
                product,
                isSelected
            )
        }
    }



    private fun handleCardSelectable(disableClick: Boolean, disabled: Boolean) {
        if (disableClick || disabled) {
            binding.imgProduct.alpha = ALPHA_DISABLED
            binding.tpgProductName.disable()
            binding.tpgPrice.disable()
            binding.switchUnify.disable()
        } else {
            binding.imgProduct.alpha = ALPHA_ENABLED
            binding.tpgProductName.enable()
            binding.tpgPrice.enable()
            binding.switchUnify.enable()
        }
    }

}