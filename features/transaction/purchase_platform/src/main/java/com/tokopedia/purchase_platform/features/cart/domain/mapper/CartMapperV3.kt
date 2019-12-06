package com.tokopedia.purchase_platform.features.cart.domain.mapper

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.data.model.response.Messages
import com.tokopedia.purchase_platform.common.data.model.response.WholesalePrice
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.AutoapplyStack
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.Message
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.data.model.VoucherOrdersItem
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.AutoApplyStackData
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.MessageData
import com.tokopedia.purchase_platform.common.feature.promo_auto_apply.domain.model.VoucherOrdersItemData
import com.tokopedia.purchase_platform.common.feature.promo_global.data.model.response.GlobalCouponAttr
import com.tokopedia.purchase_platform.common.feature.promo_global.domain.model.GlobalCouponAttrData
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.SimilarProductData
import com.tokopedia.purchase_platform.common.feature.promo_suggestion.TickerData
import com.tokopedia.purchase_platform.features.cart.data.model.response.*
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.*
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartItemHolderData
import javax.inject.Inject
import kotlin.math.min

/**
 * Created by Irfan Khoirul on 2019-10-17.
 */

class CartMapperV3 @Inject constructor(@ApplicationContext val context: Context) {

    companion object {
        const val SHOP_TYPE_OFFICIAL_STORE = "official_store"
        const val SHOP_TYPE_GOLD_MERCHANT = "gold_merchant"
        const val SHOP_TYPE_REGULER = "reguler"

        const val MERCHANT_VOUCHER_TYPE = "merchant"
    }

    fun convertToCartItemDataList(cartDataListResponse: CartDataListResponse): CartListData {
        val cartListData = CartListData()

        if (cartDataListResponse.tickers.isNotEmpty()) {
            cartListData.tickerData = mapTickerData(cartDataListResponse.tickers)
        }
        cartListData.shopGroupAvailableDataList = mapShopGroupAvailableDataList(cartDataListResponse)
        cartListData.shopGroupWithErrorDataList = mapShopGroupWithErrorDataList(cartDataListResponse)
        cartListData.isPromoCouponActive = cartDataListResponse.isCouponActive == 1

        var errorCount = 0
        for (shopGroupWithError in cartDataListResponse.shopGroupWithErrors) {
            errorCount += shopGroupWithError.totalCartDetailsError
        }
        cartListData.isError = errorCount > 0
        if (cartListData.isError) {
            cartListData.cartTickerErrorData = mapCartTickerErrorData(errorCount)
        }

        cartListData.autoApplyStackData = mapAutoApplyStackData(cartDataListResponse.autoapplyStack)
        cartListData.globalCouponAttrData = mapGlobalCouponAttr(cartDataListResponse.globalCouponAttr)
        cartListData.isAllSelected = cartDataListResponse.isGlobalCheckboxState
        cartListData.isShowOnboarding = false

        mapPromoAnalytics(cartDataListResponse.autoapplyStack, cartListData.shopGroupAvailableDataList)

        return cartListData
    }

    private fun mapTickerData(tickers: List<Ticker>): TickerData {
        val ticker = tickers[0]
        return TickerData(ticker.id, ticker.message, ticker.page)
    }

    private fun mapShopGroupAvailableDataList(cartDataListResponse: CartDataListResponse): List<ShopGroupAvailableData> {
        val shopGroupAvailableDataList = arrayListOf<ShopGroupAvailableData>()
        cartDataListResponse.shopGroupAvailables?.forEach {
            val shopGroupAvailableData = mapShopGroupAvailableData(it, cartDataListResponse)
            shopGroupAvailableDataList.add(shopGroupAvailableData)
        }

        return shopGroupAvailableDataList
    }

