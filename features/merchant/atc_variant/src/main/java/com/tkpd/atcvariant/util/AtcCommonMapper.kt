package com.tkpd.atcvariant.util

import com.tkpd.atcvariant.data.uidata.*
import com.tkpd.atcvariant.view.adapter.AtcVariantVisitable
import com.tokopedia.atc_common.data.model.request.AddToCartOccRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.detail.common.AtcVariantMapper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantResult
import com.tokopedia.product.detail.common.data.model.carttype.AlternateCopy
import com.tokopedia.product.detail.common.data.model.carttype.AvailableButton
import com.tokopedia.product.detail.common.data.model.carttype.CartTypeData
import com.tokopedia.product.detail.common.data.model.variant.ProductVariant
import com.tokopedia.product.detail.common.data.model.variant.VariantChild
import com.tokopedia.product.detail.common.data.model.variant.uimodel.VariantCategory
import com.tokopedia.product.detail.common.data.model.warehouse.WarehouseInfo
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

/**
 * Created by Yehezkiel on 11/05/21
 */
object AtcCommonMapper {

    fun generateAtcData(actionButtonCart: Int,
                        selectedChild: VariantChild?,
                        selectedWarehouse: WarehouseInfo?,
                        shopIdInt: Int,
                        trackerAttributionPdp: String,
                        trackerListNamePdp: String,
                        categoryName: String,
                        shippingMinPrice: Int,
                        userId: String,
                        isTokoNow: Boolean,
                        selectedStock: Int
    ): Any {
        return when (actionButtonCart) {
            ProductDetailCommonConstant.OCS_BUTTON -> {
                AddToCartOcsRequestParams().apply {
                    productId = selectedChild?.productId?.toLongOrZero() ?: 0L
                    shopId = shopIdInt
                    quantity = selectedChild?.getFinalMinOrder() ?: 0
                    notes = ""
                    customerId = userId.toIntOrZero()
                    warehouseId = selectedWarehouse?.id?.toIntOrZero() ?: 0
                    trackerAttribution = trackerAttributionPdp
                    trackerListName = trackerListNamePdp
                    isTradeIn = false
                    shippingPrice = shippingMinPrice
                    productName = selectedChild?.name ?: ""
                    category = categoryName
                    price = selectedChild?.finalPrice?.toString() ?: ""
                    this.userId = userId
                }
            }
            ProductDetailCommonConstant.OCC_BUTTON -> {
                AddToCartOccRequestParams(
                        productId = selectedChild?.productId ?: "",
                        shopId = shopIdInt.toString(),
                        quantity = selectedChild?.getFinalMinOrder().toString()
                ).apply {
                    warehouseId = selectedWarehouse?.id ?: ""
                    attribution = trackerAttributionPdp
                    listTracker = trackerListNamePdp
                    productName = selectedChild?.name ?: ""
                    category = categoryName
                    price = selectedChild?.finalPrice?.toString() ?: ""
                    this.userId = userId
                }
            }
            else -> {
                AddToCartRequestParams().apply {
                    productId = selectedChild?.productId?.toLongOrZero() ?: 0L
                    shopId = shopIdInt
                    quantity = if (isTokoNow) selectedStock else selectedChild?.getFinalMinOrder()
                            ?: 0
                    notes = ""
                    attribution = trackerAttributionPdp
                    listTracker = trackerListNamePdp
                    warehouseId = selectedWarehouse?.id?.toIntOrZero() ?: 0
                    atcFromExternalSource = AddToCartRequestParams.ATC_FROM_PDP
                    productName = selectedChild?.name ?: ""
                    category = categoryName
                    price = selectedChild?.finalPrice?.toString() ?: ""
                    this.userId = userId
                }
            }
        }
    }

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

