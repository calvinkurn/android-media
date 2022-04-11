package com.tokopedia.shopdiscount.select.presentation

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.common.entity.ProductType
import com.tokopedia.shopdiscount.databinding.SdItemSelectProductBinding
import com.tokopedia.shopdiscount.manage.domain.entity.Product

class SelectProductViewHolder(private val binding: SdItemSelectProductBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val ALPHA_DISABLED = 0.5f
        private const val ALPHA_ENABLED = 1.0f
    }

    fun bind(
        product: Product,
        onProductClicked: (Product) -> Unit,
        onProductSelectionChange : (Product, Boolean) -> Unit,
        isLoading: Boolean
    ) {
        binding.imgProduct.loadImage(product.imageUrl)
        binding.tpgProductName.text = product.name
        binding.root.setOnClickListener { onProductClicked(product) }
        binding.loader.isVisible = isLoading
        handleProductType(product)
        handleCheckboxAppearance(product, onProductSelectionChange)
        handleVariantAppearance(product)
        displayOriginalPrice(product)
        handleCardSelectable(product.disableClick)
    }

    private fun handleCheckboxAppearance(product: Product, onProductSelectionChange: (Product, Boolean) -> Unit) {
        binding.checkBox.setOnCheckedChangeListener(null)
        binding.checkBox.isVisible = true
        binding.checkBox.isChecked = product.isCheckboxTicked
        binding.checkBox.isClickable = !product.disableClick
        binding.checkBox.setOnCheckedChangeListener { _, isSelected -> onProductSelectionChange(product, isSelected) }
    }

    private fun handleProductType(product: Product) {
        when (product.productType) {
            ProductType.SINGLE -> {
                binding.tpgStockAndLocation.text = MethodChecker.fromHtml(
                    String.format(
                        binding.tpgStockAndLocation.context.getString(R.string.sd_total_stock),
                        product.totalStock
                    )
                )
            }
            ProductType.SINGLE_MULTI_LOCATION -> {
                binding.tpgStockAndLocation.text = MethodChecker.fromHtml(
                    String.format(
                        binding.tpgStockAndLocation.context.getString(R.string.sd_total_stock_multiple_location),
                        product.totalStock,
                        product.locationCount
                    )
                )

            }
            ProductType.VARIANT -> {
                binding.tpgStockAndLocation.text = MethodChecker.fromHtml(
                    String.format(
                        binding.tpgStockAndLocation.context.getString(R.string.sd_total_stock),
                        product.totalStock
                    )
                )
            }
            ProductType.VARIANT_MULTI_LOCATION -> {
                binding.tpgStockAndLocation.text = MethodChecker.fromHtml(
                    String.format(
                        binding.tpgStockAndLocation.context.getString(R.string.sd_total_stock_various_multiple_location),
                        product.totalStock
                    )
                )

            }
        }
    }

    private fun handleVariantAppearance(product: Product) {
        binding.labelVariantCount.isVisible = product.hasVariant
    }
    

    private fun displayOriginalPrice(product: Product) {
        binding.tpgPrice.text =
            if (product.hasSameOriginalPrice) {
                product.formattedOriginalMaxPrice
            } else {
                String.format(
                    binding.tpgPrice.context.getString(R.string.sd_placeholder),
                    product.formattedOriginalMinPrice,
                    product.formattedOriginalMaxPrice
                )
            }
    }

    private fun handleCardSelectable(disableClick : Boolean) {
        val alpha = if (disableClick) ALPHA_DISABLED else ALPHA_ENABLED
        binding.card.alpha = alpha
    }
}
