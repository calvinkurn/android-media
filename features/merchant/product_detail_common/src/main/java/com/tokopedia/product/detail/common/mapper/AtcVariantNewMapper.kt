package com.tokopedia.product.detail.common.mapper

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute

/**
 * Created by yovi.putra on 08/03/23"
 * Project name: android-tokopedia-core
 **/

object AtcVariantNewMapper {
    private const val VARIANT_HAVE_ONE_LEVEL = 1
    private const val VARIANT_HAVE_TWO_LEVEL = 2

    private const val VARIANT_LEVEL_ONE_SELECTED = 0
    private const val VARIANT_LEVEL_TWO_SELECTED = 1

    fun processVariantNew(
        variantData: ProductVariant?,
        selectedVariant: MutableMap<String, String>?,
        level: Int
    ): List<VariantCategory>? {
        val variants = variantData ?: return null
        if (variants.variants.isEmpty()) return null
        val variantSize = variants.variants.size
        // for new logic, initialize value by default is level one selected
        val selectedLevel = if (level == AtcVariantMapper.VARIANT_LEVEL_INITIALIZE) {
            VARIANT_LEVEL_ONE_SELECTED
        } else {
            level
        }

        return when (variantSize) {
            VARIANT_HAVE_ONE_LEVEL -> {
                processVariantOneLevel(
                    variantData = variantData,
                    selectedVariant = selectedVariant.orEmpty()
                )
            }
            VARIANT_HAVE_TWO_LEVEL -> {
                processVariantTwoLevel(
                    variantData = variantData,
                    selectedVariant = selectedVariant.orEmpty(),
                    selectedLevel = selectedLevel
                )
            }
            else -> null
        }
    }