    fun mapToCartRedirectionData(selectedChild: VariantChild?,
                                 cartTypeData: Map<String, CartTypeData>?,
                                 isShopOwner: Boolean = false,
                                 shouldUseAlternateTokoNow: Boolean = false,
                                 alternateCopy: List<AlternateCopy>?): PartialButtonDataModel {

        val cartType = cartTypeData?.get(selectedChild?.productId ?: "")
        val data = if (shouldUseAlternateTokoNow) {
            generateAvailableButtonMiniCartVariant(alternateCopy, cartType)
        } else {
            cartType
        }

        return PartialButtonDataModel(selectedChild?.isBuyable
                ?: false, isShopOwner, data)
    }

    private fun generateAvailableButtonMiniCartVariant(alternateCopy: List<AlternateCopy>?, cartTypeData: CartTypeData?): CartTypeData? {
        val alternateText = alternateCopy?.firstOrNull {
            it.cartType == ProductDetailCommonConstant.KEY_CART_TYPE_UPDATE_CART
        }

        return if (cartTypeData != null && cartTypeData.availableButtons.firstOrNull()?.isCartTypeDisabledOrRemindMe() == false) {

            val firstAvailable = cartTypeData.availableButtons.firstOrNull()
            val color = if (alternateText?.color?.isEmpty() == true) firstAvailable?.color else alternateText?.color
            val text = if (alternateText?.text?.isEmpty() == true) ProductDetailCommonConstant.TEXT_SAVE_ATC else alternateText?.text

            cartTypeData.copy(
                    availableButtons = listOf(cartTypeData.availableButtons.firstOrNull()?.copy(
                            color = color ?: ProductDetailCommonConstant.KEY_BUTTON_PRIMARY_GREEN,
                            text = text ?: ProductDetailCommonConstant.TEXT_SAVE_ATC)
                            ?: AvailableButton()))
        } else {
            cartTypeData
        }
    }

    fun generateAvailableButtonIngatkanSaya(alternateCopy: List<AlternateCopy>?, cartTypeData: CartTypeData?): List<AvailableButton>? {
        val remindMeAlternateCopy = alternateCopy?.firstOrNull {
            it.cartType == ProductDetailCommonConstant.KEY_REMIND_ME
        }

        return listOf(cartTypeData?.availableButtons?.firstOrNull()?.copy(
                cartType = ProductDetailCommonConstant.KEY_CHECK_WISHLIST,
                color = remindMeAlternateCopy?.color ?: ProductDetailCommonConstant.KEY_BUTTON_SECONDARY_GREEN,
                text = remindMeAlternateCopy?.text ?: ProductDetailCommonConstant.TEXT_REMIND_ME
        ) ?: return null)
    }

    fun mapToVisitable(selectedChild: VariantChild?,
                       isTokoNow: Boolean,
                       initialSelectedVariant: MutableMap<String, String>,
                       processedVariant: List<VariantCategory>?,
                       selectedProductFulfillment: Boolean,
                       totalStock: Int,
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
                        quantity = selectedQuantity,
                        minOrder = selectedChild?.getFinalMinOrder() ?: 0,
                        maxOrder = selectedChild?.getFinalMaxOrder() ?: 0,
                        shouldShowView = isTokoNow && selectedChild?.isBuyable == true)
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
                            quantity = selectedQuantity,
                            minOrder = selectedVariantChild?.getFinalMinOrder() ?: 0,
                            maxOrder = selectedVariantChild?.getFinalMaxOrder() ?: 0,
                            shouldShowView = isTokoNow && selectedVariantChild?.isBuyable == true)
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
                                 selectedProductId: String? = null,
                                 parentProductId: String? = null,
                                 mapOfSelectedVariantOption: MutableMap<String, String>? = null,
                                 atcMessage: String? = null,
                                 shouldRefreshPreviousPage: Boolean? = null,
                                 requestCode: Int? = null): ProductVariantResult {
        val result = recentData?.copy() ?: ProductVariantResult()

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
                        ?: "",
                productName = selectedChild?.name ?: ""
        )
        return productImage to headerData
    }

    fun <T : Any> T.asSuccess(): Success<T> = Success(this)
    fun Throwable.asFail(): Fail = Fail(this)
}