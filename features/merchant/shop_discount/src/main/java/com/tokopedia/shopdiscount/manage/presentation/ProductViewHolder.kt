package com.tokopedia.shopdiscount.manage.presentation

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shopdiscount.common.entity.ProductType
import com.tokopedia.shopdiscount.databinding.SdItemProductBinding
import com.tokopedia.shopdiscount.manage.domain.entity.Product

class ProductViewHolder(private val binding: SdItemProductBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product, onProductOptionClicked: (Product) -> Unit, isLoading : Boolean) {
        binding.imgProduct.loadImage(product.imageUrl)
        binding.tpgProductName.text = product.name
        binding.imgMore.setOnClickListener { onProductOptionClicked(product) }
        binding.btnUpdateDiscount.setOnClickListener { }
        binding.loader.isVisible = isLoading

        when (product.productType) {
            ProductType.SINGLE -> {
                binding.labelDiscount.text = "${product.discountMaxPercentage}"
                binding.tpgDiscountedPrice.text = "${product.discountedMaxPrice}"
                binding.tpgOriginalPrice.text = "${product.originalMaxPrice}"
                binding.tpgDiscountPeriod.text = "25 Sept 2022 - 27 Sept 2022"
                binding.tpgStockAndLocation.text = "Total stok: ${product.totalStock}"

            }
            ProductType.SINGLE_MULTI_LOCATION -> {
                binding.labelDiscount.text = "${product.discountMaxPercentage}"
                binding.tpgDiscountedPrice.text = "${product.discountedMaxPrice}"
                binding.tpgOriginalPrice.text = "${product.originalMaxPrice}"
                binding.tpgDiscountPeriod.text = "25 Sept 2022 - 27 Sept 2022"
                binding.tpgStockAndLocation.text =
                    "Total stok: ${product.totalStock} di ${product.locationCount} lokasi"
            }
            ProductType.VARIANT -> {
                binding.labelDiscount.text =
                    "${product.discountMinPercentage} - ${product.discountMaxPercentage}"
                binding.tpgDiscountedPrice.text =
                    "${product.discountedMinPrice} - ${product.discountedMaxPrice}"
                binding.tpgOriginalPrice.text =
                    "${product.originalMinPrice} - ${product.originalMaxPrice}"
                binding.tpgDiscountPeriod.text = "${product.variantCount} Varian Diskon"
                binding.tpgStockAndLocation.text = "Total stok: ${product.totalStock}"
            }
            ProductType.VARIANT_MULTI_LOCATION -> {
                binding.labelDiscount.text =
                    "${product.discountMinPercentage} - ${product.discountMaxPercentage}"
                binding.tpgDiscountedPrice.text =
                    "${product.discountedMinPrice} - ${product.discountedMaxPrice}"
                binding.tpgOriginalPrice.text =
                    "${product.originalMinPrice} - ${product.originalMaxPrice}"
                binding.tpgDiscountPeriod.text = "${product.variantCount} Varian Diskon"
                binding.tpgStockAndLocation.text =
                    "Total stok: ${product.totalStock} di beberapa lokasi"
            }
        }
    }

}
