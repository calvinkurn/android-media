package com.tokopedia.product.detail.common

import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute

/**
 * Created by Yehezkiel on 10/05/21
 * Mapper merely regarding variant logic
 */

object AtcVariantMapper {

    /**
     * Determine wether variant is select fully or not
     * fully means select 2 of 2 variant / 1 of 1 variant
     */
    fun isPartiallySelectedOptionId(selectedOptionIds: Map<String, String>?): Boolean {
        if (selectedOptionIds == null) return false

        return selectedOptionIds.any {
            it.value.toLongOrZero() == 0L
        } || selectedOptionIds.isEmpty()
    }

    fun mapVariantIdentifierToHashMap(variantData: ProductVariant?): MutableMap<String, String> {
        return variantData?.variants?.associateBy({
            it.pv.toString()
        }, {
            "0"
        })?.toMutableMap() ?: mutableMapOf()
    }

    fun mapVariantIdentifierWithDefaultSelectedToHashMap(
        variantData: ProductVariant?,
        selectedOptionIds: List<String>?
    ): MutableMap<String, String> {
        val hashMap: MutableMap<String, String> = mutableMapOf()

        variantData?.variants?.mapIndexed { index, variant ->
            hashMap[variant.pv.toString()] = selectedOptionIds?.getOrNull(index) ?: "0"
        }

        return hashMap
    }

    /**
     * Generate list of variant data that ready to render
     */
    fun processVariant(
        variantData: ProductVariant?,
        mapOfSelectedVariant: MutableMap<String, String>? = mutableMapOf(),
        level: Int = 0,
        isNew: Boolean = true
    ): List<VariantCategory>? = if (isNew) {
        processVariantNew(variantData, mapOfSelectedVariant, level)
    } else {
        processVariantOld(variantData, mapOfSelectedVariant, level)
    }

    private fun processVariantOld(variantData: ProductVariant?, mapOfSelectedVariant: MutableMap<String, String>? = mutableMapOf(), level: Int = -1): List<VariantCategory>? {
        val variantChilderValidation = validateVariantChildren(
            variantData?.children
                ?: listOf(),
            variantData?.variants?.size ?: 0
        )
        if (variantData == null) return null
        if (!variantChilderValidation) return null

        val isPartialySelected = isPartiallySelectedOptionId(mapOfSelectedVariant)
        val listOfVariant: MutableList<VariantCategory> = mutableListOf()
        val isSelectedLevelOne = level < 1

        // Parse selectedOptionsId Map to List<String>
        // If user selected only 1 level, we have to filter and generate only 1 list
        // If not we will get [0,SizeId] or [WarnaId,0]
        val selectedOptionIds: List<String> = mapOfSelectedVariant?.map {
            // [Merah,S]
            it.value
        }?.filterNot {
            it.toLong() == 0L
        } ?: listOf()

        // Check wether selected product is buyable , if not get another  siblings that buyable
        val selectedChild = getSelectedProductData(selectedOptionIds, variantData)
        val isFlashSale = selectedChild?.isFlashSale ?: false

        for ((level, variant: Variant) in variantData.variants.withIndex()) {
            listOfVariant.add(
                convertVariantViewModel(
                    variant,
                    variantData,
                    level,
                    selectedOptionIds,
                    (level + 1) == variantData.variants.size,
                    isPartialySelected,
                    isSelectedLevelOne,
                    isFlashSale
                )
            )
        }

        return listOfVariant
    }

