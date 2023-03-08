package com.tokopedia.product.detail.common.mapper.usecase

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.mapper.AtcVariantNewMapper

/**
 * Created by yovi.putra on 08/03/23"
 * Project name: android-tokopedia-core
 **/

object VariantOneLevelUseCase {

    fun process(
        variantData: ProductVariant,
        selectedVariant: Map<String, String>
    ): List<VariantCategory>? {
        val variant = variantData.variants.firstOrNull() ?: return null
        val variantSelected = selectedVariant.values.toList()
        val hasCustomImages = variant.options.all {
            it.picture?.url100?.isNotEmpty() == true
        }
        val variantGuideline = variantData.getVariantGuideline(
            sizeIdentifier = variant.isSizeIdentifier
        )
        // loop each variant options in the variant data for checking to their children
        val uiVariantOptions = variant.options.map { option ->
            // default state is stock empty
            var state = VariantConstant.STATE_EMPTY
            var isFlashSale = false
            var stock = 0

            // looping the children and find the child containing the option id
            // set state, flash sale and stock
            for (child in variantData.children) {
                // update state when variant option id equal by child option id
                if (option.id == child.optionIds.firstOrNull()) {
                    isFlashSale = child.isFlashSale
                    stock = child.stock?.stock.orZero()
                    state = if (child.optionIds == variantSelected) {
                        if (child.isBuyable) { // selected and can to buy
                            VariantConstant.STATE_SELECTED
                        } else { // selected and can not to buy
                            VariantConstant.STATE_SELECTED_EMPTY
                        }
                    } else if (child.isBuyable) { // un-selected and can to buy
                        VariantConstant.STATE_UNSELECTED
                    } else { // un-selected and can not to buy because stock is empty
                        VariantConstant.STATE_EMPTY
                    }
                    break
                }
            }

            VariantOptionWithAttribute.create(
                variantOption = option,
                variant = variant,
                state = state,
                stock = stock,
                hasCustomImages = hasCustomImages,
                isFlashSale = isFlashSale,
                level = AtcVariantNewMapper.VARIANT_LEVEL_ONE_SELECTED
            )
        }

        // create variant ui model
        val variantCategory = VariantCategory(
            name = variant.name.orEmpty(),
            identifier = variant.identifier.orEmpty(),
            variantGuideline = variantGuideline,
            isLeaf = true,
            hasCustomImage = hasCustomImages,
            variantOptions = uiVariantOptions
        )
        return listOf(variantCategory)
    }
}