    private fun mapShopGroupAvailableData(shopGroupAvailable: ShopGroupAvailable,
                                          cartDataListResponse: CartDataListResponse): ShopGroupAvailableData {
        return ShopGroupAvailableData().let {
            it.isChecked = shopGroupAvailable.checkboxState
            it.isError = false
            it.errorTitle = ""
            it.shopName = shopGroupAvailable.shop.shopName
            it.shopId = shopGroupAvailable.shop.shopId.toString()
            it.shopType =
                    if (shopGroupAvailable.shop.isOfficial == 1) SHOP_TYPE_OFFICIAL_STORE
                    else if (shopGroupAvailable.shop.goldMerchant.isGoldBadge) SHOP_TYPE_GOLD_MERCHANT
                    else SHOP_TYPE_REGULER
            it.isGoldMerchant = shopGroupAvailable.shop.goldMerchant.isGoldBadge
            it.isOfficialStore = shopGroupAvailable.shop.isOfficial == 1
            it.shopBadge =
                    if (shopGroupAvailable.shop.isOfficial == 1) shopGroupAvailable.shop.officialStore.osLogoUrl
                    else if (shopGroupAvailable.shop.goldMerchant.isGoldBadge) shopGroupAvailable.shop.goldMerchant.goldMerchantLogoUrl
                    else ""
            it.isFulfillment = shopGroupAvailable.isFulFillment
            it.fulfillmentName = shopGroupAvailable.warehouse.cityName
            it.isHasPromoList = shopGroupAvailable.hasPromoList
            it.cartString = shopGroupAvailable.cartString

            if (cartDataListResponse.autoapplyStack.voucherOrders.isNotEmpty()) {
                for (voucherOrdersItem in cartDataListResponse.autoapplyStack.voucherOrders) {
                    if (voucherOrdersItem.uniqueId == it.cartString && voucherOrdersItem.type.isNotEmpty()
                            && voucherOrdersItem.type.equals(MERCHANT_VOUCHER_TYPE, ignoreCase = true)) {
                        it.voucherOrdersItemData = mapVoucherOrdersItemData(voucherOrdersItem)
                        break
                    }
                }
            }

            it.cartItemHolderDataList = mapCartItemHolderDataList(shopGroupAvailable.cartDetails, shopGroupAvailable, it, cartDataListResponse, false)

            it
        }
    }

    private fun mapShopError(it: ShopGroupWithErrorData, shopGroupWithError: ShopGroupWithError, isDisableAllProducts: Boolean): Boolean {
        var isDisableAllProducts1 = isDisableAllProducts
        if (!it.isError) {
            var errorItemCountPerShop = 0
            for (cartDetail in shopGroupWithError.cartDetails) {
                if (cartDetail.errors != null && cartDetail.errors.size > 0) {
                    errorItemCountPerShop++
                }
            }

            var shopError = false
            if (errorItemCountPerShop == shopGroupWithError.cartDetails.size) {
                shopError = true
                isDisableAllProducts1 = true
            } else {
                isDisableAllProducts1 = false
            }
            it.isError = shopError
        }
        return isDisableAllProducts1
    }

    private fun mapVoucherOrdersItemData(voucherOrdersItem: VoucherOrdersItem): VoucherOrdersItemData {
        return VoucherOrdersItemData().let {
            it.code = voucherOrdersItem.code
            it.isSuccess = voucherOrdersItem.isSuccess
            it.uniqueId = voucherOrdersItem.uniqueId
            it.cartId = voucherOrdersItem.cartId
            it.shopId = voucherOrdersItem.shopId
            it.isPO = voucherOrdersItem.isPo
            it.addressId = voucherOrdersItem.addressId
            it.type = voucherOrdersItem.type
            it.cashbackWalletAmount = voucherOrdersItem.cashbackWalletAmount
            it.discountAmount = voucherOrdersItem.discountAmount
            it.invoiceDescription = voucherOrdersItem.invoiceDescription
            it.variant = voucherOrdersItem.type
            it.titleDescription = voucherOrdersItem.titleDescription
            it.messageData = mapMessageData(voucherOrdersItem.message)
            it.isAutoapply = true
            it
        }
    }

    private fun mapMessageData(message: Message): MessageData {
        return MessageData().let {
            it.state = message.state
            it.color = message.color
            it.text = message.text
            it
        }
    }

