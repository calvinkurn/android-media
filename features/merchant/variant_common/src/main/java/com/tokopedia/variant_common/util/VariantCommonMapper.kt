package com.tokopedia.variant_common.util

import com.tokopedia.variant_common.constant.VariantConstant
import com.tokopedia.variant_common.model.*

/**
 * Created by Yehezkiel on 08/03/20
 */

object VariantCommonMapper {
    var selectedOptionId = listOf<Int>()

    fun mapVariantIdentifierToHashMap(variantData: ProductVariantCommon?): MutableMap<String, Int> {
        return variantData?.variant?.associateBy({
            it.identifier ?: ""
        }, {
            0
        })?.toMutableMap() ?: mutableMapOf()
    }

    fun processVariant(variantData: ProductVariantCommon?, mapOfSelectedVariant: MutableMap<String, Int>? = mutableMapOf(), level: Int = -1, isPartialySelected: Boolean = false): MutableList<VariantCategory>? {
        if (variantData == null) return null

        val listOfVariant: MutableList<VariantCategory> = arrayListOf()
        var updatedSelectedOptionsId: List<Int>
        val isSelectedLevelOne = level < 1

        //Parse selectedOptionsId Map to List<Int>
        val selectedOptionIds: List<Int> = mapOfSelectedVariant?.map {
            //[Merah,S]
            it.value
        } ?: listOf()

        // If user selected only 1 level, we have to filter and generate only 1 list
        // If not we will get [0,SizeId] or [WarnaId,0]
        updatedSelectedOptionsId = selectedOptionIds.filterNot {
            it == 0
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

        selectedOptionId = updatedSelectedOptionsId

        for ((level, variant: Variant) in variantData.variant.withIndex()) {
            listOfVariant.add(convertVariantViewModel(variant, variantData, level, updatedSelectedOptionsId, (level + 1) == variantData.variant.size,
                    isSelectedProductBuyable, isPartialySelected, isSelectedLevelOne))
        }

        return listOfVariant
    }

    fun selectedProductData(variantData: ProductVariantCommon): VariantChildCommon? {
        return variantData.children.find {
            it.optionIds == selectedOptionId
        }
    }

    private fun updateSelectedOptionsIds(variantData: ProductVariantCommon, updatedSelectedOptionsId: List<Int>, mapOfSelectedVariant: MutableMap<String, Int>?) {
        variantData.variant.forEachIndexed { index, variant ->
            mapOfSelectedVariant?.set(variant.identifier ?: "", updatedSelectedOptionsId[index])
        }
    }

    private fun convertVariantViewModel(variant: Variant, variantData: ProductVariantCommon, level: Int, selectedOptionIds: List<Int>, isLeaf: Boolean,
                                        isSelectedProductBuyable: Boolean,
                                        partialySelected: Boolean,
                                        selectedLevelOne: Boolean): VariantCategory {
        val variantDataModel = VariantCategory(variant.name ?: "", variant.identifier ?: "")
        variantDataModel.variantGuideline = if (variant.isSizeIdentifier && variantData.sizeChart.isNotEmpty()) {
            variantData.sizeChart
        } else {
            ""
        }
        variantDataModel.isLeaf = isLeaf

        //If all options has images, show images, if not show colour type / chip type
        val hasCustomImage = variant.options.all {
            it.picture?.thumbnail?.isNotEmpty() == true
        }

        variantDataModel.hasCustomImage = hasCustomImage

        val partialSelectedListByLevel = if (selectedOptionIds.isNotEmpty()) {
            selectedOptionIds.subList(0, level)
        } else {
            selectedOptionIds
        }

        variant.options.forEach { option ->
            val optionVariantDataModel = VariantOptionWithAttribute()
            optionVariantDataModel.variantName = option.value ?: ""
            optionVariantDataModel.variantId = option.id ?: 0
            optionVariantDataModel.image = option.picture?.thumbnail ?: ""
            optionVariantDataModel.variantHex = option.hex ?: ""

            optionVariantDataModel.currentState = VariantConstant.STATE_EMPTY

            if (selectedOptionIds.isNotEmpty() && option.id in selectedOptionIds) {
                if (isSelectedProductBuyable) {
                    optionVariantDataModel.currentState = VariantConstant.STATE_SELECTED
                } else {
                    for (child: VariantChildCommon in variantData.children) {
                        if (child.isBuyable && selectedOptionIds.first() in child.optionIds) {
                            optionVariantDataModel.currentState = VariantConstant.STATE_SELECTED
                            break
                        }
                    }
                }
            } else {
                for (child: VariantChildCommon in variantData.children) {
                    //child.optionIds[1] means variant lvl2
                    //child.optionIds[0] means variant lvl1
                    //Check one by one wether childId is match with another Id
                    if (child.isBuyable && child.optionIds[level] == option.id) {
                        //|| (partialySelected && !isLeaf)
                        if (partialSelectedListByLevel.isEmpty()) {
                            // if not lvl 1, should not go to this if , so have to check if not leaf
                            optionVariantDataModel.currentState = VariantConstant.STATE_UNSELECTED
                        } else {
                            val childOptionId = child.optionIds.getOrNull(level)
                            childOptionId?.let {
                                if (child.optionIds.subList(0, level) == partialSelectedListByLevel) {
                                    //User selecting 2+ level variant
                                    optionVariantDataModel.currentState = VariantConstant.STATE_UNSELECTED
                                    // || (partialySelected && isLeaf)
                                } else if (selectedOptionIds.isEmpty()) {
                                    optionVariantDataModel.currentState = VariantConstant.STATE_UNSELECTED
                                }

                                // This code is works if user only select 1 level and its leaf
                                if (partialySelected) {
                                    if (selectedLevelOne) return@let
                                    if (isLeaf)
                                        optionVariantDataModel.currentState = VariantConstant.STATE_UNSELECTED
                                }

                            }
                        }
                    }
                    optionVariantDataModel.stock = child.stock?.stock ?: 0
                }
            }
            optionVariantDataModel.hasCustomImages = hasCustomImage
            optionVariantDataModel.level = level
            optionVariantDataModel.variantOptionIdentifier = variant.identifier ?: ""
            variantDataModel.variantOptions.add(optionVariantDataModel)
        }

        return variantDataModel
    }

    private fun getOtherSiblingProduct(productInfoAndVariant: ProductVariantCommon?, optionId: List<Int>): VariantChildCommon? {
        var selectedChild: VariantChildCommon? = null
        // we need to reselect other variant
        productInfoAndVariant?.run {
            var optionPartialSize = optionId.size - 1
            while (optionPartialSize > -1) {
                val partialOptionIdList = optionId.subList(0, optionPartialSize)
                for (childLoop: VariantChildCommon in children) {
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

    private fun getSelectedProductData(selectedOptionIds: List<Int>, variantData: ProductVariantCommon): VariantChildCommon? {
        return variantData.children.find {
            it.optionIds == selectedOptionIds
        }
    }

}
