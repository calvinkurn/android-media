package com.tkpd.atcvariant.util

import android.content.Intent
import com.tkpd.atcvariant.data.uidata.*
import com.tkpd.atcvariant.view.adapter.AtcVariantVisitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.atc_common.AtcFromExternalSource
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiCartParam
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartOcsRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.attachcommon.preview.ProductPreview
import com.tokopedia.common.network.util.CommonUtil
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

    private const val KEY_COLOUR_VARIANT = "colour"
    private const val KEY_VALUE_VARIANT = "value"
    private const val KEY_HEX_VARIANT = "hex"
    private const val KEY_ID_VARIANT = "id"
    private const val KEY_SIZE_VARIANT = "size"
    private const val DEFAULT_MIN_ORDER = 1

    fun generateAtcData(actionButtonCart: Int,
                        selectedChild: VariantChild?,
                        selectedWarehouse: WarehouseInfo?,
                        shopIdInt: Int,
                        trackerAttributionPdp: String,
                        trackerListNamePdp: String,
                        categoryName: String,
                        shippingMinPrice: Double,
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
                    shippingPrice = shippingMinPrice.roundToIntOrZero()
                    productName = selectedChild?.name ?: ""
                    category = categoryName
                    price = selectedChild?.finalPrice?.toString() ?: ""
                    this.userId = userId
                }
            }
            ProductDetailCommonConstant.OCC_BUTTON -> {
                AddToCartOccMultiRequestParams(
                        carts = listOf(
                                AddToCartOccMultiCartParam(
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
                                }
                        ),
                        userId = userId,
                        atcFromExternalSource = AtcFromExternalSource.ATC_FROM_PDP
                )
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
                    atcFromExternalSource = AtcFromExternalSource.ATC_FROM_PDP
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
    fun determineSelectedOptionIds(variantData: ProductVariant?, selectedChild: VariantChild?): MutableMap<String, String> {
        return if (selectedChild == null) {
            AtcVariantMapper.mapVariantIdentifierToHashMap(variantData)
        } else {
            AtcVariantMapper.mapVariantIdentifierWithDefaultSelectedToHashMap(variantData, selectedChild.optionIds)
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
                color = remindMeAlternateCopy?.color
                        ?: ProductDetailCommonConstant.KEY_BUTTON_SECONDARY_GRAY,
                text = remindMeAlternateCopy?.text ?: ProductDetailCommonConstant.TEXT_REMIND_ME
        ) ?: return null)
    }

    fun mapToVisitable(selectedChild: VariantChild?,
                       isTokoNow: Boolean,
                       initialSelectedVariant: MutableMap<String, String>,
                       processedVariant: List<VariantCategory>?,
                       selectedProductFulfillment: Boolean,
                       selectedQuantity: Int,
                       shouldShowDeleteButton: Boolean,
                       uspImageUrl: String,
                       cashBackPercentage: Int): List<AtcVariantVisitable>? {
        if (processedVariant == null) return null

        var idCounter = 0L
        val result: MutableList<AtcVariantVisitable> = mutableListOf()

        val headerData = generateHeaderDataModel(selectedChild)
        result.add(
                VariantHeaderDataModel(
                        position = idCounter,
                        productId = selectedChild?.productId ?: "",
                        productImage = headerData.first,
                        listOfVariantTitle = selectedChild?.optionName ?: listOf(),
                        isTokoCabang = selectedProductFulfillment,
                        uspImageUrl = uspImageUrl,
                        cashBackPercentage = cashBackPercentage,
                        headerData = headerData.second)
        ).also {
            idCounter += 1
        }

        result.add(
                VariantComponentDataModel(
                        position = idCounter,
                        listOfVariantCategory = processedVariant,
                        mapOfSelectedVariant = initialSelectedVariant)
        ).also {
            idCounter += 1
        }

        result.add(
                VariantQuantityDataModel(
                        position = idCounter,
                        productId = selectedChild?.productId ?: "",
                        quantity = selectedQuantity,
                        minOrder = selectedChild?.getFinalMinOrder() ?: 0,
                        maxOrder = selectedChild?.getFinalMaxOrder() ?: DEFAULT_ATC_MAX_ORDER,
                        shouldShowDeleteButton = shouldShowDeleteButton,
                        shouldShowView = isTokoNow && selectedChild?.isBuyable == true)
        ).also {
            idCounter += 1
        }

        return result
    }

    fun updateDeleteButtonQtyEditor(oldList: List<AtcVariantVisitable>, value: Boolean): List<AtcVariantVisitable> {
        return oldList.map {
            if (it is VariantQuantityDataModel) {
                val currentQuantity = it.quantity
                //if value == false we need to reset quantity editor to 0
                it.copy(shouldShowDeleteButton = value, quantity = if (!value) 0 else currentQuantity)
            } else {
                it
            }
        }
    }

    fun updateVisitable(oldList: List<AtcVariantVisitable>,
                        processedVariant: List<VariantCategory>?,
                        isPartiallySelected: Boolean,
                        selectedVariantIds: MutableMap<String, String>?,
                        selectedVariantChild: VariantChild?,
                        variantImage: String,
                        selectedProductFulfillment: Boolean,
                        isTokoNow: Boolean,
                        selectedQuantity: Int,
                        shouldShowDeleteButton: Boolean): List<AtcVariantVisitable> {

        return oldList.map {
            when (it) {
                is VariantComponentDataModel -> {
                    it.copy(listOfVariantCategory = processedVariant,
                            mapOfSelectedVariant = selectedVariantIds
                                    ?: mutableMapOf())
                }
                is VariantQuantityDataModel -> {
                    it.copy(productId = selectedVariantChild?.productId ?: "",
                            quantity = selectedQuantity,
                            minOrder = selectedVariantChild?.getFinalMinOrder() ?: 0,
                            maxOrder = selectedVariantChild?.getFinalMaxOrder()
                                    ?: DEFAULT_ATC_MAX_ORDER,
                            shouldShowDeleteButton = shouldShowDeleteButton,
                            shouldShowView = isTokoNow && selectedVariantChild?.isBuyable == true)
                }
                is VariantHeaderDataModel -> {
                    if (isPartiallySelected) {
                        //update image only when exist
                        it.copy(productImage = variantImage)
                    } else {
                        val headerData = generateHeaderDataModel(selectedVariantChild)
                        it.copy(productImage = headerData.first,
                                productId = selectedVariantChild?.productId ?: "",
                                headerData = headerData.second,
                                isTokoCabang = selectedProductFulfillment,
                                listOfVariantTitle = selectedVariantChild?.optionName ?: listOf())
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
                                 isFollowShop: Boolean? = null,
                                 requestCode: Int? = null): ProductVariantResult {
        val result = recentData?.copy() ?: ProductVariantResult()

        if (selectedProductId != null) result.selectedProductId = selectedProductId
        if (mapOfSelectedVariantOption != null) result.mapOfSelectedVariantOption = mapOfSelectedVariantOption
        if (atcMessage != null) result.atcMessage = atcMessage
        if (parentProductId != null) result.parentProductId = parentProductId
        if (shouldRefreshPreviousPage != null) result.shouldRefreshPreviousPage = shouldRefreshPreviousPage
        if (requestCode != null) result.requestCode = requestCode
        if (isFollowShop != null) result.isFollowShop = isFollowShop

        return result
    }

    fun putChatProductInfoTo(
            intent: Intent?,
            productId: String?,
            productInfo: VariantChild?,
            variantResp: ProductVariant?,
            freeOngkirImgUrl: String
    ) {
        if (intent == null || productId == null) return
        val variants = variantResp?.mapSelectedProductVariants(productId)
        val productImageUrl = productInfo?.picture?.original ?: ""
        val productName = productInfo?.name ?: ""
        val productPrice = productInfo?.finalPrice?.getCurrencyFormatted() ?: ""
        val priceBeforeDouble = productInfo?.slashPriceDouble ?: 0.0
        val priceBefore = if (priceBeforeDouble > 0) {
            priceBeforeDouble.getCurrencyFormatted()
        } else {
            ""
        }
        val dropPercentage = productInfo?.roundedDiscountPercentage ?: ""
        val productUrl = productInfo?.url ?: ""
        val isActive = productInfo?.isBuyable ?: true
        val productFsIsActive = freeOngkirImgUrl.isNotEmpty()
        val productColorVariant = variants?.get(KEY_COLOUR_VARIANT)?.get(KEY_VALUE_VARIANT) ?: ""
        val productColorHexVariant = variants?.get(KEY_COLOUR_VARIANT)?.get(KEY_HEX_VARIANT) ?: ""
        val productSizeVariant = variants?.get(KEY_SIZE_VARIANT)?.get(KEY_VALUE_VARIANT) ?: ""
        val productColorVariantId = variants?.get(KEY_COLOUR_VARIANT)?.get(KEY_ID_VARIANT) ?: ""
        val productSizeVariantId = variants?.get(KEY_SIZE_VARIANT)?.get(KEY_ID_VARIANT) ?: ""
        val isSupportVariant = variants != null
        val productPreview = ProductPreview(
                id = productId,
                imageUrl = productImageUrl,
                name = productName,
                price = productPrice,
                colorVariantId = productColorVariantId,
                colorVariant = productColorVariant,
                colorHexVariant = productColorHexVariant,
                sizeVariantId = productSizeVariantId,
                sizeVariant = productSizeVariant,
                url = productUrl,
                productFsIsActive = productFsIsActive,
                productFsImageUrl = freeOngkirImgUrl,
                priceBefore = priceBefore,
                priceBeforeInt = priceBeforeDouble,
                dropPercentage = dropPercentage,
                isActive = isActive,
                remainingStock = productInfo?.getVariantFinalStock() ?: DEFAULT_MIN_ORDER,
                isSupportVariant = isSupportVariant,
                campaignId = productInfo?.campaign?.campaignID.toLongOrZero()
        )
        val productPreviews = listOf(productPreview)
        val stringProductPreviews = CommonUtil.toJson(productPreviews)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }

    /**
     * @isInitialState means user not yet select any variant, so we need to calculate total stock
     */
    private fun generateHeaderDataModel(selectedChild: VariantChild?): Pair<String, ProductHeaderData> {
        val productImage = selectedChild?.picture?.original ?: ""
        val isCampaignActive = if (selectedChild?.campaign?.hideGimmick == true) false
        else
            selectedChild?.campaign?.isActive ?: false

        val headerData = ProductHeaderData(
                productMainPrice = selectedChild?.finalMainPrice?.getCurrencyFormatted()
                        ?: "",
                productDiscountedPercentage = selectedChild?.campaign?.discountedPercentage?.toInt()
                        ?: 0,
                isCampaignActive = isCampaignActive,
                productSlashPrice = selectedChild?.campaign?.discountedPrice?.getCurrencyFormatted()
                        ?: "",
                productStockFmt = selectedChild?.stock?.stockFmt ?: ""
        )
        return productImage to headerData
    }

    fun <T : Any> T.asSuccess(): Success<T> = Success(this)
    fun Throwable.asFail(): Fail = Fail(this)
}