    private fun mapCartItemHolderDataList(cartDetails: List<CartDetail>?,
                                          shopGroup: Any,
                                          shopGroupData: Any,
                                          cartDataListResponse: CartDataListResponse,
                                          isDisabledAllProduct: Boolean): List<CartItemHolderData> {
        val cartItemHolderDataList = arrayListOf<CartItemHolderData>()
        cartDetails?.forEach {
            cartItemHolderDataList.add(
                    CartItemHolderData().apply {
                        cartItemData = mapCartItemData(it, shopGroup, shopGroupData, cartDataListResponse, isDisabledAllProduct)
                        isEditableRemark = false
                        errorFormItemValidationMessage = ""
                        isEditableRemark = false
                        isSelected = if (cartItemData.isError) {
                            false
                        } else {
                            cartItemData.originData.isCheckboxState
                        }
                    }
            )
        }

        return cartItemHolderDataList
    }

    private fun mapCartItemData(cartDetail: CartDetail,
                                shopGroup: Any,
                                shopGroupData: Any,
                                cartDataListResponse: CartDataListResponse,
                                isDisabledAllProduct: Boolean): CartItemData {
        return CartItemData().let {
            it.originData = mapOriginData(cartDetail, shopGroup)
            it.updatedData = mapUpdatedData(cartDetail, cartDataListResponse)
            it.messageErrorData = mapMessageErrorData(cartDataListResponse.messages)
            mapCartItemDataWarning(cartDetail, it)
            mapCartItemDataError(cartDetail, it)

            when (shopGroup) {
                is ShopGroupAvailable -> {
                    it.isFulfillment = shopGroup.isFulFillment
                    it.isSingleChild = shopGroup.cartDetails.size == 1
                }
                is ShopGroupWithError -> {
                    it.isFulfillment = shopGroup.isFulFillment
                    it.isSingleChild = shopGroup.cartDetails.size == 1
                }
            }

            it.isDisableAllProducts = isDisabledAllProduct
            when (shopGroupData) {
                is ShopGroupAvailableData -> {
                    it.isParentHasErrorOrWarning = !(!shopGroupData.isError && !shopGroupData.isWarning)
                }
                is ShopGroupWithErrorData -> {
                    it.isParentHasErrorOrWarning = !(!shopGroupData.isError && !shopGroupData.isWarning)
                }
            }

            it
        }
    }

    private fun mapCartItemDataError(cartDetail: CartDetail, it: CartItemData) {
        if (cartDetail.errors != null && cartDetail.errors.size > 0) {
            it.isError = true
            it.errorMessageTitle = cartDetail.errors[0]
            val similarProduct = cartDetail.similarProduct
            if (similarProduct != null && !TextUtils.isEmpty(similarProduct.text) && !TextUtils.isEmpty(similarProduct.url)) {
                it.similarProductData = SimilarProductData(similarProduct.text, similarProduct.url)
            }
            val nicotineLiteMessage = cartDetail.nicotineLiteMessage
            if (nicotineLiteMessage != null && !TextUtils.isEmpty(nicotineLiteMessage.text) && !TextUtils.isEmpty(nicotineLiteMessage.url)) {
                it.nicotineLiteMessageData = NicotineLiteMessageData(nicotineLiteMessage.text, nicotineLiteMessage.url)
            }

            if (cartDetail.errors.size > 1) {
                it.errorMessageDescription = cartDetail.errors.subList(1, cartDetail.errors.size - 1).joinToString()
            }
        }
    }

    private fun mapCartItemDataWarning(cartDetail: CartDetail, it: CartItemData) {
        if (cartDetail.messages != null && cartDetail.messages.size > 0) {
            it.isWarning = true
            it.warningMessageTitle = cartDetail.messages[0]

            if (cartDetail.messages.size > 1) {
                it.warningMessageDescription = cartDetail.messages.subList(1, cartDetail.messages.size - 1).joinToString()
            }
        }
    }

