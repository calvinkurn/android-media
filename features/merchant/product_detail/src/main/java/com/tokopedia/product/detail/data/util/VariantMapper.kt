package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.pdplayout.BasicInfo
import com.tokopedia.product.detail.common.data.model.pdplayout.ComponentData
import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1
import com.tokopedia.product.detail.common.data.model.pdplayout.Media
import com.tokopedia.product.detail.common.data.model.variant.Child
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.Variant
import com.tokopedia.product.detail.data.model.variant.VariantCategory
import com.tokopedia.product.detail.data.model.variant.VariantOptionWithAttribute

/**
 * Created by Yehezkiel on 2020-02-26
 */
object VariantMapper {

    var selectedOptionId = listOf<Int>()

    fun mapVariantIdentifierToHashMap(variantData: ProductVariant?): MutableMap<String, Int> {
        return variantData?.variant?.associateBy({
            it.identifier ?: ""
        }, {
            0
        })?.toMutableMap() ?: mutableMapOf()
    }

    fun processVariant(variantData: ProductVariant?, mapOfSelectedVariant: MutableMap<String, Int>? = mutableMapOf(), level: Int = -1): MutableList<VariantCategory>? {
        if (variantData == null) return null

        val listOfVariant: MutableList<VariantCategory> = arrayListOf()
        var updatedSelectedOptionsId: List<Int>
        val isSelectedLevelOne = level < 1

        //Means if variant level has 2 level and user partialy select only 1 variant
        val isPartialySelected = mapOfSelectedVariant?.any {
            it.value == 0
        } ?: false

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

    private fun updateSelectedOptionsIds(variantData: ProductVariant, updatedSelectedOptionsId: List<Int>, mapOfSelectedVariant: MutableMap<String, Int>?) {
        variantData.variant.forEachIndexed { index, variant ->
            mapOfSelectedVariant?.set(variant.identifier ?: "", updatedSelectedOptionsId[index])
        }
    }

    private fun convertVariantViewModel(variant: Variant, variantData: ProductVariant, level: Int, selectedOptionIds: List<Int>, isLeaf: Boolean,
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
                if (isSelectedProductBuyable) {
                    optionVariantDataModel.currentState = ProductDetailConstant.STATE_SELECTED
                } else {
                    for (child: Child in variantData.children) {
                        if (child.isBuyable && selectedOptionIds.first() in child.optionIds) {
                            optionVariantDataModel.currentState = ProductDetailConstant.STATE_SELECTED
                            break
                        }
                    }
                }
            } else {
                for (child: Child in variantData.children) {
                    //child.optionIds[1] means variant lvl2
                    //child.optionIds[0] means variant lvl1
                    //Check one by one wether childId is match with another Id
                    if (child.isBuyable && child.optionIds[level] == option.id) {
                        //|| (partialySelected && !isLeaf)
                        if (partialSelectedListByLevel.isEmpty()) {
                            // if not lvl 1, should not go to this if , so have to check if not leaf
                            optionVariantDataModel.currentState = ProductDetailConstant.STATE_UNSELECTED
                        } else {
                            val childOptionId = child.optionIds.getOrNull(level)
                            childOptionId?.let {
                                if (child.optionIds.subList(0, level) == partialSelectedListByLevel) {
                                    //User selecting 2+ level variant
                                    optionVariantDataModel.currentState = ProductDetailConstant.STATE_UNSELECTED
                                    // || (partialySelected && isLeaf)
                                } else if (selectedOptionIds.isEmpty()) {
                                    optionVariantDataModel.currentState = ProductDetailConstant.STATE_UNSELECTED
                                }

                                // This code is works if user only select 1 level and its leaf
                                if (partialySelected) {
                                    if (selectedLevelOne) return@let
                                    if (isLeaf)
                                        optionVariantDataModel.currentState = ProductDetailConstant.STATE_UNSELECTED
                                }

                            }
                        }
                    }
                    optionVariantDataModel.stock = child.stock?.stock ?: 0
                }
            }
            optionVariantDataModel.level = level
            optionVariantDataModel.variantOptionIdentifier = variant.identifier ?: ""
            variantDataModel.variantOptions.add(optionVariantDataModel)
        }

        return variantDataModel
    }

    private fun getSelectedProductData(selectedOptionIds: List<Int>, variantData: ProductVariant): Child? {
        return variantData.children.find {
            it.optionIds == selectedOptionIds
        }
    }

    fun selectedProductData(variantData: ProductVariant): Child? {
        return variantData.children.find {
            it.optionIds == selectedOptionId
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

    fun updateDynamicProductInfo(oldData: DynamicProductInfoP1?, newData: Child?, existingListMedia: List<Media>?): DynamicProductInfoP1? {
        if (oldData == null) return null

        val basic = oldData.basic.copy(
                productID = newData?.productId.toString(),
                sku = newData?.sku ?: "",
                minOrder = newData?.stock?.minimumOrder ?: 0,
                url = newData?.url ?: "")

        val newCampaign = oldData.data.campaign.copy(
                campaignID = newData?.campaign?.campaignID ?: "",
                campaignType = newData?.campaign?.campaignType.toString(),
                campaignTypeName = newData?.campaign?.campaignTypeName ?: "",
                isActive = newData?.campaign?.isActive ?: false,
                originalPrice = newData?.campaign?.originalPrice?.toInt() ?: 0,
                discountedPrice = newData?.campaign?.discountedPrice?.toInt() ?: 0,
                startDate = newData?.campaign?.startDate ?: "",
                endDate = newData?.campaign?.endDate ?: "",
                stock = newData?.campaign?.stock ?: 0,
                isAppsOnly = newData?.campaign?.isAppsOnly ?: false,
                appLinks = newData?.campaign?.applinks ?: ""
        )

        val newMedia = if (newData?.hasPicture == true) {
            val copyOfOldMedia = existingListMedia?.toMutableList()
            copyOfOldMedia?.add(0, Media(type = "image", uRL300 = newData.picture?.original
                    ?: "", uRLOriginal = newData.picture?.original
                    ?: "", uRLThumbnail = newData.picture?.original ?: ""))
            copyOfOldMedia ?: mutableListOf()
        } else {
            oldData.data.media
        }

        val newPrice = oldData.data.price.copy(
                value = newData?.price?.toInt() ?: 0
        )

        val data = oldData.data.copy(
                isCOD = newData?.isCod ?: false,
                isWishlist = newData?.isWishlist ?: false,
                campaign = newCampaign,
                price = newPrice,
                name= newData?.name ?: "",
                media = newMedia)

        return DynamicProductInfoP1(basic, data, oldData.layoutName)
    }

    fun updateMediaToCurrentP1Data(oldData: DynamicProductInfoP1?, media: MutableList<Media>): DynamicProductInfoP1 {
        val basic = oldData?.basic?.copy()
        val data = oldData?.data?.copy(
                media = media
        )
        return DynamicProductInfoP1(basic ?: BasicInfo(),data ?: ComponentData(), oldData?.layoutName ?: "")
    }
}
