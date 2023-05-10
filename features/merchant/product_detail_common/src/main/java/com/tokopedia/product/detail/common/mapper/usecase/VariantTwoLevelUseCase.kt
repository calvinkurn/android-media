package com.tokopedia.product.detail.common.mapper.usecase

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute
import com.tokopedia.product.detail.common.mapper.AtcVariantNewMapper.VARIANT_LEVEL_ONE_INDEX
import com.tokopedia.product.detail.common.mapper.AtcVariantNewMapper.VARIANT_LEVEL_TWO_INDEX

/**
 * Created by yovi.putra on 08/03/23"
 * Project name: android-tokopedia-core
 **/

object VariantTwoLevelUseCase {

    fun process(
        variantData: ProductVariant,
        mapOfSelectedVariant: Map<String, String>
    ): List<VariantCategory>? {
        val selectedVariant = mapOfSelectedVariant.values.toList()
        // process variant state on level one
        val variantLevelOne = variantData.variants.getOrNull(
            VARIANT_LEVEL_ONE_INDEX
        ) ?: return null
        val uiVariantLevelOne = processVariantLevelOne(
            variantLevelOne = variantLevelOne,
            variantData = variantData,
            selectedVariant = selectedVariant
        )

        // process variant state on level two
        val variantLevelTwo = variantData.variants.getOrNull(
            VARIANT_LEVEL_TWO_INDEX
        ) ?: return null
        val uiVariantLevelTwo = processVariantLevelTwo(
            variantLevelTwo = variantLevelTwo,
            selectedVariant = selectedVariant,
            variantData = variantData
        )

        return listOf(uiVariantLevelOne, uiVariantLevelTwo)
    }

    /***
     * Process variant one level before process variant two level
     * @param variantLevelOne is variant level one data
     * @param selectedVariant is variant option one and two level selected
     * @param variantData is all variant data
     */
    private fun processVariantLevelOne(
        variantLevelOne: Variant,
        variantData: ProductVariant,
        selectedVariant: List<String>
    ): VariantCategory {
        val hasCustomImagesLevelOne = variantLevelOne.options.all {
            it.picture?.url100?.isNotEmpty() == true
        }
        val uiVariantOption = processVariantOptionLevelOne(
            variantLevelOne = variantLevelOne,
            variantData = variantData,
            selectedVariant = selectedVariant,
            hasCustomImagesLevelOne = hasCustomImagesLevelOne
        )
        // create variant ui model
        return VariantCategory(
            name = variantLevelOne.name.orEmpty(),
            identifier = variantLevelOne.identifier.orEmpty(),
            variantGuideline = variantData.getVariantGuideline(
                sizeIdentifier = variantLevelOne.isSizeIdentifier
            ),
            isLeaf = false,
            hasCustomImage = hasCustomImagesLevelOne,
            variantOptions = uiVariantOption
        )
    }

    /**
     * Loop each variant options in the variant data for checking to their children
     */
    private fun processVariantOptionLevelOne(
        variantLevelOne: Variant,
        variantData: ProductVariant,
        selectedVariant: List<String>,
        hasCustomImagesLevelOne: Boolean
    ) = variantLevelOne.options.map { option ->
        var state = VariantConstant.STATE_EMPTY
        var isFlashSale = false
        var stock = 0

        // looping the children for set state, flash sale and stock
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
            } else {
                // build variant combination for checking to children optionIds
                // with option id in level one and  first item of selectedVariant in level two
                val selectedVariantLevelTwo = selectedVariant.getOrNull(VARIANT_LEVEL_TWO_INDEX)
                val combineVariant = listOf(option.id, selectedVariantLevelTwo)
                // this condition is the un-selected variant option
                // [0] is variant on one level -> from variant option id
                // [1] is variant on two level -> from variant selected get last item
                if (combineVariant == child.optionIds) {
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
        }

        // create variant options ui model
        VariantOptionWithAttribute.create(
            variantOption = option,
            variant = variantLevelOne,
            state = state,
            stock = stock,
            hasCustomImages = hasCustomImagesLevelOne,
            isFlashSale = isFlashSale,
            level = VARIANT_LEVEL_ONE_INDEX
        )
    }

    /***
     * Process variant two level after process variant one level
     * @param variantLevelTwo is variant level two data
     * @param selectedVariant is variant option one and two level selected
     * @param variantData is all variant data
     */
    private fun processVariantLevelTwo(
        variantLevelTwo: Variant,
        selectedVariant: List<String>,
        variantData: ProductVariant
    ): VariantCategory {
        val hasCustomImagesLevelTwo = variantLevelTwo.options.all {
            it.picture?.url100?.isNotEmpty() == true
        }
        // loop each variant options in the variant data for checking to their children
        val uiVariantOption = processVariantOptionLevelTwo(
            variantLevelTwo = variantLevelTwo,
            selectedVariant = selectedVariant,
            variantData = variantData,
            hasCustomImagesLevelTwo = hasCustomImagesLevelTwo
        )
        return VariantCategory(
            name = variantLevelTwo.name.orEmpty(),
            identifier = variantLevelTwo.identifier.orEmpty(),
            variantGuideline = variantData.getVariantGuideline(
                sizeIdentifier = variantLevelTwo.isSizeIdentifier
            ),
            isLeaf = true,
            hasCustomImage = hasCustomImagesLevelTwo,
            variantOptions = uiVariantOption
        )
    }

    /**
     * Loop each variant options in the variant data for checking to their children
     */
    private fun processVariantOptionLevelTwo(
        variantLevelTwo: Variant,
        selectedVariant: List<String>,
        variantData: ProductVariant,
        hasCustomImagesLevelTwo: Boolean
    ) = variantLevelTwo.options.map { option ->
        var state = VariantConstant.STATE_EMPTY
        var isFlashSale = false
        var stock = 0

        // looping the children for set state, flash sale and stock
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
            } else {
                // build variant combination for checking to children optionIds
                // with option id in level two and  first item of selectedVariant in level one
                val selectedVariantLevelOne = selectedVariant.getOrNull(VARIANT_LEVEL_ONE_INDEX)
                val combineVariant = listOf(selectedVariantLevelOne, option.id)
                // match variant id from
                // [0] is variant on one level -> from variant selected get first item
                // [1] is variant on two level -> from option id
                if (combineVariant == child.optionIds) {
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
        }

        // create variant options ui model
        VariantOptionWithAttribute.create(
            variantOption = option,
            variant = variantLevelTwo,
            state = state,
            stock = stock,
            hasCustomImages = hasCustomImagesLevelTwo,
            isFlashSale = isFlashSale,
            level = VARIANT_LEVEL_TWO_INDEX
        )
    }
}