    private fun mapOriginData(cartDetail: CartDetail, shopData: Any): CartItemData.OriginData {
        return CartItemData.OriginData().let {
            it.cartId = cartDetail.cartId
            it.parentId = cartDetail.product.parentId.toString()
            it.productId = cartDetail.product.productId.toString()
            it.productName = cartDetail.product.productName
            it.minOrder = cartDetail.product.productMinOrder
            it.maxOrder = if (cartDetail.product.productSwitchInvenage == 0) {
                cartDetail.product.productMaxOrder
            } else {
                min(cartDetail.product.productMaxOrder, cartDetail.product.productInvenageValue)
            }
            it.priceChangesState = cartDetail.product.priceChanges.changesState
            it.priceChangesDesc = cartDetail.product.priceChanges.description
            it.productInvenageByUserInCart = cartDetail.product.productInvenageTotal.byUser.inCart
            it.productInvenageByUserLastStockLessThan = cartDetail.product.productInvenageTotal.byUser.lastStockLessThan
            it.productInvenageByUserText = cartDetail.product.productInvenageTotal.byUserText.complete
            it.pricePlan = cartDetail.product.productPrice.toDouble()
            it.pricePlanInt = cartDetail.product.productPrice
            it.priceCurrency = cartDetail.product.productPriceCurrency
            it.priceFormatted = cartDetail.product.productPriceFmt
            it.productImage = cartDetail.product.productImage.imageSrc200Square
            it.productVarianRemark = cartDetail.product.productNotes
            it.weightPlan = cartDetail.product.productWeight.toDouble()
            it.weightUnit = cartDetail.product.productWeightUnitCode
            it.weightFormatted = cartDetail.product.productWeightFmt
            it.isPreOrder = cartDetail.product.isPreorder == 1
            it.isCod = cartDetail.product.isCod
            it.isFreeReturn = cartDetail.product.isFreereturns == 1
            it.isCashBack = !cartDetail.product.productCashback.isNullOrEmpty()
            it.isFavorite = false
            it.productCashBack = cartDetail.product.productCashback
            it.cashBackInfo = "Cashback ${cartDetail.product.productCashback}"
            it.freeReturnLogo =
                    if (cartDetail.product.freeReturns != null) cartDetail.product.freeReturns.freeReturnsLogo
                    else ""
            it.category = cartDetail.product.category
            it.categoryId = cartDetail.product.categoryId.toString()
            it.wholesalePriceData = mapWholeSalePriceDataList(cartDetail.product.wholesalePrice)
            it.trackerAttribution = cartDetail.product.productTrackerData.attribution
            it.trackerListName = cartDetail.product.productTrackerData.trackerListName
            it.originalRemark = it.productVarianRemark
            it.isWishlisted = cartDetail.product.isWishlisted
            it.originalQty = cartDetail.product.productQuantity
            it.preOrderInfo =
                    if (cartDetail.product.productPreorder != null && cartDetail.product.productPreorder.durationText != null)
                        "PO ${cartDetail.product.productPreorder.durationText}"
                    else ""
            it.isCheckboxState = cartDetail.isCheckboxState
            it.priceOriginal = cartDetail.product.productOriginalPrice
            if (cartDetail.product.freeShipping != null && cartDetail.product.freeShipping.eligible &&
                    !TextUtils.isEmpty(cartDetail.product.freeShipping.badgeUrl)) {
                it.isFreeShipping = true
                it.freeShippingBadgeUrl = cartDetail.product.freeShipping.badgeUrl
            }

            when (shopData) {
                is ShopGroupAvailable -> mapShopInfoCartItemData(it, shopData)
                is ShopGroupWithError -> mapShopInfoCartItemData(it, shopData)
            }

            it
        }
    }

    private fun mapShopInfoCartItemData(cartItemData: CartItemData.OriginData, shopGroupAvailable: ShopGroupAvailable) {
        cartItemData.let {
            it.shopName = shopGroupAvailable.shop.shopName
            it.shopCity = shopGroupAvailable.shop.cityName
            it.shopId = shopGroupAvailable.shop.shopId.toString()
            it.shopType =
                    if (shopGroupAvailable.shop.isOfficial == 1) SHOP_TYPE_OFFICIAL_STORE
                    else if (shopGroupAvailable.shop.goldMerchant.isGoldBadge) SHOP_TYPE_GOLD_MERCHANT
                    else SHOP_TYPE_REGULER
            it.isOfficialStore = shopGroupAvailable.shop.isOfficial == 1
            it.isGoldMerchant = shopGroupAvailable.shop.goldMerchant.isGoldBadge
            it.cartString = shopGroupAvailable.cartString
            it.warehouseId = shopGroupAvailable.warehouse.warehouseId
        }
    }

