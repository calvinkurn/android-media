package com.tokopedia.shopdiscount.select.presentation

import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.gone
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
        private const val MARGIN_TOP = 4
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
        handleConstraint(product)
    }

    private fun displayProductSku(sku: String) {
        if (sku.isNotEmpty()) {
            binding.tpgSkuNumber.visible()
            val template = binding.tpgSkuNumber.context.getString(R.string.sd_sku)
            binding.tpgSkuNumber.text = String.format(template, sku)
        } else {
            binding.tpgSkuNumber.gone()
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
            binding.labelVariantCount.gone()
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

    private fun handleConstraint(product: ReservableProduct) {
        if (product.countVariant == ZERO) {
            constraintPriceTypographyToSkuNumberBottom()
        } else {
            revertToOriginalConstraint()
        }
    }

    private fun constraintPriceTypographyToSkuNumberBottom() {
        val set = ConstraintSet()
        set.clone(binding.card)
        set.connect(
            binding.tpgPrice.id,
            ConstraintSet.TOP,
            binding.tpgSkuNumber.id,
            ConstraintSet.BOTTOM
        )
        set.applyTo(binding.card)
    }

    private fun revertToOriginalConstraint() {
        val set = ConstraintSet()
        set.clone(binding.card)
        set.connect(
            binding.tpgPrice.id,
            ConstraintSet.TOP,
            binding.labelVariantCount.id,
            ConstraintSet.BOTTOM,
            MARGIN_TOP
        )
        set.applyTo(binding.card)
    }
}
