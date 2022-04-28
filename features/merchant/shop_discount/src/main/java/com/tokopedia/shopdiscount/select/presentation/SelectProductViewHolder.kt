package com.tokopedia.shopdiscount.select.presentation

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.databinding.SdItemSelectProductBinding
import com.tokopedia.shopdiscount.select.domain.entity.ReservableProduct
import com.tokopedia.shopdiscount.utils.constant.ZERO
import com.tokopedia.shopdiscount.utils.extension.disable
import com.tokopedia.shopdiscount.utils.extension.enable

class SelectProductViewHolder(private val binding: SdItemSelectProductBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val ALPHA_CHIP_DISABLED = 0.3f
        private const val ALPHA_DISABLED = 0.5f
        private const val ALPHA_ENABLED = 1.0f
    }

    fun bind(
        position: Int,
        product: ReservableProduct,
        onProductClicked: (ReservableProduct, Int) -> Unit,
        onProductSelectionChange: (ReservableProduct, Boolean) -> Unit,
        isLoading: Boolean
    ) {
        binding.imgProduct.loadImage(product.picture)
        binding.tpgProductName.text = product.name
        binding.root.setOnClickListener { onProductClicked(product, position) }
        binding.loader.isVisible = isLoading

        displayProductSku(product.sku)
        handleCheckboxAppearance(product, onProductSelectionChange)
        handleVariantAppearance(product)
        displayOriginalPrice(product)
        handleCardSelectable(product.disableClick, product.disabled)
        binding.tpgStockAndLocation.text = MethodChecker.fromHtml(
            String.format(
                binding.tpgStockAndLocation.context.getString(R.string.sd_total_stock_plain),
                product.stock,
            )
        )
    }

    private fun displayProductSku(sku: String) {
        binding.tpgSkuNumber.isVisible = sku.isNotEmpty()
        if (sku.isNotEmpty()) {
            val template = binding.tpgSkuNumber.context.getString(R.string.sd_sku)
            binding.tpgSkuNumber.text = String.format(template, sku)
        }
    }

    private fun handleCheckboxAppearance(
        product: ReservableProduct,
        onProductSelectionChange: (ReservableProduct, Boolean) -> Unit
    ) {
        binding.checkBox.setOnCheckedChangeListener(null)
        binding.checkBox.isVisible = true
        binding.checkBox.isChecked = product.isCheckboxTicked
        binding.checkBox.isClickable = !product.disableClick && !product.disabled
        binding.checkBox.setOnCheckedChangeListener { _, isSelected ->
            onProductSelectionChange(
                product,
                isSelected
            )
        }
    }

    private fun handleVariantAppearance(product: ReservableProduct) {
        if (product.countVariant == ZERO) {
            binding.labelVariantCount.invisible()
        } else {
            binding.labelVariantCount.visible()
        }
        val template = binding.tpgSkuNumber.context.getString(R.string.sd_has_variant)
        binding.labelVariantCount.text = String.format(template, product.countVariant)
    }

    private fun displayOriginalPrice(product: ReservableProduct) {
        binding.tpgPrice.text =
            if (product.hasSameOriginalPrice) {
                product.minPriceFormatted
            } else {
                String.format(
                    binding.tpgPrice.context.getString(R.string.sd_placeholder),
                    product.minPriceFormatted,
                    product.maxPriceFormatted
                )
            }
    }

    private fun handleCardSelectable(disableClick: Boolean, disabled: Boolean) {
        if (disableClick || disabled) {
            binding.imgProduct.alpha = ALPHA_DISABLED
            binding.tpgProductName.disable()
            binding.tpgStockAndLocation.disable()
            binding.tpgPrice.disable()
            binding.labelVariantCount.opacityLevel = ALPHA_CHIP_DISABLED
            binding.tpgSkuNumber.disable()
            binding.checkBox.disable()
        } else {
            binding.imgProduct.alpha = ALPHA_ENABLED
            binding.tpgProductName.enable()
            binding.tpgStockAndLocation.enable()
            binding.labelVariantCount.opacityLevel = ALPHA_ENABLED
            binding.tpgPrice.enable()
            binding.tpgSkuNumber.enable()
            binding.checkBox.enable()
        }
    }
}
