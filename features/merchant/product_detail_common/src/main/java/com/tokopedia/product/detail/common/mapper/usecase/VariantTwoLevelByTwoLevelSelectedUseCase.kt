package com.tokopedia.product.detail.common.mapper.usecase

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.mapper.AtcVariantNewMapper

/**
 * Created by yovi.putra on 08/03/23"
 * Project name: android-tokopedia-core
 **/

object VariantTwoLevelByTwoLevelSelectedUseCase {

    fun process(
        variantData: ProductVariant,
        mapOfSelectedVariant: Map<String, String>
    ): List<VariantCategory>? {
        val selectedVariant = mapOfSelectedVariant.values.toList()
        // update variant state on one level
        val variantTwoLevel = variantData.variants.getOrNull(
            AtcVariantNewMapper.VARIANT_LEVEL_TWO_SELECTED
        ) ?: return null
        val uiVariantTwoLevel = processVariantTwoLevel(
            variantTwoLevel = variantTwoLevel,
            variantData = variantData,
            selectedVariant = selectedVariant
        )
        // update variant state on two level
        val variantOneLevel = variantData.variants.getOrNull(
            AtcVariantNewMapper.VARIANT_LEVEL_ONE_SELECTED
        ) ?: return null
        val uiVariantOneLevel = processVariantOneLevel(
            variantOneLevel = variantOneLevel,
            selectedVariant = selectedVariant,
            variantData = variantData
        )

        return listOf(uiVariantOneLevel, uiVariantTwoLevel)
    }

    /***
     * Process variant two level after process variant one level
     * @param variantTwoLevel is variant level two data
     * @param selectedVariant is variant option one and two level selected
     * @param variantData is all variant data
     */
    private fun processVariantTwoLevel(
        variantTwoLevel: Variant,
        variantData: ProductVariant,
        selectedVariant: List<String>
    ): VariantCategory {
        val hasCustomImagesTwoLevel = variantTwoLevel.options.all {
            it.picture?.url100?.isNotEmpty() == true
        }
        val uiTwoLevel = processVariantOptionTwoLevel(
            variantTwoLevel = variantTwoLevel,
            variantData = variantData,
            selectedVariant = selectedVariant,
            hasCustomImagesTwoLevel = hasCustomImagesTwoLevel
        )
        return VariantCategory(
            name = variantTwoLevel.name.orEmpty(),
            identifier = variantTwoLevel.identifier.orEmpty(),
            variantGuideline = variantData.getVariantGuideline(
                sizeIdentifier = variantTwoLevel.isSizeIdentifier
            ),
            isLeaf = true,
            hasCustomImage = hasCustomImagesTwoLevel,
            variantOptions = uiTwoLevel
        )
    }

    /**
     * Loop each variant options in the variant data for checking to their children
     */
    private fun processVariantOptionTwoLevel(
        variantTwoLevel: Variant,
        variantData: ProductVariant,
        selectedVariant: List<String>,
        hasCustomImagesTwoLevel: Boolean
    ) = variantTwoLevel.options.map { option ->
        var state = VariantConstant.STATE_EMPTY
        var isFlashSale = false
        var stock = 0

        for (child in variantData.children) {
            if (option.id in selectedVariant && child.optionIds == selectedVariant) {
                // this condition is the currently selected variant option
                // when variant option is in variant selected and child options ids equals selected variant

                isFlashSale = child.isFlashSale
                stock = child.stock?.stock.orZero()
                state = if (child.isBuyable) { // selected and can to buy
                    VariantConstant.STATE_SELECTED
                } else { // selected and can not to buy
                    VariantConstant.STATE_SELECTED_EMPTY
                }
                break
            } else if (option.id == child.optionIds.lastOrNull() && selectedVariant.firstOrNull() == child.optionIds.firstOrNull()) {
                // match variant child's id from
                // [0] is variant on one level -> from option id
                // [1] is variant on two level -> from variant selected get first item

                isFlashSale = child.isFlashSale
                stock = child.stock?.stock.orZero()
                state = if (child.isBuyable) { // un-selected and can to buy
                    VariantConstant.STATE_UNSELECTED
                } else { // un-selected and can not to buy because stock is empty
                    VariantConstant.STATE_EMPTY
                }
                break
            }
        }

        // create variant options ui model
        VariantOptionWithAttribute.create(
            variantOption = option,
            variant = variantTwoLevel,
            state = state,
            stock = stock,
            hasCustomImages = hasCustomImagesTwoLevel,
            isFlashSale = isFlashSale,
            level = AtcVariantNewMapper.VARIANT_LEVEL_TWO_SELECTED
        )
    }

    /***
     * Process variant one level after process variant two level
     * @param variantOneLevel is variant level one data
     * @param selectedVariant is variant option one and two level selected
     * @param variantData is all variant data
     */
    private fun processVariantOneLevel(
        variantOneLevel: Variant,
        selectedVariant: List<String>,
        variantData: ProductVariant
    ): VariantCategory {
        val hasCustomImagesOneLevel = variantOneLevel.options.all {
            it.picture?.url100?.isNotEmpty() == true
        }
        val uiOneLevel = processVariantOptionOneLevel(
            variantOneLevel = variantOneLevel,
            selectedVariant = selectedVariant,
            variantData = variantData,
            hasCustomImagesOneLevel = hasCustomImagesOneLevel
        )
        return VariantCategory(
            name = variantOneLevel.name.orEmpty(),
            identifier = variantOneLevel.identifier.orEmpty(),
            variantGuideline = variantData.getVariantGuideline(
                sizeIdentifier = variantOneLevel.isSizeIdentifier
            ),
            isLeaf = false,
            hasCustomImage = hasCustomImagesOneLevel,
            variantOptions = uiOneLevel
        )
    }

    /**
     * Loop each variant options in the variant data for checking to their children
     */
    private fun processVariantOptionOneLevel(
        variantOneLevel: Variant,
        selectedVariant: List<String>,
        variantData: ProductVariant,
        hasCustomImagesOneLevel: Boolean
    ) = variantOneLevel.options.map { option ->
        var state = VariantConstant.STATE_EMPTY
        var isFlashSale = false
        var stock = 0

        for (child in variantData.children) {
            if (option.id in selectedVariant && child.optionIds == selectedVariant) {
                // this condition is the currently selected variant option
                // when variant option is in variant selected and child options ids equals selected variant

                isFlashSale = child.isFlashSale
                stock = child.stock?.stock.orZero()
                state = if (child.isBuyable) { // selected and can to buy
                    VariantConstant.STATE_SELECTED
                } else { // selected and can not to buy
                    VariantConstant.STATE_SELECTED_EMPTY
                }
                break
            } else if (option.id == child.optionIds.firstOrNull() && selectedVariant.lastOrNull() == child.optionIds.lastOrNull()) {
                // match variant child's id from
                // [0] is variant on one level -> from variant selected get last item
                // [1] is variant on two level -> from option id

                isFlashSale = child.isFlashSale
                stock = child.stock?.stock.orZero()
                state = if (child.isBuyable) { // un-selected and can to buy
                    VariantConstant.STATE_UNSELECTED
                } else { // un-selected and can not to buy because stock is empty
                    VariantConstant.STATE_EMPTY
                }
                break
            }
        }

        // create variant options ui model
        VariantOptionWithAttribute.create(
            variantOption = option,
            variant = variantOneLevel,
            state = state,
            stock = stock,
            hasCustomImages = hasCustomImagesOneLevel,
            isFlashSale = isFlashSale,
            level = AtcVariantNewMapper.VARIANT_LEVEL_ONE_SELECTED
        )
    }
}