    private fun mapShopInfoCartItemData(cartItemData: CartItemData.OriginData, shopGroupWithError: ShopGroupWithError) {
        cartItemData.let {
            it.shopName = shopGroupWithError.shop.shopName
            it.shopCity = shopGroupWithError.shop.cityName
            it.shopId = shopGroupWithError.shop.shopId.toString()
            it.shopType =
                    if (shopGroupWithError.shop.isOfficial == 1) SHOP_TYPE_OFFICIAL_STORE
                    else if (shopGroupWithError.shop.goldMerchant.isGoldBadge) SHOP_TYPE_GOLD_MERCHANT
                    else SHOP_TYPE_REGULER
            it.isOfficialStore = shopGroupWithError.shop.isOfficial == 1
            it.isGoldMerchant = shopGroupWithError.shop.goldMerchant.isGoldBadge
            it.cartString = shopGroupWithError.cartString
            it.warehouseId = shopGroupWithError.warehouse.warehouseId
        }
    }

    private fun mapWholeSalePriceDataList(wholesalePriceList: List<WholesalePrice>?): List<WholesalePriceData> {
        val wholesalePriceDataList = arrayListOf<WholesalePriceData>()
        wholesalePriceList?.forEach {
            val wholesalePriceData = mapWholeSalePriceData(it)
            wholesalePriceDataList.add(wholesalePriceData)
        }
        return wholesalePriceDataList
    }

    private fun mapWholeSalePriceData(wholesalePrice: WholesalePrice): WholesalePriceData {
        return WholesalePriceData().let {
            it.qtyMinFmt = wholesalePrice.qtyMinFmt
            it.qtyMaxFmt = wholesalePrice.qtyMaxFmt
            it.prdPrcFmt = wholesalePrice.prdPrcFmt
            it.qtyMin = wholesalePrice.qtyMin
            it.qtyMax = wholesalePrice.qtyMax
            it.prdPrc = wholesalePrice.prdPrc
            it
        }
    }

    private fun mapPromoAnalytics(autoApplyStack: AutoapplyStack,
                                  shopGroupAvailableDataList: List<ShopGroupAvailableData>) {
        if (autoApplyStack.trackingDetails != null && autoApplyStack.trackingDetails.size > 0) {
            for (trackingDetail in autoApplyStack.trackingDetails) {
                for (shopGroupAvailableData in shopGroupAvailableDataList) {
                    for (cartItemHolderData in shopGroupAvailableData.cartItemDataList) {
                        val originData = cartItemHolderData.cartItemData.originData
                        if (originData.productId.equals(trackingDetail.productId.toString(), ignoreCase = true)) {
                            originData.promoCodes = trackingDetail.promoCodesTracking
                            originData.promoDetails = trackingDetail.promoDetailsTracking
                        }
                    }
                }
            }
        }
    }

    private fun mapUpdatedData(cartDetail: CartDetail, cartDataListResponse: CartDataListResponse): CartItemData.UpdatedData {
        return CartItemData.UpdatedData().let {
            it.quantity = cartDetail.product.productQuantity
            it.remark = cartDetail.product.productNotes
            it.maxCharRemark = cartDataListResponse.maxCharNote
            it
        }
    }

    private fun mapMessageErrorData(messages: Messages): CartItemData.MessageErrorData {
        return CartItemData.MessageErrorData().let {
            it.errorCheckoutPriceLimit = messages.errorCheckoutPriceLimit
            it.errorFieldBetween = messages.errorFieldBetween
            it.errorFieldMaxChar = messages.errorFieldMaxChar
            it.errorFieldRequired = messages.errorFieldRequired
            it.errorProductAvailableStock = messages.errorProductAvailableStock
            it.errorProductAvailableStockDetail = messages.errorProductAvailableStockDetail
            it.errorProductMaxQuantity = messages.errorProductMaxQuantity
            it.errorProductMinQuantity = messages.errorProductMinQuantity
            it
        }
    }

    private fun mapShopGroupWithErrorDataList(cartDataListResponse: CartDataListResponse): List<ShopGroupWithErrorData> {
        val shopGroupWithErrorDataList = arrayListOf<ShopGroupWithErrorData>()
        cartDataListResponse.shopGroupWithErrors?.forEach {
            val shopGroupWithErrorData = mapShopGroupWithErrorData(it, cartDataListResponse)
            shopGroupWithErrorDataList.add(shopGroupWithErrorData)
        }

        return shopGroupWithErrorDataList
    }