    private fun convertVariantViewModel(
        variant: Variant,
        variantData: ProductVariant,
        level: Int,
        selectedOptionIds: List<String>,
        isLeaf: Boolean,
        partialySelected: Boolean,
        selectedLevelOne: Boolean,
        isSelectedProductFlashSale: Boolean
    ): VariantCategory {
        // If all options has images, show images, if not show colour type / chip type
        val hasCustomImage = variant.options.all {
            it.picture?.url100?.isNotEmpty() == true
        }

        val shouldDetermineLevel2 = if (selectedOptionIds.isNotEmpty()) {
            selectedOptionIds.subList(0, level)
        } else {
            selectedOptionIds
        }

        val optionList = variant.options.map { option ->
            var currentState = VariantConstant.STATE_EMPTY
            var stock = 0
            var isFlashSale = false
            if (selectedOptionIds.isNotEmpty() && option.id in selectedOptionIds) {
                if (!partialySelected) {
                    // This Function is Fired When User Already Select All Of The Variant
                    var isOneOfChildBuyable = false
                    if (level == 0) { // itteration at variants level 0
                        isOneOfChildBuyable = variantData.isOneOfTheChildBuyablePartial(selectedOptionIds.first())
                        isFlashSale = variantData.isSelectedChildHasFlashSale(selectedOptionIds[level])
                    } else {
                        isOneOfChildBuyable = variantData.isOneOfTheChildBuyable(selectedOptionIds)
                        isFlashSale = isSelectedProductFlashSale
                    }
                    currentState = if (isOneOfChildBuyable) VariantConstant.STATE_SELECTED else VariantConstant.STATE_SELECTED_EMPTY
                } else {
                    // This Function is Fired When User Selected Partial Variant
                    val isOneOfChildBuyable = variantData.isOneOfTheChildBuyablePartial(selectedOptionIds.first())
                    currentState = if (isOneOfChildBuyable) VariantConstant.STATE_SELECTED else VariantConstant.STATE_SELECTED_EMPTY
                    if (selectedLevelOne && variantData.isSelectedChildHasFlashSale(selectedOptionIds.first())) {
                        isFlashSale = true
                    }
                }
            } else {
                // This Function is to determine unselect or empty variant
                for (child: VariantChild in variantData.children) {
                    // child.optionIds[1] means variant lvl2
                    // child.optionIds[0] means variant lvl1
                    // Check one by one wether childId is match with another Id
                    if (child.isBuyable && child.optionIds[level] == option.id) {
                        if (shouldDetermineLevel2.isEmpty()) {
                            // It means level 1
                            currentState = VariantConstant.STATE_UNSELECTED
                            if (level == 0 && child.isFlashSale) {
                                isFlashSale = true
                            }
                        } else {
                            val childOptionId = child.optionIds.getOrNull(level)
                            childOptionId?.let {
                                if (child.optionIds.subList(0, level) == shouldDetermineLevel2) {
                                    // Check if the combination is match with child
                                    currentState = VariantConstant.STATE_UNSELECTED
                                    isFlashSale = child.isFlashSale
                                } else if (selectedOptionIds.isEmpty()) {
                                    currentState = VariantConstant.STATE_UNSELECTED
                                }

                                // This code is works if user only select 1 level and its leaf
                                if (partialySelected) {
                                    if (selectedLevelOne) return@let
                                    if (isLeaf) {
                                        currentState = VariantConstant.STATE_UNSELECTED
                                    }
                                }
                            }
                        }
                    }
                    stock = child.stock?.stock ?: 0
                }
            }

            return@map VariantOptionWithAttribute(
                variantName = option.value.orEmpty(),
                variantId = option.id.orEmpty(),
                image100 = option.picture?.url100.orEmpty(),
                imageOriginal = option.picture?.original.orEmpty(),
                variantHex = option.hex.orEmpty(),
                currentState = currentState,
                stock = stock,
                hasCustomImages = hasCustomImage,
                level = level,
                variantOptionIdentifier = variant.identifier.orEmpty(),
                variantCategoryKey = variant.pv.toString(),
                flashSale = isFlashSale
            )
        }

        return VariantCategory(
            name = variant.name.orEmpty(),
            identifier = variant.identifier.orEmpty(),
            variantGuideline = if (variant.isSizeIdentifier && variantData.sizeChart.isNotEmpty()) variantData.sizeChart else "",
            isLeaf = isLeaf,
            hasCustomImage = hasCustomImage,
            variantOptions = optionList
        )
    }

    private fun getSelectedProductData(selectedOptionIds: List<String>, variantData: ProductVariant): VariantChild? {
        return variantData.children.find {
            it.optionIds == selectedOptionIds
        }
    }

    /**
     * Example validation:
     * each child should have the same size option with the variant size.
     * Example: option[red,XL] has size 2, should same with the variant size (color and size)
     */
    private fun validateVariantChildren(childList: List<VariantChild>, variantSize: Int): Boolean {
        for (childModel: VariantChild in childList) {
            if (childModel.optionIds.size != variantSize) {
                return false
            }
        }
        return true
    }

    private fun processVariantNew(
        variantData: ProductVariant?,
        selectedVariant: MutableMap<String, String>? = mutableMapOf(),
        level: Int = -1
    ): List<VariantCategory>? {
        val variants = variantData ?: return null
        if (variantData.variants.isEmpty()) return null
        val variantSize = variants.variants.size

        return if (variantSize == 1 && level == 0) { // one level and level one selected
            processVariantOneLevel(variantData = variantData, selectedVariant = selectedVariant)
        } else {
            processVariantTwoLevel(variantData = variantData, selectedVariant = selectedVariant)
        }
    }

    private fun processVariantOneLevel(
        variantData: ProductVariant?,
        selectedVariant: MutableMap<String, String>? = mutableMapOf()
    ): List<VariantCategory>? {
        val variants = variantData ?: return null
        val variantSelected = selectedVariant?.values?.toList()
        val uiVariants = variants.variants.map { variant ->
            val hasCustomImages = variant.options.all {
                it.picture?.url100?.isNotEmpty() == true
            }

            // loop each variant options in the variant for checking to their children
            val uiVariantOptions = variant.options.map { option ->
                // default state is stock empty
                var state = VariantConstant.STATE_EMPTY
                var isFlashSale = false
                var stock = 0

                // check each variant options to children
                // set state, flash sale and stock
                for (child in variants.children) {
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

                // create variant options ui model
                VariantOptionWithAttribute.create(
                    variantOption = option,
                    variant = variant,
                    state = state,
                    stock = stock,
                    hasCustomImages = hasCustomImages,
                    isFlashSale = isFlashSale,
                    level = 0
                )
            }

            // create variant ui model
            VariantCategory(
                name = variant.name.orEmpty(),
                identifier = variant.identifier.orEmpty(),
                variantGuideline = if (variant.isSizeIdentifier && variantData.sizeChart.isNotEmpty()) variantData.sizeChart else "",
                isLeaf = true,
                hasCustomImage = hasCustomImages,
                variantOptions = uiVariantOptions
            )
        }
        return uiVariants
    }

    private fun processVariantTwoLevel(
        variantData: ProductVariant?,
        selectedVariant: MutableMap<String, String>? = mutableMapOf()
    ): List<VariantCategory>? = null
}
