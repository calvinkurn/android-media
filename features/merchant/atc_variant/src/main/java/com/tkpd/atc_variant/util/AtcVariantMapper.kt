package com.tkpd.atc_variant.util


import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.detail.common.VariantConstant
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantOptionWithAttribute

/**
 * Created by Yehezkiel on 10/05/21
 */

object AtcVariantMapper {

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

    fun mapVariantIdentifierWithDefaultSelectedToHashMap(variantData: ProductVariant?, selectedOptionIds: List<String>): MutableMap<String, String> {
        val hashMap: MutableMap<String, String> = mutableMapOf()

        variantData?.variants?.mapIndexed { index, variant ->
            hashMap[variant.pv.toString()] = selectedOptionIds.getOrNull(index) ?: "0"
        }

        return hashMap
    }

    fun processVariant(variantData: ProductVariant?, mapOfSelectedVariant: MutableMap<String, String>? = mutableMapOf(), level: Int = -1): List<VariantCategory>? {
        val variantChilderValidation = validateVariantChildren(variantData?.children
                ?: listOf(), variantData?.variants?.size ?: 0)
        if (variantData == null) return null
        if (!variantChilderValidation) return null

        val isPartialySelected = isPartiallySelectedOptionId(mapOfSelectedVariant)
        val listOfVariant: MutableList<VariantCategory> = mutableListOf()
        var updatedSelectedOptionsId: List<String>
        val isSelectedLevelOne = level < 1

        //Parse selectedOptionsId Map to List<Int>
        val selectedOptionIds: List<String> = mapOfSelectedVariant?.map {
            //[Merah,S]
            it.value
        } ?: listOf()

        // If user selected only 1 level, we have to filter and generate only 1 list
        // If not we will get [0,SizeId] or [WarnaId,0]
        updatedSelectedOptionsId = selectedOptionIds.filterNot {
            it.toLong() == 0L
        }

        //Check wether selected product is buyable , if not get another  siblings that buyable
        val selectedProductData = getSelectedProductData(updatedSelectedOptionsId, variantData)

        //If selectedProductIds is not buyable choose another buyable child
        if (selectedProductData != null && !selectedProductData.isBuyable) {
            updatedSelectedOptionsId = getOtherSiblingProduct(variantData, selectedOptionIds)?.optionIds
                    ?: listOf()
            updateSelectedOptionsIds(variantData, updatedSelectedOptionsId, mapOfSelectedVariant)
        }
        val isSelectedProductBuyable = selectedProductData?.isBuyable ?: false
        val isSelectedProductFlashSale = selectedProductData?.isFlashSale ?: false

        for ((level, variant: Variant) in variantData.variants.withIndex()) {
            listOfVariant.add(convertVariantViewModel(variant, variantData, level, updatedSelectedOptionsId, (level + 1) == variantData.variants.size,
                    isSelectedProductBuyable, isPartialySelected, isSelectedLevelOne, isSelectedProductFlashSale))
        }

        return listOfVariant
    }

    private fun updateSelectedOptionsIds(variantData: ProductVariant, updatedSelectedOptionsId: List<String>, mapOfSelectedVariant: MutableMap<String, String>?) {
        variantData.variants.forEachIndexed { index, variant ->
            mapOfSelectedVariant?.set(variant.pv.toString(), updatedSelectedOptionsId[index])
        }
    }

    private fun convertVariantViewModel(variant: Variant, variantData: ProductVariant, level: Int, selectedOptionIds: List<String>, isLeaf: Boolean,
                                        isSelectedProductBuyable: Boolean,
                                        partialySelected: Boolean,
                                        selectedLevelOne: Boolean,
                                        isSelectedProductFlashSale: Boolean): VariantCategory {

        //If all options has images, show images, if not show colour type / chip type
        val hasCustomImage = variant.options.all {
            it.picture?.url100?.isNotEmpty() == true
        }

        val partialSelectedListByLevel = if (selectedOptionIds.isNotEmpty()) {
            selectedOptionIds.subList(0, level)
        } else {
            selectedOptionIds
        }

        val optionList = variant.options.map { option ->
            var currentState = VariantConstant.STATE_EMPTY
            var stock = 0
            var isFlashSale = false
            if (selectedOptionIds.isNotEmpty() && option.id in selectedOptionIds) {
                if (isSelectedProductBuyable) {
                    // This Function is Fired When User Already Select All Of The Variant
                    currentState = VariantConstant.STATE_SELECTED
                    isFlashSale = if (level == 0) {
                        variantData.isSelectedChildHasFlashSale(selectedOptionIds[level])
                    } else {
                        isSelectedProductFlashSale
                    }
                } else {
                    // This Function is Fired When User Selected Partial Variant
                    for (child: VariantChild in variantData.children) {
                        if (child.isBuyable && selectedOptionIds.first() in child.optionIds) {
                            currentState = VariantConstant.STATE_SELECTED
                            if (selectedLevelOne && child.isFlashSale) {
                                isFlashSale = true
                                break
                            }
                        }
                    }
                }
            } else {
                //This Function is to determine unselect or empty variant
                for (child: VariantChild in variantData.children) {
                    //child.optionIds[1] means variant lvl2
                    //child.optionIds[0] means variant lvl1
                    //Check one by one wether childId is match with another Id
                    if (child.isBuyable && child.optionIds[level] == option.id) {
                        if (partialSelectedListByLevel.isEmpty()) {
                            //It means level 1
                            currentState = VariantConstant.STATE_UNSELECTED
                            if (level == 0 && child.isFlashSale) {
                                isFlashSale = true
                            }
                        } else {
                            val childOptionId = child.optionIds.getOrNull(level)
                            childOptionId?.let {
                                if (child.optionIds.subList(0, level) == partialSelectedListByLevel) {
                                    //Check if the combination is match with child
                                    currentState = VariantConstant.STATE_UNSELECTED
                                    isFlashSale = child.isFlashSale
                                } else if (selectedOptionIds.isEmpty()) {
                                    currentState = VariantConstant.STATE_UNSELECTED
                                }

                                // This code is works if user only select 1 level and its leaf
                                if (partialySelected) {
                                    if (selectedLevelOne) return@let
                                    if (isLeaf)
                                        currentState = VariantConstant.STATE_UNSELECTED
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

    private fun getOtherSiblingProduct(productInfoAndVariant: ProductVariant?, optionId: List<String>): VariantChild? {
        var selectedChild: VariantChild? = null
        // we need to reselect other variant
        productInfoAndVariant?.run {
            var optionPartialSize = optionId.size - 1
            while (optionPartialSize > -1) {
                val partialOptionIdList = optionId.subList(0, optionPartialSize)
                for (childLoop: VariantChild in children) {
                    if (!childLoop.isBuyable) {
                        continue
                    }
                    if (optionPartialSize == 0) {
                        selectedChild = childLoop
                        break
                    }
                    if (childLoop.optionIds.subList(0, optionPartialSize) == partialOptionIdList) {
                        selectedChild = childLoop
                        break
                    }
                }
                if (selectedChild != null) {
                    break
                }
                optionPartialSize--
            }
        }
        return selectedChild
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
}