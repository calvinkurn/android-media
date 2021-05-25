package com.tkpd.atc_variant.util

import com.tkpd.atc_variant.data.uidata.*
import com.tkpd.atc_variant.views.adapter.AtcVariantVisitable
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.minicart.common.domain.data.MiniCartItem
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
     * if product parent, select first buyable child, if all child empty, select the first one
     * if product not buyable, also ignore
     *
     * auto select will run if there is only 1 child left buyable
     */
    fun determineSelectedOptionIds(isParent: Boolean, variantData: ProductVariant?, selectedChild: VariantChild?): MutableMap<String, String> {
        val shouldAutoSelect = variantData?.autoSelectedOptionIds() ?: listOf()
        return when {
            isParent -> {
                if (selectedChild == null) {
                    AtcVariantMapper.mapVariantIdentifierToHashMap(variantData)
                } else {
                    AtcVariantMapper.mapVariantIdentifierWithDefaultSelectedToHashMap(variantData, selectedChild.optionIds)
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
                       totalStock: Int,
                       miniCartData: MiniCartItem?,
                       selectedQuantity: Int): List<AtcVariantVisitable>? {
        if (processedVariant == null) return null

        var idCounter = 0L
        val result: MutableList<AtcVariantVisitable> = mutableListOf()

        val headerData = generateHeaderDataModel(selectedChild)
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

        result.add(
                VariantQuantityDataModel(
                        position = idCounter,
                        productId = selectedChild?.productId ?: "",
                        quantity = miniCartData?.quantity
                                ?: selectedQuantity,
                        minOrder = selectedChild?.getFinalMinOrder() ?: 0,
                        shouldShowView = isTokoNow && selectedChild?.isBuyable == true,
                        cartId = miniCartData?.cartId ?: "")
        ).also {
            idCounter += 1
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
                        selectedProductFulfillment: Boolean,
                        miniCartData: MiniCartItem?,
                        isTokoNow: Boolean,
                        selectedQuantity: Int): List<AtcVariantVisitable> {

        return oldList.map {
            when (it) {
                is VariantComponentDataModel -> {
                    it.copy(listOfVariantCategory = processedVariant,
                            mapOfSelectedVariant = selectedVariantIds
                                    ?: mutableMapOf(),
                            isEmptyStock = allChildEmpty,
                            isTokoCabang = selectedProductFulfillment)
                }
                is VariantQuantityDataModel -> {
                    it.copy(productId = selectedVariantChild?.productId ?: "",
                            quantity = miniCartData?.quantity
                                    ?: selectedQuantity,
                            minOrder = selectedVariantChild?.getFinalMinOrder() ?: 0,
                            shouldShowView = isTokoNow && selectedVariantChild?.isBuyable == true,
                            cartId = miniCartData?.cartId ?: "")
                }
                is VariantHeaderDataModel -> {
                    if (isPartiallySelected) {
                        //update image only when exist
                        it.copy(productImage = variantImage)
                    } else {
                        val headerData = generateHeaderDataModel(selectedVariantChild)
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
    private fun generateHeaderDataModel(selectedChild: VariantChild?): Pair<String, ProductHeaderData> {
        val productImage = selectedChild?.picture?.original ?: ""
        val headerData = ProductHeaderData(
                productId = selectedChild?.productId ?: "",
                productMainPrice = selectedChild?.finalMainPrice?.getCurrencyFormatted()
                        ?: "",
                productDiscountedPercentage = selectedChild?.campaign?.discountedPercentage?.toInt()
                        ?: 0,
                isCampaignActive = selectedChild?.campaign?.isActive ?: false,
                productSlashPrice = selectedChild?.campaign?.discountedPrice?.getCurrencyFormatted()
                        ?: "",
                productStockWording = selectedChild?.stock?.stockWordingHTML
                        ?: ""
        )
        return productImage to headerData
    }

    fun <T : Any> T.asSuccess(): Success<T> = Success(this)
    fun Throwable.asFail(): Fail = Fail(this)
}