    private fun processVariantOneLevel(
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
                level = VARIANT_LEVEL_ONE_SELECTED
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

    private fun processVariantTwoLevel(
        variantData: ProductVariant,
        selectedVariant: Map<String, String>,
        selectedLevel: Int
    ): List<VariantCategory>? = when (selectedLevel) {
        VARIANT_LEVEL_ONE_SELECTED -> processVariantTwoLevelWithOneLeveSelected(
            variantData = variantData,
            mapOfSelectedVariant = selectedVariant
        )
        VARIANT_LEVEL_TWO_SELECTED -> processVariantTwoLevelWithTwoLeveSelected(
            variantData = variantData,
            mapOfSelectedVariant = selectedVariant
        )
        else -> null
    }

    private fun processVariantTwoLevelWithOneLeveSelected(
        variantData: ProductVariant,
        mapOfSelectedVariant: Map<String, String>
    ): List<VariantCategory>? {
        val selectedVariant = mapOfSelectedVariant.values.toList()
        // process variant state on level one
        val variantOneLevel = variantData.variants.getOrNull(VARIANT_LEVEL_ONE_SELECTED) ?: return null
        val hasCustomImagesOneLevel = variantOneLevel.options.all {
            it.picture?.url100?.isNotEmpty() == true
        }
        // loop each variant options in the variant data for checking to their children
        val uiOneLevel = variantOneLevel.options.map { option ->
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
                } else if (option.id == child.optionIds.firstOrNull() && child.optionIds.lastOrNull() == selectedVariant.lastOrNull()) {
                    // this condition is the un-selected variant option
                    // validate base on index. index 0 is level one and index 1 is level two
                    // if variant option in level one equals by child option id index 0 and
                    // child option id last position (index 1) equals by selected variant last position (index 1)

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
                level = VARIANT_LEVEL_ONE_SELECTED
            )
        }

        // process variant state on two level
        val variantTwoLevel = variantData.variants.getOrNull(VARIANT_LEVEL_TWO_SELECTED) ?: return null
        val hasCustomImagesTwoLevel = variantTwoLevel.options.all {
            it.picture?.url100?.isNotEmpty() == true
        }
        // loop each variant options in the variant data for checking to their children
        val uiTwoLevel = variantTwoLevel.options.map { option ->
            var state = VariantConstant.STATE_EMPTY
            var isFlashSale = false
            var stock = 0

            if (option.id in selectedVariant) {
                for (child in variantData.children) {
                    // if variant option into variant selected and variant selected ids into child optionIds
                    if (child.optionIds == selectedVariant) {
                        isFlashSale = child.isFlashSale
                        stock = child.stock?.stock.orZero()
                        state = if (child.isBuyable) { // selected and can to buy
                            VariantConstant.STATE_SELECTED
                        } else { // selected and can not to buy
                            VariantConstant.STATE_SELECTED_EMPTY
                        }
                        break
                    }
                }
            } else {
                for (child in variantData.children) {
                    // match variant id from
                    // [0] is variant on one level -> from variant selected get first item
                    // [1] is variant on two level -> from option id
                    if (option.id == child.optionIds.lastOrNull() && selectedVariant.firstOrNull() == child.optionIds.firstOrNull()) {
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
                variant = variantTwoLevel,
                state = state,
                stock = stock,
                hasCustomImages = hasCustomImagesTwoLevel,
                isFlashSale = isFlashSale,
                level = VARIANT_LEVEL_TWO_SELECTED
            )
        }

        return listOf(
            // create variant ui model
            VariantCategory(
                name = variantOneLevel.name.orEmpty(),
                identifier = variantOneLevel.identifier.orEmpty(),
                variantGuideline = variantData.getVariantGuideline(
                    sizeIdentifier = variantOneLevel.isSizeIdentifier
                ),
                isLeaf = false,
                hasCustomImage = hasCustomImagesOneLevel,
                variantOptions = uiOneLevel
            ),
            // create variant ui model
            VariantCategory(
                name = variantTwoLevel.name.orEmpty(),
                identifier = variantTwoLevel.identifier.orEmpty(),
                variantGuideline = variantData.getVariantGuideline(
                    sizeIdentifier = variantTwoLevel.isSizeIdentifier
                ),
                isLeaf = true,
                hasCustomImage = hasCustomImagesTwoLevel,
                variantOptions = uiTwoLevel
            )
        )
    }

    private fun processVariantTwoLevelWithTwoLeveSelected(
        variantData: ProductVariant,
        mapOfSelectedVariant: Map<String, String>
    ): List<VariantCategory>? {
        val selectedVariant = mapOfSelectedVariant.values.toList()
        // update variant state on one level
        val variantTwoLevel = variantData.variants.getOrNull(VARIANT_LEVEL_TWO_SELECTED) ?: return null
        val hasCustomImagesOneLevel = variantTwoLevel.options.all {
            it.picture?.url100?.isNotEmpty() == true
        }
        val uiTwoLevel = variantTwoLevel.options.map { option ->
            var state = VariantConstant.STATE_EMPTY
            var isFlashSale = false
            var stock = 0

            for (child in variantData.children) {
                // if variant option in selected variant and child options ids equals selected variant
                if (option.id in selectedVariant && child.optionIds == selectedVariant) {
                    isFlashSale = child.isFlashSale
                    stock = child.stock?.stock.orZero()
                    state = if (child.isBuyable) { // selected and can to buy
                        VariantConstant.STATE_SELECTED
                    } else { // selected and can not to buy
                        VariantConstant.STATE_SELECTED_EMPTY
                    }
                    break
                } else if (option.id == child.optionIds.lastOrNull() && selectedVariant.firstOrNull() == child.optionIds.firstOrNull()) {
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
                hasCustomImages = hasCustomImagesOneLevel,
                isFlashSale = isFlashSale,
                level = VARIANT_LEVEL_TWO_SELECTED
            )
        }

        // update variant state on two level
        val variantOneLevel = variantData.variants.getOrNull(VARIANT_LEVEL_ONE_SELECTED) ?: return null
        val hasCustomImagesTwoLevel = variantOneLevel.options.all {
            it.picture?.url100?.isNotEmpty() == true
        }
        val uiOneLevel = variantOneLevel.options.map { option ->
            var state = VariantConstant.STATE_EMPTY
            var isFlashSale = false
            var stock = 0

            if (option.id in selectedVariant) {
                for (child in variantData.children) {
                    // if variant option into variant selected and variant selected ids into child optionIds
                    if (child.optionIds == selectedVariant) {
                        isFlashSale = child.isFlashSale
                        stock = child.stock?.stock.orZero()
                        state = if (child.isBuyable) { // selected and can to buy
                            VariantConstant.STATE_SELECTED
                        } else { // selected and can not to buy
                            VariantConstant.STATE_SELECTED_EMPTY
                        }
                        break
                    }
                }
            } else {
                for (child in variantData.children) {
                    // match variant id from
                    // [0] is variant on one level -> from variant selected get first item
                    // [1] is variant on two level -> from option id
                    if (option.id == child.optionIds.firstOrNull() && selectedVariant.lastOrNull() == child.optionIds.lastOrNull()) {
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
                variant = variantOneLevel,
                state = state,
                stock = stock,
                hasCustomImages = hasCustomImagesTwoLevel,
                isFlashSale = isFlashSale,
                level = VARIANT_LEVEL_ONE_SELECTED
            )
        }

        return listOf(
            // create variant ui model
            VariantCategory(
                name = variantOneLevel.name.orEmpty(),
                identifier = variantOneLevel.identifier.orEmpty(),
                variantGuideline = variantData.getVariantGuideline(
                    sizeIdentifier = variantOneLevel.isSizeIdentifier
                ),
                isLeaf = false,
                hasCustomImage = hasCustomImagesOneLevel,
                variantOptions = uiOneLevel
            ),
            // create variant ui model
            VariantCategory(
                name = variantTwoLevel.name.orEmpty(),
                identifier = variantTwoLevel.identifier.orEmpty(),
                variantGuideline = variantData.getVariantGuideline(
                    sizeIdentifier = variantTwoLevel.isSizeIdentifier
                ),
                isLeaf = true,
                hasCustomImage = hasCustomImagesTwoLevel,
                variantOptions = uiTwoLevel
            )
        )
    }
}
