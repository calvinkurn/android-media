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
        position: Int,
        product: Product,
        onProductImageClicked: (Product) -> Unit,
        onProductClicked: (Product, Int) -> Unit,
        onUpdateDiscountButtonClicked: (Product) -> Unit,
        onOverflowMenuClicked: (Product) -> Unit,
        onVariantInfoClicked: (Product, Int) -> Unit,
        onProductSelectionChange: (Product, Boolean) -> Unit,
        isLoading: Boolean
    ) {
        binding.imgProduct.loadImage(product.imageUrl)
        binding.imgProduct.setOnClickListener { onProductImageClicked(product) }
        binding.tpgProductName.text = product.name
        binding.imgMore.setOnClickListener { onOverflowMenuClicked(product) }
        binding.btnUpdateDiscount.setOnClickListener { onUpdateDiscountButtonClicked(product) }
        binding.root.setOnClickListener { onProductClicked(product, position) }
        binding.loader.isVisible = isLoading
        handleProductType(product)
        binding.imgInfo.setOnClickListener { onVariantInfoClicked(product, position) }
        binding.tpgOriginalPrice.strikethrough()
        handleCheckboxAppearance(product, onProductSelectionChange)
        handleChangeDiscountButtonAppearance(product.shouldDisplayCheckbox)
        handleOverflowMenuAppearance(product.shouldDisplayCheckbox)
        handleCardSelectable(product.disableClick)
    }

    private fun handleCheckboxAppearance(
        product: Product,
        onProductSelectionChange: (Product, Boolean) -> Unit
    ) {
        binding.checkBox.setOnCheckedChangeListener(null)
        binding.checkBox.isVisible = product.shouldDisplayCheckbox
        binding.checkBox.isChecked = product.isCheckboxTicked
        binding.checkBox.isClickable = !product.disableClick
        binding.checkBox.setOnCheckedChangeListener { _, isSelected ->
            onProductSelectionChange(
                product,
                isSelected
            )
        }
    }

    private fun handleOverflowMenuAppearance(shouldDisplayCheckbox: Boolean) {
        binding.imgMore.isVisible = !shouldDisplayCheckbox
    }

    private fun handleChangeDiscountButtonAppearance(shouldDisplayCheckbox: Boolean) {
        binding.btnUpdateDiscount.isVisible = !shouldDisplayCheckbox
    }

    private fun handleCardSelectable(disableClick: Boolean) {
        val labelType = if (disableClick) HIGHLIGHT_LIGHT_GREY else HIGHLIGHT_LIGHT_RED
        binding.labelDiscount.setLabelType(labelType)

        val alpha = if (disableClick) ALPHA_DISABLED else ALPHA_ENABLED
        binding.card.alpha = alpha
    }

    private fun handleProductType(product: Product) {
        binding.imgInfo.isVisible = product.hasVariant

        when (product.productType) {
            ProductType.SINGLE -> {
                binding.labelDiscount.text = product.formattedDiscountMaxPercentage
                binding.tpgDiscountedPrice.text = product.formattedDiscountMaxPrice.replace(" ","")
                binding.tpgOriginalPrice.text = product.formattedOriginalMaxPrice.replace(" ","")
                displayDiscountPeriodRange(product)
                binding.tpgStockAndLocation.text = MethodChecker.fromHtml(
                    String.format(
                        binding.tpgInformation.context.getString(R.string.sd_total_stock),
                        product.totalStock
                    )
                )
            }
            ProductType.SINGLE_MULTI_LOCATION -> {
                displayDiscountPercentageRange(product)
                displayDiscountedPriceRange(product)
                displayOriginalPriceRange(product)
                displayDiscountPeriodRange(product)
                binding.tpgStockAndLocation.text = MethodChecker.fromHtml(
                    String.format(
                        binding.tpgInformation.context.getString(R.string.sd_total_stock_multiple_location),
                        product.totalStock,
                        product.locationCount
                    )
                )
            }
            ProductType.VARIANT -> {
                displayDiscountPercentageRange(product)
                displayDiscountedPriceRange(product)
                displayOriginalPriceRange(product)
                displayVariantCount()
                binding.tpgStockAndLocation.text = MethodChecker.fromHtml(
                    String.format(
                        binding.tpgInformation.context.getString(R.string.sd_total_stock),
                        product.totalStock
                    )
                )
            }
        }
    }

    private fun displayDiscountPercentageRange(product: Product) {
        binding.labelDiscount.text = if (product.hasSameDiscountPercentageAmount) {
            product.formattedDiscountMaxPercentage
        } else {
            String.format(
                binding.labelDiscount.context.getString(R.string.sd_placeholder),
                product.formattedDiscountMinPercentage,
                product.formattedDiscountMaxPercentage
            )
        }
    }

    private fun displayDiscountedPriceRange(product: Product) {
        binding.tpgDiscountedPrice.text = if (product.hasSameDiscountedPriceAmount) {
            product.formattedDiscountMaxPrice.replace(" ","")
        } else {
            String.format(
                binding.tpgDiscountedPrice.context.getString(R.string.sd_placeholder),
                product.formattedDiscountMinPrice.replace(" ",""),
                product.formattedDiscountMaxPrice.replace(" ","")
            )
        }

    }

    private fun displayOriginalPriceRange(product: Product) {
        binding.tpgOriginalPrice.text = if (product.hasSameOriginalPrice) {
            product.formattedOriginalMaxPrice.replace(" ","")
        } else {
            String.format(
                binding.tpgOriginalPrice.context.getString(R.string.sd_placeholder),
                product.formattedOriginalMinPrice.replace(" ",""),
                product.formattedOriginalMaxPrice.replace(" ","")
            )
        }
    }

    private fun displayVariantCount() {
        binding.tpgInformation.text =
            binding.tpgInformation.context.getString(R.string.sd_with_variant)
    }

    private fun displayDiscountPeriodRange(product: Product) {
        binding.tpgInformation.text = String.format(
            binding.tpgInformation.context.getString(R.string.sd_placeholder),
            product.discountStartDate,
            product.discountEndDate
        )
    }
}
