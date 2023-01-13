package com.tokopedia.product.detail.view.util

import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute

/**
 * Created by Yehezkiel on 21/05/21
 */
object ProductDetailVariantLogic {

    fun determineVariant(mapOfSelectedOptionIds: Map<String, String>,
                         productVariant: ProductVariant?): VariantCategory? {
        val variantLevelOne = productVariant?.variants?.firstOrNull()
        val variantOptions = variantLevelOne?.options

        if (productVariant == null || variantOptions == null) return null

        val productId = productVariant.getChildByOptionId(mapOfSelectedOptionIds.values.toList())?.productId.orEmpty()
        val children = productVariant.children.firstOrNull { it.productId == productId }
        val listOfVariantLevelOne = mutableListOf<VariantOptionWithAttribute>()
        val getVariantOptionsLevelOneByProductId = children?.optionIds?.firstOrNull().orEmpty()
        val haveCustomImage = variantOptions.all {
            it.picture?.url100?.isNotEmpty() == true
        }
        var areAllVariantHaveSelectedChild = false

        for (i in variantOptions) {
            val isBuyable = productVariant.children.any {
                it.optionIds.firstOrNull() == i.id && it.isBuyable
            }

            val isSelected = getVariantOptionsLevelOneByProductId == i.id
            val currentState = if (isSelected && isBuyable) {
                areAllVariantHaveSelectedChild = true
                VariantConstant.STATE_SELECTED
            } else if (isSelected && !isBuyable) {
                areAllVariantHaveSelectedChild = true
                VariantConstant.STATE_SELECTED_EMPTY
            } else if (!isBuyable) {
                VariantConstant.STATE_EMPTY
            } else {
                VariantConstant.STATE_UNSELECTED
            }

            val isFlashSale = productVariant.isSelectedChildHasFlashSale(i.id ?: "")

            listOfVariantLevelOne.add(VariantOptionWithAttribute(
                    variantName = i.value.orEmpty(),
                    variantId = i.id.orEmpty(),
                    image100 = i.picture?.url100.orEmpty(),
                    imageOriginal = i.picture?.original.orEmpty(),
                    variantHex = i.hex.orEmpty(),
                    currentState = currentState,
                    hasCustomImages = haveCustomImage,
                    flashSale = isFlashSale,
                    variantCategoryKey = variantLevelOne.pv.orEmpty()
            ))
        }

        val selectedVariantName = children?.name?.split("-")?.last().orEmpty()
        val stringVariantIdentifier = productVariant.variants.mapNotNull { it.identifier }.joinToString()

        val variantTitle = if (areAllVariantHaveSelectedChild) selectedVariantName else stringVariantIdentifier //to determine pilih warna,ukuran or pilih hitam,xl
        return VariantCategory(
                name = variantTitle,
                identifier = variantLevelOne.name.orEmpty(),
                hasCustomImage = haveCustomImage,
                variantOptions = listOfVariantLevelOne
        )
    }
}
