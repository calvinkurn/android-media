package com.tokopedia.cart.domain.mapper

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.cart.R
import com.tokopedia.cart.data.model.response.promo.*
import com.tokopedia.cart.data.model.response.shopgroupsimplified.*
import com.tokopedia.cart.domain.model.cartlist.*
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.purchase_platform.common.constant.CartConstant.STATE_RED
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.*
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.min

/**
 * Created by Irfan Khoirul on 2019-10-17.
 */

class CartSimplifiedMapper @Inject constructor(@ApplicationContext val context: Context) {

    companion object {
        const val SHOP_TYPE_OFFICIAL_STORE = "official_store"
        const val SHOP_TYPE_GOLD_MERCHANT = "gold_merchant"
        const val SHOP_TYPE_REGULER = "reguler"
    }

    fun convertToCartItemDataList(cartDataListResponse: CartDataListResponse): CartListData {
        val cartListData = CartListData()

        if (cartDataListResponse.tickers.isNotEmpty()) {
            cartListData.tickerData = mapTickerData(cartDataListResponse.tickers)
        }
        cartListData.shopGroupAvailableDataList = mapShopGroupAvailableDataList(cartDataListResponse)
        cartListData.unavailableGroupData = mapUnavailableGroupData(cartDataListResponse)
        cartListData.isPromoCouponActive = cartDataListResponse.isCouponActive == 1

        var errorCount = 0
        cartDataListResponse.unavailableSections.forEach {
            for (shopGroupWithError in it.unavailableGroups) {
                errorCount += shopGroupWithError.totalCartDetailsError
            }
        }
        cartListData.isError = errorCount > 0
        if (cartListData.isError) {
            cartListData.cartTickerErrorData = mapCartTickerErrorData(errorCount)
        }

        cartListData.lastApplyShopGroupSimplifiedData = mapLastApplySimplified(cartDataListResponse.promo.lastApplyPromo.lastApplyPromoData)
        cartListData.errorDefault = mapPromoCheckoutErrorDefault(cartDataListResponse.promo.errorDefault)
        cartListData.isAllSelected = cartDataListResponse.isGlobalCheckboxState
        cartListData.isShowOnboarding = false

        mapPromoAnalytics(cartDataListResponse.promo.lastApplyPromo.lastApplyPromoData, cartListData.shopGroupAvailableDataList)

        return cartListData
    }

    private fun mapTickerData(tickers: List<Ticker>): TickerData {
        val ticker = tickers[0]
        return TickerData(ticker.id, ticker.message, ticker.page)
    }

    private fun mapShopGroupAvailableDataList(cartDataListResponse: CartDataListResponse): List<ShopGroupAvailableData> {
        val shopGroupAvailableDataList = arrayListOf<ShopGroupAvailableData>()
        cartDataListResponse.availableSection.availableGroupGroups.forEach {
            val shopGroupAvailableData = mapShopGroupAvailableData(it, cartDataListResponse)
            shopGroupAvailableDataList.add(shopGroupAvailableData)
        }

        return shopGroupAvailableDataList
    }

    private fun mapShopGroupAvailableData(availableGroup: AvailableGroup,
                                          cartDataListResponse: CartDataListResponse): ShopGroupAvailableData {
        return ShopGroupAvailableData().let {
            it.isChecked = availableGroup.checkboxState
            it.isError = false
            it.errorTitle = ""
            it.shopName = availableGroup.shop.shopName
            it.shopId = availableGroup.shop.shopId.toString()
            it.shopType =
                    if (availableGroup.shop.isOfficial == 1) SHOP_TYPE_OFFICIAL_STORE
                    else if (availableGroup.shop.goldMerchant.isGoldBadge) SHOP_TYPE_GOLD_MERCHANT
                    else SHOP_TYPE_REGULER
            it.isGoldMerchant = availableGroup.shop.goldMerchant.isGoldBadge
            it.isOfficialStore = availableGroup.shop.isOfficial == 1
            it.shopBadge =
                    if (availableGroup.shop.isOfficial == 1) availableGroup.shop.officialStore.osLogoUrl
                    else if (availableGroup.shop.goldMerchant.isGoldBadge) availableGroup.shop.goldMerchant.goldMerchantLogoUrl
                    else ""
            it.isFulfillment = availableGroup.isFulFillment
            it.fulfillmentName = availableGroup.shipmentInformation.shopLocation
            it.isHasPromoList = availableGroup.hasPromoList
            it.cartString = availableGroup.cartString
            it.promoCodes = availableGroup.promoCodes
            it.cartItemHolderDataList = mapCartItemHolderDataList(availableGroup.cartDetails, availableGroup, it, cartDataListResponse, false)

            it.preOrderInfo = if (availableGroup.shipmentInformation.preorder.isPreorder) availableGroup.shipmentInformation.preorder.duration else ""
            it.freeShippingBadgeUrl = if (availableGroup.shipmentInformation.freeShipping.eligible) availableGroup.shipmentInformation.freeShipping.badgeUrl else ""
            it.incidentInfo = availableGroup.shop.shopAlertMessage
            it
        }
    }