    private fun mapShopGroupWithErrorData(shopGroupWithError: ShopGroupWithError,
                                          cartDataListResponse: CartDataListResponse): ShopGroupWithErrorData {
        return ShopGroupWithErrorData().let {
            it.isError = !shopGroupWithError.errors.isNullOrEmpty()
            it.errorLabel = shopGroupWithError.errors.firstOrNull() ?: ""
            it.shopName = shopGroupWithError.shop.shopName
            it.shopId = shopGroupWithError.shop.shopId.toString()
            it.shopType =
                    when {
                        shopGroupWithError.shop.isOfficial == 1 -> SHOP_TYPE_OFFICIAL_STORE
                        shopGroupWithError.shop.goldMerchant.isGoldBadge -> SHOP_TYPE_GOLD_MERCHANT
                        else -> SHOP_TYPE_REGULER
                    }
            it.cityName = shopGroupWithError.shop.cityName
            it.isGoldMerchant = shopGroupWithError.shop.goldMerchant.isGoldBadge
            it.isOfficialStore = shopGroupWithError.shop.isOfficial == 1
            it.shopBadge =
                    when {
                        shopGroupWithError.shop.isOfficial == 1 -> shopGroupWithError.shop.officialStore.osLogoUrl
                        shopGroupWithError.shop.goldMerchant.isGoldBadge -> shopGroupWithError.shop.goldMerchant.goldMerchantLogoUrl
                        else -> ""
                    }
            it.isFulfillment = shopGroupWithError.isFulFillment
            it.fulfillmentName = shopGroupWithError.warehouse.cityName
            it.cartString = shopGroupWithError.cartString

            var isDisableAllProducts = true
            isDisableAllProducts = mapShopError(it, shopGroupWithError, isDisableAllProducts)
            it.cartItemHolderDataList = mapCartItemHolderDataList(shopGroupWithError.cartDetails, shopGroupWithError, it, cartDataListResponse, isDisableAllProducts)

            it
        }
    }

    private fun mapCartTickerErrorData(errorItemCount: Int): CartTickerErrorData {
        return CartTickerErrorData.Builder()
                .errorCount(errorItemCount)
                .errorInfo(String.format(context.getString(R.string.cart_error_message), errorItemCount))
                .actionInfo(context.getString(R.string.cart_error_action))
                .build()
    }

    private fun mapAutoApplyStackData(autoApplyStack: AutoapplyStack): AutoApplyStackData {
        return AutoApplyStackData().let {
            it.promoCodeId = autoApplyStack.promoCodeId
            it.voucherOrdersItemDataList = mapVoucherOrdersItemDataList(autoApplyStack.voucherOrders)
            it.discountAmount = autoApplyStack.discountAmount
            it.titleDescription = autoApplyStack.titleDescription
            it.code = if (autoApplyStack.codes.isNotEmpty()) autoApplyStack.codes[0] else ""
            it.isCoupon = autoApplyStack.isCoupon
            it.isSuccess = autoApplyStack.isSuccess
            if (autoApplyStack.message != null) {
                it.messageSuccess = autoApplyStack.message.text
                it.state = autoApplyStack.message.state
            }
            it
        }
    }

    private fun mapVoucherOrdersItemDataList(voucherOrdersItemList: List<VoucherOrdersItem>?): List<VoucherOrdersItemData> {
        val voucherOrderItemsDataList = arrayListOf<VoucherOrdersItemData>()
        voucherOrdersItemList?.forEach {
            val voucherOrderItemData = mapVoucherOrdersItemData(it)
            voucherOrderItemsDataList.add(voucherOrderItemData)
        }

        return voucherOrderItemsDataList
    }

    private fun mapGlobalCouponAttr(globalCouponAttr: GlobalCouponAttr): GlobalCouponAttrData {
        return GlobalCouponAttrData().let {
            it.description = globalCouponAttr.description
            it.quantityLabel = globalCouponAttr.quantityLabel
            it
        }
    }

}