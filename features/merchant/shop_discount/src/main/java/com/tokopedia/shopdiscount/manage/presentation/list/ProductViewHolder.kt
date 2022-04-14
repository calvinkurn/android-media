package com.tokopedia.shopdiscount.manage.presentation.list

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.common.entity.ProductType
import com.tokopedia.shopdiscount.databinding.SdItemProductBinding
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.utils.extension.strikethrough
import com.tokopedia.unifycomponents.Label.Companion.HIGHLIGHT_LIGHT_GREY
import com.tokopedia.unifycomponents.Label.Companion.HIGHLIGHT_LIGHT_RED

class ProductViewHolder(private val binding: SdItemProductBinding) :
    RecyclerView.ViewHolder(binding.root) {

    companion object {
        private const val ALPHA_DISABLED = 0.5f
        private const val ALPHA_ENABLED = 1.0f
    }

    fun bind(
        product: Product,
        onProductClicked: (Product) -> Unit,
        onUpdateDiscountButtonClicked: (Product) -> Unit,
        onOverflowMenuClicked: (Product) -> Unit,
        onVariantInfoClicked: (Product) -> Unit,
        onProductSelectionChange : (Product, Boolean) -> Unit,
        isLoading: Boolean
    ) {
        binding.imgProduct.loadImage(product.imageUrl)
        binding.tpgProductName.text = product.name
        binding.imgMore.setOnClickListener { onOverflowMenuClicked(product) }
        binding.btnUpdateDiscount.setOnClickListener { onUpdateDiscountButtonClicked(product) }
        binding.root.setOnClickListener { onProductClicked(product) }
        binding.loader.isVisible = isLoading
        handleProductType(product)
        binding.imgInfo.setOnClickListener { onVariantInfoClicked(product) }
        binding.tpgOriginalPrice.strikethrough()
        handleCheckboxAppearance(product, onProductSelectionChange)
        handleChangeDiscountButtonAppearance(product.shouldDisplayCheckbox)
        handleOverflowMenuAppearance(product.shouldDisplayCheckbox)
        handleCardSelectable(product.disableClick)
    }

    private fun handleCheckboxAppearance(product: Product, onProductSelectionChange: (Product, Boolean) -> Unit) {
        binding.checkBox.setOnCheckedChangeListener(null)
        binding.checkBox.isVisible = product.shouldDisplayCheckbox
        binding.checkBox.isChecked = product.isCheckboxTicked
        binding.checkBox.isClickable = !product.disableClick
        binding.checkBox.setOnCheckedChangeListener { _, isSelected -> onProductSelectionChange(product, isSelected) }
    }

    private fun handleOverflowMenuAppearance(shouldDisplayCheckbox: Boolean) {
        binding.imgMore.isVisible = !shouldDisplayCheckbox
    }

    private fun handleChangeDiscountButtonAppearance(shouldDisplayCheckbox: Boolean) {
        binding.btnUpdateDiscount.isVisible = !shouldDisplayCheckbox
    }

    private fun handleCardSelectable(disableClick : Boolean) {
        val labelType =  if (disableClick) HIGHLIGHT_LIGHT_GREY else HIGHLIGHT_LIGHT_RED
        binding.labelDiscount.setLabelType(labelType)

        val alpha = if (disableClick) ALPHA_DISABLED else ALPHA_ENABLED
        binding.card.alpha = alpha
    }

    private fun handleProductType(product: Product) {
        binding.imgInfo.isVisible = product.hasVariant

        when (product.productType) {
            ProductType.SINGLE -> {
                binding.labelDiscount.text = product.formattedDiscountMaxPercentage
                binding.tpgDiscountedPrice.text = product.formattedDiscountMaxPrice
                binding.tpgOriginalPrice.text = product.formattedOriginalMaxPrice
                binding.tpgInformation.text = String.format(
                    binding.tpgInformation.context.getString(R.string.sd_placeholder),
                    product.discountStartDate,
                    product.discountEndDate
                )
                binding.tpgStockAndLocation.text = MethodChecker.fromHtml(
                    String.format(
                        binding.tpgInformation.context.getString(R.string.sd_total_stock),
                        product.totalStock
                    )
                )
            }
            ProductType.SINGLE_MULTI_LOCATION -> {
                binding.labelDiscount.text = product.formattedDiscountMaxPercentage
                binding.tpgDiscountedPrice.text = product.formattedDiscountMaxPrice
                binding.tpgOriginalPrice.text = product.formattedOriginalMaxPrice
                binding.tpgInformation.text = String.format(
                    binding.tpgInformation.context.getString(R.string.sd_placeholder),
                    product.discountStartDate,
                    product.discountEndDate
                )
                binding.tpgStockAndLocation.text = MethodChecker.fromHtml(
                    String.format(
                        binding.tpgInformation.context.getString(R.string.sd_total_stock_multiple_location),
                        product.totalStock,
                        product.locationCount
                    )
                )

            }
            ProductType.VARIANT -> {
                displayDiscountPercentage(product)
                displayDiscountedPrice(product)
                displayOriginalPrice(product)
                binding.tpgInformation.text =
                    binding.tpgInformation.context.getString(R.string.sd_with_variant)
                binding.tpgStockAndLocation.text = MethodChecker.fromHtml(
                    String.format(
                        binding.tpgInformation.context.getString(R.string.sd_total_stock),
                        product.totalStock
                    )
                )
            }
            ProductType.VARIANT_MULTI_LOCATION -> {
                displayDiscountPercentage(product)
                displayDiscountedPrice(product)
                displayOriginalPrice(product)
                binding.tpgInformation.text =
                    binding.tpgInformation.context.getString(R.string.sd_with_variant)
                binding.tpgStockAndLocation.text = MethodChecker.fromHtml(
                    String.format(
                        binding.tpgInformation.context.getString(R.string.sd_total_stock_various_multiple_location),
                        product.totalStock
                    )
                )

            }
        }
    }

    private fun displayDiscountPercentage(product: Product) {
        binding.labelDiscount.text =
            if (product.hasSameDiscountPercentageAmount) {
                product.formattedDiscountMaxPercentage
            } else {
                String.format(
                    binding.labelDiscount.context.getString(R.string.sd_placeholder),
                    product.formattedDiscountMinPercentage,
                    product.formattedDiscountMaxPercentage
                )
            }
    }

    private fun displayDiscountedPrice(product: Product) {
        binding.tpgDiscountedPrice.text =
            if (product.hasSameDiscountedPriceAmount) {
                product.formattedDiscountMaxPrice
            } else {
                String.format(
                    binding.tpgDiscountedPrice.context.getString(R.string.sd_placeholder),
                    product.formattedDiscountMinPrice,
                    product.formattedDiscountMaxPrice
                )
            }
    }

    private fun displayOriginalPrice(product: Product) {
        binding.tpgOriginalPrice.text =
            if (product.hasSameOriginalPrice) {
                product.formattedOriginalMaxPrice
            } else {
                String.format(
                    binding.tpgDiscountedPrice.context.getString(R.string.sd_placeholder),
                    product.formattedOriginalMinPrice,
                    product.formattedOriginalMaxPrice
                )
            }
    }
}