    private fun mapShopError(it: ShopGroupWithErrorData, unavailableGroup: UnavailableGroup, isDisableAllProducts: Boolean): Boolean {
        var isDisableAllProducts1 = isDisableAllProducts
        if (!it.isError) {
            var errorItemCountPerShop = 0
            for (cartDetail in unavailableGroup.cartDetails) {
                if (cartDetail.errors.isNotEmpty()) {
                    errorItemCountPerShop++
                }
            }

            var shopError = false
            if (errorItemCountPerShop == unavailableGroup.cartDetails.size) {
                shopError = true
                isDisableAllProducts1 = true
            } else {
                isDisableAllProducts1 = false
            }
            it.isError = shopError
        }
        return isDisableAllProducts1
    }

    private fun mapCartItemHolderDataList(cartDetails: List<CartDetail>?,
                                          shopGroup: Any,
                                          shopGroupData: Any,
                                          cartDataListResponse: CartDataListResponse,
                                          isDisabledAllProduct: Boolean): MutableList<CartItemHolderData> {
        val cartItemHolderDataList = arrayListOf<CartItemHolderData>()
        cartDetails?.forEach {
            val cartItemData = mapCartItemData(it, shopGroup, shopGroupData, cartDataListResponse, isDisabledAllProduct)
            val cartItemHolderData = CartItemHolderData(
                    cartItemData = cartItemData,
                    errorFormItemValidationType = 0,
                    errorFormItemValidationMessage = "",
                    isEditableRemark = false,
                    isStateHasNotes = false,
                    isSelected = if (cartItemData.isError) {
                        false
                    } else {
                        cartItemData.originData?.isCheckboxState ?: true
                    }
            )
            validateQty(cartItemHolderData)

            cartItemHolderDataList.add(cartItemHolderData)
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
                is AvailableGroup -> {
                    it.isFulfillment = shopGroup.isFulFillment
                    it.isSingleChild = shopGroup.cartDetails.size == 1
                }
                is UnavailableGroup -> {
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

    private fun validateQty(cartItemHolderData: CartItemHolderData) {
        var errorFormItemValidationMessage = ""
        when {
            cartItemHolderData.cartItemData?.updatedData?.remark?.length ?: 0 > cartItemHolderData.cartItemData?.updatedData?.maxCharRemark ?: 0 -> {
                errorFormItemValidationMessage = cartItemHolderData.cartItemData?.messageErrorData?.errorFieldMaxChar
                        ?.replace("{{value}}", cartItemHolderData.cartItemData?.updatedData?.maxCharRemark.toString()) ?: ""
            }
            cartItemHolderData.cartItemData?.updatedData?.quantity ?: 0 > cartItemHolderData.cartItemData?.originData?.maxOrder ?: 0 -> {
                val formattedMaxCharRemark = String.format(Locale.US, "%,d", cartItemHolderData.cartItemData?.originData?.maxOrder).replace(',', '.')
                errorFormItemValidationMessage = cartItemHolderData.cartItemData?.messageErrorData?.errorProductMaxQuantity
                        ?.replace("{{value}}", formattedMaxCharRemark) ?: ""
            }
            cartItemHolderData.cartItemData?.updatedData?.quantity ?: 0 < cartItemHolderData.cartItemData?.originData?.minOrder ?: 0 -> {
                errorFormItemValidationMessage = cartItemHolderData.cartItemData?.messageErrorData?.errorProductMinQuantity
                        ?.replace("{{value}}", cartItemHolderData.cartItemData?.originData?.minOrder.toString()) ?: ""
            }
        }

        cartItemHolderData.errorFormItemValidationMessage = errorFormItemValidationMessage
    }

    private fun mapCartItemDataError(cartDetail: CartDetail, it: CartItemData) {
        if (cartDetail.errors.isNotEmpty()) {
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
        if (cartDetail.messages.isNotEmpty()) {
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
            it.isCashBack = !cartDetail.product.productCashback.isEmpty()
            it.isFavorite = false
            it.productCashBack = cartDetail.product.productCashback
            it.cashBackInfo = "Cashback ${cartDetail.product.productCashback}"
            it.freeReturnLogo = cartDetail.product.freeReturns.freeReturnsLogo
            it.category = cartDetail.product.category
            it.categoryId = cartDetail.product.categoryId.toString()
            it.wholesalePriceData = mapWholeSalePriceDataList(cartDetail.product.wholesalePrice)
            it.trackerAttribution = cartDetail.product.productTrackerData.attribution
            it.trackerListName = cartDetail.product.productTrackerData.trackerListName
            it.originalRemark = it.productVarianRemark
            it.isWishlisted = cartDetail.product.isWishlisted
            it.originalQty = cartDetail.product.productQuantity
            it.preOrderInfo =
                    if (cartDetail.product.productPreorder.durationText.isNotBlank())
                        "PO ${cartDetail.product.productPreorder.durationText}"
                    else ""
            it.isCheckboxState = cartDetail.isCheckboxState
            it.priceOriginal = cartDetail.product.productOriginalPrice
            if (cartDetail.product.freeShipping.eligible && cartDetail.product.freeShipping.badgeUrl.isNotBlank()) {
                it.isFreeShipping = true
                it.freeShippingBadgeUrl = cartDetail.product.freeShipping.badgeUrl
            }
            it.variant = cartDetail.product.variantDescriptionDetail.variantNames.joinToString(", ")
            it.warningMessage = cartDetail.product.productWarningMessage
            it.slashPriceLabel = if (cartDetail.product.isSlashPrice) cartDetail.product.slashPriceLabel else ""
            it.initialPriceBeforeDrop = cartDetail.product.initialPrice
            it.productAlertMessage = cartDetail.product.productAlertMessage

            when (shopData) {
                is AvailableGroup -> mapShopInfoCartItemData(it, shopData)
                is UnavailableGroup -> mapShopInfoCartItemData(it, shopData)
            }

            it
        }
    }

    private fun mapShopInfoCartItemData(cartItemData: CartItemData.OriginData, availableGroup: AvailableGroup) {
        val listPromoCheckout = arrayListOf<String>()
        availableGroup.promoCodes.forEach {
            listPromoCheckout.add(it)
        }

        cartItemData.let {
            it.shopName = availableGroup.shop.shopName
            it.shopCity = availableGroup.shop.cityName
            it.shopId = availableGroup.shop.shopId.toString()
            it.shopType =
                    if (availableGroup.shop.isOfficial == 1) SHOP_TYPE_OFFICIAL_STORE
                    else if (availableGroup.shop.goldMerchant.isGoldBadge) SHOP_TYPE_GOLD_MERCHANT
                    else SHOP_TYPE_REGULER
            it.isOfficialStore = availableGroup.shop.isOfficial == 1
            it.isGoldMerchant = availableGroup.shop.goldMerchant.isGoldBadge
            it.cartString = availableGroup.cartString
            it.warehouseId = availableGroup.warehouse.warehouseId
            it.listPromoCheckout = listPromoCheckout
        }
    }

    private fun mapShopInfoCartItemData(cartItemData: CartItemData.OriginData, unavailableGroup: UnavailableGroup) {
        cartItemData.let {
            it.shopName = unavailableGroup.shop.shopName
            it.shopCity = unavailableGroup.shop.cityName
            it.shopId = unavailableGroup.shop.shopId.toString()
            it.shopType =
                    if (unavailableGroup.shop.isOfficial == 1) SHOP_TYPE_OFFICIAL_STORE
                    else if (unavailableGroup.shop.goldMerchant.isGoldBadge) SHOP_TYPE_GOLD_MERCHANT
                    else SHOP_TYPE_REGULER
            it.isOfficialStore = unavailableGroup.shop.isOfficial == 1
            it.isGoldMerchant = unavailableGroup.shop.goldMerchant.isGoldBadge
            it.cartString = unavailableGroup.cartString
            it.warehouseId = unavailableGroup.warehouse.warehouseId
        }
    }

    private fun mapWholeSalePriceDataList(wholesalePriceList: List<WholesalePrice>?): List<WholesalePriceData> {
        val wholesalePriceDataList = arrayListOf<WholesalePriceData>()
        wholesalePriceList?.forEach {
            val wholesalePriceData = mapWholeSalePriceData(it)
            wholesalePriceDataList.add(wholesalePriceData)
        }
        return wholesalePriceDataList.asReversed()
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

    private fun mapPromoAnalytics(lastApplyPromoData: LastApplyPromoData,
                                  shopGroupAvailableDataList: List<ShopGroupAvailableData>) {
        if (lastApplyPromoData.trackingDetails.isNotEmpty()) {
            for (trackingDetail in lastApplyPromoData.trackingDetails) {
                for (shopGroupAvailableData in shopGroupAvailableDataList) {
                    val cartItemDataList = shopGroupAvailableData.cartItemDataList
                    cartItemDataList?.let {
                        for (cartItemHolderData in cartItemDataList) {
                            val originData = cartItemHolderData.cartItemData?.originData
                            if (originData?.productId.equals(trackingDetail.productId.toString(), ignoreCase = true)) {
                                originData?.promoCodes = trackingDetail.promoCodesTracking
                                originData?.promoDetails = trackingDetail.promoDetailsTracking
                            }
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

    private fun mapUnavailableGroupData(cartDataListResponse: CartDataListResponse): List<UnavailableGroupData> {
        val unavailableGroupDatas = ArrayList<UnavailableGroupData>()
        cartDataListResponse.unavailableSections.forEach {
            unavailableGroupDatas.add(
                    UnavailableGroupData().apply {
                        title = it.title
                        description = it.unavailableDescription
                        action = mapActionUnavailableGroupData(it.actions)
                        shopGroupWithErrorDataList = mapShopGroupWithErrorDataList(it.unavailableGroups, cartDataListResponse)
                    }
            )
        }
        return unavailableGroupDatas
    }

    private fun mapActionUnavailableGroupData(actions: List<Action>): List<ActionData> {
        val actionDatas = ArrayList<ActionData>()
        actions.forEach {
            actionDatas.add(
                    ActionData().apply {
                        id = it.id
                        code = it.code
                        message = it.message
                    }
            )
        }
        return actionDatas
    }

    private fun mapShopGroupWithErrorDataList(unavailableGroups: List<UnavailableGroup>,
                                              cartDataListResponse: CartDataListResponse): List<ShopGroupWithErrorData> {
        val shopGroupWithErrorDataList = arrayListOf<ShopGroupWithErrorData>()
        unavailableGroups.forEach {
            val shopGroupWithErrorData = mapShopGroupWithErrorData(it, cartDataListResponse)
            shopGroupWithErrorDataList.add(shopGroupWithErrorData)
        }

        return shopGroupWithErrorDataList
    }

    private fun mapShopGroupWithErrorData(unavailableGroup: UnavailableGroup,
                                          cartDataListResponse: CartDataListResponse): ShopGroupWithErrorData {
        return ShopGroupWithErrorData().let {
            it.isError = !unavailableGroup.errors.isNullOrEmpty()
            it.errorLabel = unavailableGroup.errors.firstOrNull() ?: ""
            it.shopName = unavailableGroup.shop.shopName
            it.shopId = unavailableGroup.shop.shopId.toString()
            it.shopType =
                    when {
                        unavailableGroup.shop.isOfficial == 1 -> SHOP_TYPE_OFFICIAL_STORE
                        unavailableGroup.shop.goldMerchant.isGoldBadge -> SHOP_TYPE_GOLD_MERCHANT
                        else -> SHOP_TYPE_REGULER
                    }
            it.cityName = unavailableGroup.shop.cityName
            it.isGoldMerchant = unavailableGroup.shop.goldMerchant.isGoldBadge
            it.isOfficialStore = unavailableGroup.shop.isOfficial == 1
            it.shopBadge =
                    when {
                        unavailableGroup.shop.isOfficial == 1 -> unavailableGroup.shop.officialStore.osLogoUrl
                        unavailableGroup.shop.goldMerchant.isGoldBadge -> unavailableGroup.shop.goldMerchant.goldMerchantLogoUrl
                        else -> ""
                    }
            it.isFulfillment = unavailableGroup.isFulFillment
            it.fulfillmentName = unavailableGroup.shipmentInformation.shopLocation
            it.cartString = unavailableGroup.cartString

            var isDisableAllProducts = true
            isDisableAllProducts = mapShopError(it, unavailableGroup, isDisableAllProducts)
            it.cartItemHolderDataList = mapCartItemHolderDataList(unavailableGroup.cartDetails, unavailableGroup, it, cartDataListResponse, isDisableAllProducts)

            it
        }
    }

    private fun mapCartTickerErrorData(errorItemCount: Int): CartTickerErrorData {
        return CartTickerErrorData().let {
            it.errorCount = errorItemCount
            it.errorInfo = String.format(context.getString(R.string.cart_error_message), errorItemCount)
            it.actionInfo = context.getString(R.string.cart_error_action)
            it
        }
    }

    private fun mapLastApplySimplified(lastApplyPromoData: LastApplyPromoData): LastApplyUiModel {
        return LastApplyUiModel(
                codes = lastApplyPromoData.codes,
                voucherOrders = mapListVoucherOrders(lastApplyPromoData.listVoucherOrders),
                additionalInfo = mapAdditionalInfo(lastApplyPromoData.additionalInfo),
                message = mapMessageGlobalPromo(lastApplyPromoData.message),
                listRedPromos = mapCreateListRedPromos(lastApplyPromoData)
        )
    }

    private fun mapListVoucherOrders(listVoucherOrdersItem: List<VoucherOrders>): List<LastApplyVoucherOrdersItemUiModel> {
        val listVoucherOrders: ArrayList<LastApplyVoucherOrdersItemUiModel> = arrayListOf()
        listVoucherOrdersItem.forEach {
            listVoucherOrders.add(mapVoucherOrders(it))
        }
        return listVoucherOrders
    }

    private fun mapVoucherOrders(voucherOrders: VoucherOrders): LastApplyVoucherOrdersItemUiModel {
        return LastApplyVoucherOrdersItemUiModel(
                code = voucherOrders.code,
                uniqueId = voucherOrders.uniqueId,
                message = mapMessage(voucherOrders.message)
        )
    }

    private fun mapMessage(message: MessageVoucherOrders): LastApplyMessageUiModel {
        return LastApplyMessageUiModel(
                color = message.color,
                state = message.state,
                text = message.text)
    }

    private fun mapMessageGlobalPromo(message: MessageGlobalPromo): LastApplyMessageUiModel {
        return LastApplyMessageUiModel(
                color = message.color,
                state = message.state,
                text = message.text)
    }

    private fun mapAdditionalInfo(promoAdditionalInfo: PromoAdditionalInfo): LastApplyAdditionalInfoUiModel {
        return LastApplyAdditionalInfoUiModel(
                messageInfo = mapMessageInfo(promoAdditionalInfo.messageInfo),
                errorDetail = mapErrorDetail(promoAdditionalInfo.errorDetail),
                emptyCartInfo = mapEmptyCartInfo(promoAdditionalInfo.emptyCartInfo)
        )
    }

    private fun mapMessageInfo(promoMessageInfo: PromoMessageInfo): LastApplyMessageInfoUiModel {
        return LastApplyMessageInfoUiModel(
                detail = promoMessageInfo.detail,
                message = promoMessageInfo.message)
    }

    private fun mapErrorDetail(promoErrorDetail: PromoErrorDetail): LastApplyErrorDetailUiModel {
        return LastApplyErrorDetailUiModel(
                message = promoErrorDetail.message)
    }

    private fun mapEmptyCartInfo(promoEmptyCartInfo: PromoEmptyCartInfo): LastApplyEmptyCartInfoUiModel {
        return LastApplyEmptyCartInfoUiModel(
                imgUrl = promoEmptyCartInfo.imageUrl,
                message = promoEmptyCartInfo.message,
                detail = promoEmptyCartInfo.detail
        )
    }

    private fun mapPromoCheckoutErrorDefault(errorDefault: ErrorDefault): PromoCheckoutErrorDefault {
        return PromoCheckoutErrorDefault(
                title = errorDefault.title,
                desc = errorDefault.desc)
    }

    private fun mapCreateListRedPromos(lastApplyPromoData: LastApplyPromoData): List<String> {
        val listRedPromos = arrayListOf<String>()
        if (lastApplyPromoData.message.state == STATE_RED) {
            lastApplyPromoData.codes.forEach {
                listRedPromos.add(it)
            }
        }
        lastApplyPromoData.listVoucherOrders.forEach {
            if (it.message.state == STATE_RED) {
                listRedPromos.add(it.code)
            }
        }
        return listRedPromos
    }
}