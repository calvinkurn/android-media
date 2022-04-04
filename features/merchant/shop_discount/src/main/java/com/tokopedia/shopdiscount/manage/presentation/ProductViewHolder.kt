package com.tokopedia.shopdiscount.manage.presentation

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shopdiscount.R
import com.tokopedia.shopdiscount.common.entity.ProductType
import com.tokopedia.shopdiscount.databinding.SdItemProductBinding
import com.tokopedia.shopdiscount.manage.domain.entity.Product
import com.tokopedia.shopdiscount.utils.extension.strikethrough

class ProductViewHolder(private val binding: SdItemProductBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product, onProductOptionClicked: (Product) -> Unit, isLoading: Boolean) {
        binding.imgProduct.loadImage(product.imageUrl)
        binding.tpgProductName.text = product.name
        binding.imgMore.setOnClickListener { onProductOptionClicked(product) }
        binding.btnUpdateDiscount.setOnClickListener { }
        binding.loader.isVisible = isLoading

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
                binding.tpgStockAndLocation.text = String.format(
                    binding.tpgInformation.context.getString(R.string.sd_total_stock),
                    product.totalStock
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
                binding.tpgStockAndLocation.text =
                    String.format(
                        binding.tpgInformation.context.getString(R.string.sd_total_stock_multiple_location),
                        product.totalStock,
                        product.variantCount
                    )
            }
            ProductType.VARIANT -> {
                displayDiscountPercentage(product)
                displayDiscountedPrice(product)
                displayOriginalPrice(product)
                binding.tpgInformation.text = "${product.variantCount} Varian Diskon"
                binding.tpgStockAndLocation.text = String.format(
                    binding.tpgInformation.context.getString(R.string.sd_total_stock),
                    product.totalStock
                )
            }
            ProductType.VARIANT_MULTI_LOCATION -> {
                displayDiscountPercentage(product)
                displayDiscountedPrice(product)
                displayOriginalPrice(product)
                binding.tpgInformation.text = "${product.variantCount} Varian Diskon"
                binding.tpgStockAndLocation.text =
                    String.format(
                        binding.tpgInformation.context.getString(R.string.sd_total_stock_various_multiple_location),
                        product.totalStock
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
        binding.tpgOriginalPrice.strikethrough()
    }

}
