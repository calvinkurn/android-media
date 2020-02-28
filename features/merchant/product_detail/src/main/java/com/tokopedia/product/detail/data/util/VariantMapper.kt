package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.variant.Child
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.data.model.variant.VariantCategory
import com.tokopedia.product.detail.data.model.variant.VariantOptionWithAttribute

/**
 * Created by Yehezkiel on 2020-02-26
 */
object VariantMapper {

    fun mapVariantIdentifierToHashMap(variantData: ProductVariant?): MutableMap<String, Int> {
        return variantData?.variant?.associateBy({
            it.identifier ?: ""
        }, {
            0
        })?.toMutableMap() ?: mutableMapOf()
    }

    fun processVariant(variantData: ProductVariant?, mapOfSelectedVariant: MutableMap<String, Int>? = mutableMapOf()): MutableList<VariantCategory>? {
        if (variantData == null) return null
        val listOfVariant: MutableList<VariantCategory> = arrayListOf()

        val mapOfSelectedVariantTest:MutableMap<String,Int> = mutableMapOf()
//        mapOfSelectedVariantTest["colour"] = 45903582
//        mapOfSelectedVariantTest["size"] = 45903584


        val selectedOptionIds: List<Int> = mapOfSelectedVariant?.map { //[Merah,S]
            it.value
        } ?: listOf()

        //Check wether selected product is buyable , if not get another  siblings that buyable
        var selectedProductData = getSelectedProductData(selectedOptionIds, variantData)
        if (selectedProductData?.isBuyable != true) {
            selectedProductData = getOtherSiblingProduct(variantData, selectedOptionIds)
        }

        val isSelectedProductBuyable = selectedProductData?.isBuyable ?: false
        val updatedSelectedOptionIds = selectedProductData?.optionIds ?: listOf()

        for ((level, variant: Variant) in variantData.variant.withIndex()) {
            listOfVariant.add(convertVariantViewModel(variant, variantData, level, updatedSelectedOptionIds, (level + 1) == variantData.variant.size,
                    isSelectedProductBuyable))
        }

        return listOfVariant

    }

    fun convertVariantViewModel(variant: Variant, variantData: ProductVariant, level: Int, selectedOptionIds: List<Int>, isParentBranch: Boolean,
                                isSelectedProductBuyable: Boolean): VariantCategory {
        val variantDataModel = VariantCategory(variant.name ?: "", variant.identifier ?: "")
        variantDataModel.variantGuideline = if (variant.isSizeIdentifier && variantData.sizeChart.isNotEmpty()) {
            variantData.sizeChart
        } else {
            ""
        }
        variantDataModel.hasCustomImage = variant.options.all {
            it.picture?.thumbnail?.isNotEmpty() == true
        }

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

            optionVariantDataModel.currentState = ProductDetailConstant.STATE_EMPTY

            if (selectedOptionIds.isNotEmpty() && option.id in selectedOptionIds) {
                if (isSelectedProductBuyable)
                    optionVariantDataModel.currentState = ProductDetailConstant.STATE_SELECTED
            } else {
                variantData.children.forEach { child ->
                    if (child.isBuyable && child.optionIds[level] == option.id) {

                        if (partialSelectedListByLevel.isEmpty()) {
                            optionVariantDataModel.currentState = ProductDetailConstant.STATE_UNSELECTED
                        } else {
                            val childOptionId = child.optionIds.getOrNull(level)
                            childOptionId?.let {
                                if (child.optionIds.subList(0, level) == partialSelectedListByLevel) {
                                    //User selecting 2+ level variant
                                    optionVariantDataModel.stock = child.stock?.stock ?: 0
                                    optionVariantDataModel.currentState = ProductDetailConstant.STATE_UNSELECTED
                                } else if (selectedOptionIds.isEmpty()) {
                                    //Means first time, and the variant is not selected at all
                                    optionVariantDataModel.currentState = ProductDetailConstant.STATE_UNSELECTED
                                }
                            }
                        }
                    }
                }
            }


            variantDataModel.variantOptions.add(optionVariantDataModel)
        }

        return variantDataModel
    }

    private fun getSelectedProductData(selectedOptionIds: List<Int>, variantData: ProductVariant): Child? {
        return variantData.children.find {
            it.optionIds == selectedOptionIds
        }
    }

    private fun getOtherSiblingProduct(productInfoAndVariant: ProductVariant?, optionId: List<Int>): Child? {
        var selectedChild: Child? = null
        // we need to reselect other variant
        productInfoAndVariant?.run {
            var optionPartialSize = optionId.size - 1
            while (optionPartialSize > -1) {
                val partialOptionIdList = optionId.subList(0, optionPartialSize)
                for (childLoop: Child in children) {
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
}
