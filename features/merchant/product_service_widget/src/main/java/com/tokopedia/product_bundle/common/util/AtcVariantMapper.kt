package com.tokopedia.product_bundle.common.util

import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.product.detail.common.data.model.variant.*
import com.tokopedia.product_bundle.common.data.model.response.BundleItem
import com.tokopedia.product_bundle.common.data.model.response.Child
import com.tokopedia.product_bundle.common.data.model.response.VariantOption
import com.tokopedia.product.detail.common.data.model.variant.VariantOption as AtcVariantOption

object AtcVariantMapper {

    fun mapToProductVariant(bundleItem: BundleItem): ProductVariant = ProductVariant(
        parentId = bundleItem.productID.toString(),
        variants = mapToVariants(bundleItem),
        children = mapToVariantChildren(bundleItem)
    )

    private fun mapToVariants(bundleItem: BundleItem): List<Variant> = bundleItem.selections.map {
        Variant(
            pv = it.productVariantID.toString(),
            v = it.variantID.toString(),
            name = it.name,
            identifier = it.identifier,
            options = mapToVariantOptions(it.options)
        )
    }

    private fun mapToVariantOptions(options: List<VariantOption>) = options.map {
            AtcVariantOption(
                it.productVariantOptionID.toString(),
                it.unitValueID.toString(),
                it.value,
                it.hex
            )
        }

    private fun mapToVariantChildren(bundleItem: BundleItem) = bundleItem.children.map {
        try {
            val originalPrice = it.originalPrice
            val discountedPrice = it.bundlePrice
            val discountedPercentage = DiscountUtil.getDiscountPercentage(originalPrice, discountedPrice)

            VariantChild(
                productId = it.productID.toString(),
                price = it.bundlePrice,
                stock =  VariantStock(
                    stock = it.stock,
                    isBuyable = it.isBuyable && it.stock.isMoreThanZero(),
                    minimumOrder = it.minOrder.toString()
                ),
                optionIds = it.optionIds.map { optionId -> optionId.toString() },
                name =  it.name,
                picture = Picture(
                    original = it.picURL,
                    thumbnail = it.picURL,
                    url100 = it.picURL
                ),
                campaign = VariantCampaign(
                    isActive = originalPrice != discountedPrice,
                    originalPrice = originalPrice,
                    discountedPrice = discountedPrice,
                    discountedPercentage = discountedPercentage.toFloat(),
                    stock = it.stock
                ),
                optionName = mapOptionName(it, bundleItem)
            )
        } catch (e: Exception) {
            VariantChild()
        }
    }

    private fun mapOptionName(it: Child, bundleItem: BundleItem) =
        it.optionIds.mapIndexed { index, optionId ->
            bundleItem.selections.getOrNull(index)?.options?.find {
                it.productVariantOptionID == optionId
            }?.value.orEmpty()
        }
}