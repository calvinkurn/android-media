package com.tkpd.atc_variant.util

import com.tkpd.atc_variant.data.uidata.*
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.product.detail.common.AtcVariantMapper
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

/**
 * Created by Yehezkiel on 11/05/21
 */
object AtcCommonMapper {

    /**
     * Generate selected option ids for initial variant selection state
     * if product parent, we dont want to select the variant except it has abillity to auto select
     * if product not buyable, also ignore
     *
     * auto select will run if there is only 1 child left buyable
     */
    fun determineSelectedOptionIds(isParent: Boolean, variantData: ProductVariant, selectedChild: VariantChild?): MutableMap<String, String> {
        val shouldAutoSelect = variantData.autoSelectedOptionIds()
        return when {
            isParent -> {
                if (shouldAutoSelect.isNotEmpty()) {
                    //if product parent and able to auto select, do auto select
                    AtcVariantMapper.mapVariantIdentifierWithDefaultSelectedToHashMap(variantData, shouldAutoSelect)
                } else {
                    //if product parent, dont update selected variant
                    AtcVariantMapper.mapVariantIdentifierToHashMap(variantData)
                }
            }
            selectedChild?.isBuyable == true -> {
                AtcVariantMapper.mapVariantIdentifierWithDefaultSelectedToHashMap(variantData, selectedChild.optionIds)
            }
            shouldAutoSelect.isNotEmpty() -> {
                AtcVariantMapper.mapVariantIdentifierWithDefaultSelectedToHashMap(variantData, shouldAutoSelect)
            }
            else -> {
                AtcVariantMapper.mapVariantIdentifierToHashMap(variantData)
            }
        }
    }

    fun mapToCartRedirectionData(selectedChild: VariantChild?, cartTypeData: Map<String, CartTypeData>?, isShopOwner: Boolean = false): PartialButtonDataModel {
        return PartialButtonDataModel(selectedChild?.isBuyable
                ?: false, isShopOwner, cartTypeData?.get(selectedChild?.productId ?: ""))
    }

    fun mapToVisitable(selectedChild: VariantChild?,
                       isTokoNow: Boolean,
                       initialSelectedVariant: MutableMap<String, String>,
                       processedVariant: List<VariantCategory>?,
                       selectedProductFulfillment: Boolean,
                       totalStock: Int): List<AtcVariantVisitable>? {
        if (processedVariant == null) return null

        var idCounter = 0L
        val result: MutableList<AtcVariantVisitable> = mutableListOf()
        val isInitialState = initialSelectedVariant.values.all {
            it == "0"
        }

        val headerData = generateHeaderDataModel(selectedChild, isInitialState, totalStock)
        result.add(
                VariantHeaderDataModel(
                        position = idCounter,
                        productImage = headerData.first,
                        headerData = headerData.second)
        ).also {
            idCounter += 1
        }

        result.add(
                VariantComponentDataModel(
                        position = idCounter,
                        listOfVariantCategory = processedVariant,
                        mapOfSelectedVariant = initialSelectedVariant,
                        isEmptyStock = totalStock == 0,
                        isTokoCabang = selectedProductFulfillment)
        ).also {
            idCounter += 1
        }

        if (isTokoNow) {
            result.add(
                    VariantQuantityDataModel(
                            position = idCounter,
                            productId = selectedChild?.productId ?: "")
            ).also {
                idCounter += 1
            }
        }

        return result
    }

    fun updateVisitable(oldList: List<AtcVariantVisitable>,
                        processedVariant: List<VariantCategory>?,
                        isPartiallySelected: Boolean,
                        selectedVariantIds: MutableMap<String, String>?,
                        allChildEmpty: Boolean,
                        selectedVariantChild: VariantChild?,
                        variantImage: String,
                        selectedProductFulfillment: Boolean): List<AtcVariantVisitable> {

        return oldList.map {
            when (it) {
                is VariantComponentDataModel -> {
                    it.copy(listOfVariantCategory = processedVariant,
                            mapOfSelectedVariant = selectedVariantIds
                                    ?: mutableMapOf(),
                            isEmptyStock = allChildEmpty,
                            isTokoCabang = selectedProductFulfillment)
                }
                is VariantHeaderDataModel -> {
                    if (isPartiallySelected) {
                        //update image only when exist
                        it.copy(productImage = variantImage)
                    } else {
                        val headerData = generateHeaderDataModel(selectedChild = selectedVariantChild)
                        it.copy(productImage = headerData.first, headerData = headerData.second)
                    }
                }
                else -> {
                    it
                }
            }
        }
    }

    fun updateActivityResultData(recentData: ProductVariantResult?,
                                 listVariant: List<VariantCategory>? = null,
                                 selectedProductId: String? = null,
                                 parentProductId: String? = null,
                                 mapOfSelectedVariantOption: MutableMap<String, String>? = null,
                                 atcMessage: String? = null,
                                 shouldRefreshPreviousPage: Boolean? = null,
                                 requestCode: Int? = null): ProductVariantResult {
        val result = recentData?.copy() ?: ProductVariantResult()

        if (listVariant != null) result.listOfVariantSelected = listVariant
        if (selectedProductId != null) result.selectedProductId = selectedProductId
        if (mapOfSelectedVariantOption != null) result.mapOfSelectedVariantOption = mapOfSelectedVariantOption
        if (atcMessage != null) result.atcMessage = atcMessage
        if (parentProductId != null) result.parentProductId = parentProductId
        if (shouldRefreshPreviousPage != null) result.shouldRefreshPreviousPage = shouldRefreshPreviousPage
        if (requestCode != null) result.requestCode = requestCode

        return result
    }

    /**
     * @isInitialState means user not yet select any variant, so we need to calculate total stock
     */
    private fun generateHeaderDataModel(selectedChild: VariantChild?,
                                        isInitialState: Boolean = false,
                                        totalStock: Int = 0): Pair<String, ProductHeaderData> {
        val productImage = selectedChild?.picture?.original ?: ""
        val headerData = ProductHeaderData(
                productMainPrice = selectedChild?.finalMainPrice?.getCurrencyFormatted()
                        ?: "",
                productDiscountedPercentage = selectedChild?.campaign?.discountedPercentage?.toInt()
                        ?: 0,
                productCampaignIdentifier = selectedChild?.campaign?.campaignIdentifier
                        ?: 0,
                productSlashPrice = selectedChild?.campaign?.discountedPrice?.getCurrencyFormatted()
                        ?: "",
                productStockWording = selectedChild?.stock?.stockWordingHTML
                        ?: "",
                isProductBuyable = selectedChild?.isBuyable ?: false,
                totalStock = totalStock,
                isInitialState = isInitialState
        )
        return productImage to headerData
    }

    fun <T : Any> T.asSuccess(): Success<T> = Success(this)
    fun Throwable.asFail(): Fail = Fail(this)
}