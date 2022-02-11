package com.tokopedia.checkout.domain.mapper

import com.tokopedia.checkout.data.model.response.shipmentaddressform.CrossSellBottomSheet
import com.tokopedia.checkout.data.model.response.shipmentaddressform.CrossSellInfoData
import com.tokopedia.checkout.data.model.response.shipmentaddressform.CrossSellOrderSummary
import com.tokopedia.checkout.view.uimodel.CrossSellBottomSheetModel
import com.tokopedia.checkout.view.uimodel.CrossSellInfoModel
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.CrossSellOrderSummaryModel
import com.tokopedia.checkout.data.model.response.shipmentaddressform.*
import com.tokopedia.checkout.data.model.response.shipmentaddressform.AddOnsResponse
import com.tokopedia.checkout.data.model.response.shipmentaddressform.Shop
import com.tokopedia.checkout.domain.model.cartshipmentform.*
import com.tokopedia.checkout.domain.model.cartshipmentform.AddOnDataItem
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticcart.shipping.model.ShipProd
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.*
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.Data
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MvcShippingBenefitUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoSpIdUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.data.PurchaseProtectionPlanDataResponse
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import com.tokopedia.purchase_platform.common.utils.Utils.isNotNullOrEmptyOrZero
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import java.util.*
import javax.inject.Inject

class ShipmentMapper @Inject constructor() {

    fun convertToShipmentAddressFormData(shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse): CartShipmentAddressFormData {
        return CartShipmentAddressFormData().apply {
            var isDisableEgold = false
            var isDisablePPP = false
            var isDisableDonation = false
            var isDisableCrossSell = false
            for (disabledFeature in shipmentAddressFormDataResponse.disabledFeatures) {
                when (disabledFeature) {
                    DISABLED_DROPSHIPPER -> isDropshipperDisable = true
                    DISABLED_ORDER_PRIORITY -> isOrderPrioritasDisable = true
                    DISABLED_EGOLD -> isDisableEgold = true
                    DISABLED_PURCHASE_PROTECTION -> isDisablePPP = true
                    DISABLED_DONATION -> isDisableDonation = true
                    DISABLED_CROSS_SELL -> isDisableCrossSell = true
                }
            }

            keroDiscomToken = shipmentAddressFormDataResponse.keroDiscomToken
            keroToken = shipmentAddressFormDataResponse.keroToken
            keroUnixTime = shipmentAddressFormDataResponse.keroUnixTime
            isHidingCourier = shipmentAddressFormDataResponse.hideCourier
            isBlackbox = shipmentAddressFormDataResponse.isBlackbox == 1
            errorCode = shipmentAddressFormDataResponse.errorCode
            isError = shipmentAddressFormDataResponse.errors.isNotEmpty()
            errorMessage = shipmentAddressFormDataResponse.errors.joinToString()
            isShowOnboarding = shipmentAddressFormDataResponse.isShowOnboarding
            isIneligiblePromoDialogEnabled = shipmentAddressFormDataResponse.isIneligiblePromoDialogEnabled
            isOpenPrerequisiteSite = shipmentAddressFormDataResponse.isOpenPrerequisiteSite
            isEligibleNewShippingExperience = shipmentAddressFormDataResponse.isEligibleNewShippingExperience
            addressesData = mapAddressesData(shipmentAddressFormDataResponse)
            cod = mapCod(shipmentAddressFormDataResponse.cod)
            campaignTimerUi = mapCampaignTimer(shipmentAddressFormDataResponse.campaignTimer)
            lastApplyData = mapPromoLastApply(shipmentAddressFormDataResponse.promoSAFResponse.lastApply.data)
            promoCheckoutErrorDefault = mapPromoCheckoutErrorDefault(shipmentAddressFormDataResponse.promoSAFResponse.errorDefault)
            errorTicker = shipmentAddressFormDataResponse.errorTicker
            groupAddress = mapGroupAddresses(shipmentAddressFormDataResponse, isDisablePPP)
            isHasError = checkCartHasError(this)
            popUpMessage = shipmentAddressFormDataResponse.popUpMessage
            shipmentAddressFormDataResponse.tickers.firstOrNull()?.let {
                tickerData = mapTickerData(it)
            }
            if (!isDisableEgold) {
                egoldAttributes = mapEgold(shipmentAddressFormDataResponse)
            }
            if (!isDisableDonation) {
                donation = mapDonation(shipmentAddressFormDataResponse)
            }
            if (!isDisableCrossSell) {
                crossSell = mapCrossSell(shipmentAddressFormDataResponse)
            }
            popup = mapPopUp(shipmentAddressFormDataResponse.popup)
        }
    }

    private fun mapTickerData(it: Ticker): TickerData {
        return TickerData().apply {
            id = it.id
            message = it.message
            page = it.page
            title = ""
        }
    }

    private fun mapGroupAddresses(shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse, isDisablePPP: Boolean): MutableList<GroupAddress> {
        val groupAddressListResult = arrayListOf<GroupAddress>()
        for (groupAddress in shipmentAddressFormDataResponse.groupAddress) {
            groupAddressListResult.add(
                    GroupAddress().apply {
                        isError = !groupAddress.errors.isNullOrEmpty() || shipmentAddressFormDataResponse.errorTicker.isNotEmpty()
                        errorMessage = groupAddress.errors.joinToString()
                        userAddress = mapUserAddress(groupAddress)
                        groupShop = mapGroupShops(groupAddress, shipmentAddressFormDataResponse, isDisablePPP)
                    }
            )
        }
        return groupAddressListResult
    }

    private fun mapGroupShops(groupAddress: com.tokopedia.checkout.data.model.response.shipmentaddressform.GroupAddress,
                              shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse,
                              isDisablePPP: Boolean): MutableList<GroupShop> {
        val groupShopListResult = arrayListOf<GroupShop>()
        groupAddress.groupShop.forEach {
            groupShopListResult.add(
                    GroupShop().apply {
                        isError = !it.errors.isNullOrEmpty() || shipmentAddressFormDataResponse.errorTicker.isNotEmpty()
                        errorMessage = if (shipmentAddressFormDataResponse.errorTicker.isNotEmpty()) "" else it.errors.joinToString()
                        hasUnblockingError = !it.unblockingErrors.isNullOrEmpty()
                        unblockingErrorMessage = it.unblockingErrors.joinToString()
                        shippingId = it.shippingId
                        spId = it.spId
                        dropshipperName = it.dropshiper.name
                        dropshipperPhone = it.dropshiper.telpNo
                        isUseInsurance = it.isInsurance
                        cartString = it.cartString
                        isHasPromoList = it.isHasPromoList
                        isSaveStateFlag = it.isSaveStateFlag
                        isLeasingProduct = it.vehicleLeasing.isLeasingProduct
                        bookingFee = it.vehicleLeasing.bookingFee
                        listPromoCodes = it.listPromoCodes
                        isFulfillment = it.isFulfillment
                        fulfillmentId = it.warehouse.warehouseId
                        fulfillmentBadgeUrl = it.tokoCabangInfo.badgeUrl
                        fulfillmentName = it.tokoCabangInfo.message
                        shipmentInformationData = mapShipmentInformationData(it.shipmentInformation)
                        shop = mapShopData(it.shop)
                        addOns = mapAddOnsData(it.addOns)
                        shopShipments = mapShopShipments(it.shopShipments)
                        val mapProducts = mapProducts(it, groupAddress, shipmentAddressFormDataResponse, isDisablePPP, shop.shopTypeInfoData)
                        products = mapProducts.first
                        firstProductErrorIndex = mapProducts.second
                        isDisableChangeCourier = it.isDisableChangeCourier
                        autoCourierSelection = it.autoCourierSelection
                        boMetadata = it.boMetadata
                        courierSelectionErrorData = CourierSelectionErrorData(it.courierSelectionError.title, it.courierSelectionError.description)
                    }
            )
        }
        return groupShopListResult
    }

    private fun mapProducts(groupShop: com.tokopedia.checkout.data.model.response.shipmentaddressform.GroupShop,
                            groupAddress: com.tokopedia.checkout.data.model.response.shipmentaddressform.GroupAddress,
                            shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse,
                            isDisablePPP: Boolean,
                            shopTypeInfoData: ShopTypeInfoData): Pair<MutableList<Product>, Int> {
        val productListResult = arrayListOf<Product>()
        var firstErrorIndex = -1
        groupShop.cartDetails.forEachIndexed { index, cartDetail ->
            cartDetail.products.forEach { product ->
                val productResult = Product().apply {
                    analyticsProductCheckoutData = mapAnalyticsProductCheckoutData(
                            product,
                            groupAddress.userAddress,
                            groupShop,
                            shipmentAddressFormDataResponse.cod,
                            shipmentAddressFormDataResponse.promoSAFResponse,
                            shopTypeInfoData
                    )
                    if (product.tradeInInfo.isValidTradeIn) {
                        productPrice = product.tradeInInfo.newDevicePrice.toLong()
                    }
                    isError = !product.errors.isNullOrEmpty() ||
                            shipmentAddressFormDataResponse.errorTicker.isNotEmpty() ||
                            cartDetail.bundleDetail.bundleId.isNotBlankOrZero() && cartDetail.errors.isNotEmpty()
                    errorMessage = if (shipmentAddressFormDataResponse.errorTicker.isNotEmpty()) {
                        ""
                    } else if (product.errors.isNotEmpty()) {
                        product.errors[0]
                    } else if (cartDetail.bundleDetail.bundleId.isNotBlankOrZero() && cartDetail.errors.isNotEmpty()) {
                        // Bundle error
                        cartDetail.errors[0]
                    } else {
                        ""
                    }
                    errorMessageDescription = if (shipmentAddressFormDataResponse.errorTicker.isNotEmpty()) "" else if (product.errors.size >= 2) product.errors[1] else ""
                    if (isError) {
                        if (firstErrorIndex == -1) {
                            firstErrorIndex = index
                        }
                    }
                    productId = product.productId
                    cartId = product.cartId
                    productName = product.productName
                    productPriceFmt = product.productPriceFmt
                    productPrice = product.productPrice
                    productOriginalPrice = product.productOriginalPrice
                    productWholesalePrice = product.productWholesalePrice
                    productWholesalePriceFmt = product.productWholesalePriceFmt
                    productWeightFmt = product.productWeightFmt
                    productWeight = product.productWeight
                    productWeightActual = product.productWeightActual
                    productCondition = product.productCondition
                    productUrl = product.productUrl
                    isProductReturnable = product.productReturnable == 1
                    isProductIsFreeReturns = product.productIsFreeReturns == 1
                    isProductIsPreorder = product.productIsPreorder == 1
                    preOrderDurationDay = product.productPreorder.durationDay
                    if (product.productPreorder.durationText.isNotEmpty()) {
                        productPreOrderInfo = "PO " + product.productPreorder.durationText
                    }
                    productCashback = product.productCashback
                    productMinOrder = product.productMinOrder
                    productInvenageValue = product.productInvenageValue
                    productSwitchInvenage = product.productSwitchInvenage
                    productPriceCurrency = product.productPriceCurrency
                    productImageSrc200Square = product.productImageSrc200Square
                    productNotes = product.productNotes
                    productQuantity = product.productQuantity
                    isProductFinsurance = product.productFinsurance == 1
                    isProductFcancelPartial = product.productFcancelPartial == 1
                    productCatId = product.productCatId
                    isShowTicker = product.productTicker.isShowTicker
                    tickerMessage = product.productTicker.message
                    if (product.freeShippingExtra.eligible) {
                        isFreeShippingExtra = true
                    }
                    if (product.freeShipping.eligible) {
                        isFreeShipping = true
                    }
                    if (product.tradeInInfo.isValidTradeIn) {
                        tradeInInfoData = mapTradeInInfoData(product.tradeInInfo)
                    }
                    if (!isDisablePPP && product.purchaseProtectionPlanDataResponse.protectionAvailable) {
                        purchaseProtectionPlanData = mapPurchaseProtectionData(product.purchaseProtectionPlanDataResponse)
                    }
                    variant = product.variantDescriptionDetail.variantDescription
                    productAlertMessage = product.productAlertMessage
                    productInformation = product.productInformation
                    if (cartDetail.bundleDetail.bundleId.isNotBlankOrZero()) {
                        isBundlingItem = true
                        bundlingItemPosition = if (cartDetail.products.firstOrNull()?.productId == productId) {
                            BUNDLING_ITEM_HEADER
                        } else if (cartDetail.products.lastOrNull()?.productId == productId) {
                            BUNDLING_ITEM_FOOTER
                        } else {
                            BUNDLING_ITEM_DEFAULT
                        }
                        bundleId = cartDetail.bundleDetail.bundleId
                        bundleGroupId = cartDetail.bundleDetail.bundleGroupId
                        bundleType = cartDetail.bundleDetail.bundleType
                        bundleTitle = cartDetail.bundleDetail.bundleName
                        bundlePrice = cartDetail.bundleDetail.bundlePrice
                        bundleSlashPriceLabel = cartDetail.bundleDetail.slashPriceLabel
                        bundleOriginalPrice = cartDetail.bundleDetail.bundleOriginalPrice
                        bundleQuantity = cartDetail.bundleDetail.bundleQty
                        bundleIconUrl = cartDetail.bundleDetail.bundleIconUrl
                    } else {
                        isBundlingItem = false
                        bundleId = "0"
                    }
                    addOnProduct = mapAddOnsData(product.addOns)
                }
                productListResult.add(productResult)
            }
        }
        return productListResult to firstErrorIndex
    }

    private fun mapAnalyticsProductCheckoutData(product: com.tokopedia.checkout.data.model.response.shipmentaddressform.Product,
                                                userAddress: UserAddress,
                                                groupShop: com.tokopedia.checkout.data.model.response.shipmentaddressform.GroupShop,
                                                cod: Cod,
                                                promoSAFResponse: PromoSAFResponse,
                                                shopTypeInfoData: ShopTypeInfoData): AnalyticsProductCheckoutData {
        return AnalyticsProductCheckoutData().apply {
            productId = product.productId.toString()
            productAttribution = product.productTrackerData.attribution
            productListName = product.productTrackerData.trackerListName
            productCategory = product.productCategory
            productCategoryId = product.productCatId.toString()
            productName = product.productName
            productPrice = product.productPrice.toString()
            if (product.tradeInInfo.isValidTradeIn) {
                productPrice = product.tradeInInfo.newDevicePrice.toString()
            }
            productShopId = groupShop.shop.shopId.toString()
            productShopName = groupShop.shop.shopName
            productShopType = shopTypeInfoData.shopType
            productVariant = ""
            productBrand = ""
            productQuantity = product.productQuantity
            warehouseId = groupShop.warehouse.warehouseId.toString()
            productWeight = product.productWeight.toString()
            buyerAddressId = userAddress.addressId
            shippingDuration = ""
            courier = ""
            shippingPrice = ""
            codFlag = cod.isCod.toString()
            tokopediaCornerFlag = if (isNotNullOrEmptyOrZero(userAddress.cornerId)) {
                true.toString()
            } else {
                false.toString()
            }
            isFulfillment = groupShop.isFulfillment.toString()
            isDiscountedPrice = product.productOriginalPrice > 0
            campaignId = product.campaignId
            promoSAFResponse.lastApply.data.trackingDetails.forEach {
                if (it.productId == product.productId) {
                    promoCode = it.promoCodesTracking
                    promoDetails = it.promoDetailsTracking
                }
            }
        }
    }

    private fun mapShopShipments(shopShipment: List<com.tokopedia.checkout.data.model.response.shipmentaddressform.ShopShipment>): List<ShopShipment> {
        val shopShipmentListResult = arrayListOf<ShopShipment>()
        shopShipment.forEach {
            shopShipmentListResult.add(
                    ShopShipment().apply {
                        isDropshipEnabled = it.isDropshipEnabled == 1
                        shipCode = it.shipCode
                        shipId = it.shipId
                        shipLogo = it.shipLogo
                        shipName = it.shipName
                        shipProds = mapShipProds(it.shipProds)
                    }
            )
        }
        return shopShipmentListResult
    }

    private fun mapShipProds(shipProds: List<com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipProd>): List<ShipProd> {
        val shipProdListResult = arrayListOf<ShipProd>()
        shipProds.forEach {
            shipProdListResult.add(
                    ShipProd().apply {
                        additionalFee = it.additionalFee
                        minimumWeight = it.minimumWeight
                        shipGroupId = it.shipGroupId
                        shipGroupName = it.shipGroupName
                        shipProdId = it.shipProdId
                        shipProdName = it.shipProdName
                    }
            )
        }

        return shipProdListResult
    }

    private fun mapShopData(shop: Shop): com.tokopedia.checkout.domain.model.cartshipmentform.Shop {
        return com.tokopedia.checkout.domain.model.cartshipmentform.Shop().apply {
            shopId = shop.shopId
            shopName = shop.shopName
            shopImage = shop.shopImage
            shopUrl = shop.shopUrl
            shopStatus = shop.shopStatus
            shopTypeInfoData = mapShopTypeInfo(shop)
            postalCode = shop.postalCode
            latitude = shop.latitude
            longitude = shop.longitude
            districtId = shop.districtId
            districtName = shop.districtName
            origin = shop.origin
            addressStreet = shop.addressStreet
            provinceId = shop.provinceId
            cityId = shop.cityId
            cityName = shop.cityName
            shopAlertMessage = shop.shopAlertMessage
            isTokoNow = shop.isTokoNow
        }
    }

    private fun mapAddOnsData(addOns: AddOnsResponse): AddOns {
        return AddOns().apply {
            status = addOns.status
            addOnButton = mapAddOnButton(addOns.addOnButton)
            addOnBottomsheet = mapAddOnBottomSheet(addOns.addOnBottomsheet)
            addOnData = mapAddOnListData(addOns.addOnData)
        }
    }

    private fun mapAddOnListData(addOnData: List<AddOnsResponse.AddOnDataItem>): MutableList<AddOnDataItem> {
        val listAddOnDataItem = arrayListOf<AddOnDataItem>()
        addOnData.forEach { item ->
            listAddOnDataItem.add(AddOnDataItem().apply {
                addOnPrice = item.addOnPrice
                addOnId = item.addOnId
                addOnMetadata = item.addOnMetadata
                addOnQty = item.addOnQty
            })
        }
        return listAddOnDataItem
    }

    private fun mapAddOnButton(addOnButton: AddOnsResponse.AddOnButton): AddOnButton {
        return AddOnButton().apply {
            leftIconUrl = addOnButton.leftIconUrl
            rightIconUrl = addOnButton.rightIconUrl
            description = addOnButton.description
            action = addOnButton.action
            title = addOnButton.title
        }
    }

    private fun mapAddOnBottomSheet(addOnBottomsheet: AddOnsResponse.AddOnBottomsheet): AddOnBottomsheet {
        return AddOnBottomsheet().apply {
            headerTitle = addOnBottomsheet.headerTitle
            description = addOnBottomsheet.description
            ticker = mapAddOnTicker(addOnBottomsheet.ticker)
            products = mapAddOnProducts(addOnBottomsheet.products)
        }
    }

    private fun mapAddOnTicker(ticker: AddOnsResponse.AddOnBottomsheet.Ticker): AddOnTicker {
        return AddOnTicker().apply {
            text = ticker.text
        }
    }

    private fun mapAddOnProducts(products: List<AddOnsResponse.AddOnBottomsheet.ProductsItem>): MutableList<AddOnProductsItem> {
        val listAddOnProductItem = arrayListOf<AddOnProductsItem>()
        products.forEach { productItem ->
            listAddOnProductItem.add(AddOnProductsItem().apply {
                productImageUrl = productItem.productImageUrl
                productName = productItem.productName
            })
        }
        return listAddOnProductItem
    }

    private fun mapShopTypeInfo(shop: Shop): ShopTypeInfoData {
        val shopTypeInfo = shop.shopTypeInfo
        val tmpShopType =
                when {
                    shop.isGold == 1 -> SHOP_TYPE_GOLD_MERCHANT
                    shop.isOfficial == 1 -> SHOP_TYPE_OFFICIAL_STORE
                    else -> SHOP_TYPE_REGULER
                }
        return ShopTypeInfoData().apply {
            shopTier = shopTypeInfo.shopTier
            shopGrade = shopTypeInfo.shopGrade
            shopBadge = shopTypeInfo.shopBadge
            badgeSvg = shopTypeInfo.badgeSvg
            title = shopTypeInfo.title
            titleFmt = shopTypeInfo.titleFmt
            shopType = tmpShopType
        }
    }

    private fun mapShipmentInformationData(shipmentInformation: ShipmentInformation): ShipmentInformationData {
        return ShipmentInformationData().apply {
            preorder = mapPreorderData(shipmentInformation)
            estimation = shipmentInformation.estimation
            shopLocation = shipmentInformation.shopLocation
            freeShipping = mapFreeShippingData(shipmentInformation.freeShipping)
            freeShippingExtra = mapFreeShippingData(shipmentInformation.freeShippingExtra)
        }
    }

    private fun mapFreeShippingData(freeShipping: FreeShipping): FreeShippingData {
        return FreeShippingData().apply {
            badgeUrl = freeShipping.badgeUrl
            eligible = freeShipping.eligible
        }
    }

    private fun mapPreorderData(shipmentInformation: ShipmentInformation): PreorderData {
        return PreorderData().apply {
            duration = shipmentInformation.preorder.duration
            isPreorder = shipmentInformation.preorder.isPreorder
        }
    }

    private fun mapUserAddress(groupAddress: com.tokopedia.checkout.data.model.response.shipmentaddressform.GroupAddress): com.tokopedia.logisticCommon.data.entity.address.UserAddress {
        return com.tokopedia.logisticCommon.data.entity.address.UserAddress().apply {
            status = groupAddress.userAddress.status
            address = groupAddress.userAddress.address
            address2 = groupAddress.userAddress.address2
            addressId = groupAddress.userAddress.addressId
            addressName = groupAddress.userAddress.addressName
            cityId = groupAddress.userAddress.cityId
            cityName = groupAddress.userAddress.cityName
            country = groupAddress.userAddress.country
            districtId = groupAddress.userAddress.districtId
            districtName = groupAddress.userAddress.districtName
            latitude = groupAddress.userAddress.latitude
            longitude = groupAddress.userAddress.longitude
            phone = groupAddress.userAddress.phone
            postalCode = groupAddress.userAddress.postalCode
            provinceId = groupAddress.userAddress.provinceId
            provinceName = groupAddress.userAddress.provinceName
            receiverName = groupAddress.userAddress.receiverName
            cornerId = groupAddress.userAddress.cornerId
            isCorner = groupAddress.userAddress.isCorner
            state = groupAddress.userAddress.state
            stateDetail = groupAddress.userAddress.stateDetail
            shopId = groupAddress.userAddress.tokoNow.shopId
            warehouseId = groupAddress.userAddress.tokoNow.warehouseId
        }
    }

    private fun mapTradeInInfoData(tradeInInfo: TradeInInfo): TradeInInfoData {
        return TradeInInfoData().apply {
            isValidTradeIn = tradeInInfo.isValidTradeIn
            newDevicePrice = tradeInInfo.newDevicePrice
            newDevicePriceFmt = tradeInInfo.newDevicePriceFmt
            oldDevicePrice = tradeInInfo.oldDevicePrice
            oldDevicePriceFmt = tradeInInfo.oldDevicePriceFmt
            isDropOffEnable = tradeInInfo.isDropOffEnable
            deviceModel = tradeInInfo.deviceModel
            diagnosticId = tradeInInfo.diagnosticId
        }
    }

    private fun mapPurchaseProtectionData(pppDataMapping: PurchaseProtectionPlanDataResponse): PurchaseProtectionPlanData {
        return PurchaseProtectionPlanData().apply {
            isProtectionAvailable = pppDataMapping.protectionAvailable
            protectionLinkText = pppDataMapping.protectionLinkText
            protectionLinkUrl = pppDataMapping.protectionLinkUrl
            isProtectionOptIn = pppDataMapping.protectionOptIn
            protectionPrice = pppDataMapping.protectionPrice
            protectionPricePerProduct = pppDataMapping.protectionPricePerProduct
            protectionSubtitle = pppDataMapping.protectionSubtitle
            protectionTitle = pppDataMapping.protectionTitle
            protectionTypeId = pppDataMapping.protectionTypeId
            isProtectionCheckboxDisabled = pppDataMapping.protectionCheckboxDisabled
        }
    }

    private fun mapPromoLastApply(promoData: Data): LastApplyUiModel {
        return LastApplyUiModel().apply {
            codes = mapPromoGlobalCodes(promoData)
            voucherOrders = mapPromoVoucherOrders(promoData)
            additionalInfo = mapLastApplyAdditionalInfoUiModel(promoData.additionalInfo)
            message = mapLastApplyMessageUiModel(promoData.message)
//            listRedPromos = mapListRedPromos(promoData)
            listAllPromoCodes = mapListAllPromos(promoData)
        }
    }

    private fun mapListAllPromos(promoData: Data): List<String> {
        val listAllPromoCodes = arrayListOf<String>()
        promoData.codes.forEach { promoCode ->
            listAllPromoCodes.add(promoCode)
        }
        promoData.voucherOrders.forEach {
            listAllPromoCodes.add(it.code)
        }
        return listAllPromoCodes
    }

    private fun mapListRedPromos(promoData: Data?): List<String> {
        val listRedStates = arrayListOf<String>()
        if (promoData?.message?.state.equals(CheckoutConstant.STATE_RED, ignoreCase = true)) {
            promoData?.codes?.forEach { promoCode ->
                listRedStates.add(promoCode)
            }
        }
        promoData?.voucherOrders?.forEach {
            if (it.message.state.equals(CheckoutConstant.STATE_RED, ignoreCase = true)) {
                listRedStates.add(it.code)
            }
        }
        return listRedStates
    }

    private fun mapLastApplyAdditionalInfoUiModel(additionalInfo: AdditionalInfo): LastApplyAdditionalInfoUiModel {
        return LastApplyAdditionalInfoUiModel().apply {
            emptyCartInfo = mapLastApplyEmptyCartInfoUiModel(additionalInfo.cartEmptyInfo)
            errorDetail = mapLastApplyErrorDetailUiModel(additionalInfo.errorDetail)
            messageInfo = mapLastApplyMessageInfoUiModel(additionalInfo.messageInfo)
            promoSpIds = mapPromoSpId(additionalInfo)
            usageSummaries = mapLastApplyUsageSummariesUiModel(additionalInfo.listUsageSummaries)
        }
    }

    private fun mapLastApplyUsageSummariesUiModel(listUsageSummaries: List<UsageSummaries>): List<LastApplyUsageSummariesUiModel> {
        return listUsageSummaries.map {
            LastApplyUsageSummariesUiModel().apply {
                description = it.desc
                type = it.type
                amountStr = it.amountStr
                amount = it.amount
                currencyDetailsStr = it.currencyDetailsStr
            }
        }
    }

    private fun mapLastApplyMessageInfoUiModel(messageInfo: MessageInfo): LastApplyMessageInfoUiModel {
        return LastApplyMessageInfoUiModel().apply {
            detail = messageInfo.detail
            message = messageInfo.message
        }
    }

    private fun mapLastApplyErrorDetailUiModel(errorDetail: ErrorDetail): LastApplyErrorDetailUiModel {
        return LastApplyErrorDetailUiModel().apply {
            message = errorDetail.message
        }
    }

    private fun mapLastApplyEmptyCartInfoUiModel(cartEmptyInfo: CartEmptyInfo): LastApplyEmptyCartInfoUiModel {
        return LastApplyEmptyCartInfoUiModel().apply {
            detail = cartEmptyInfo.detail
            imgUrl = cartEmptyInfo.imageUrl
            message = cartEmptyInfo.message
        }
    }

    private fun mapPromoVoucherOrders(promoData: Data): List<LastApplyVoucherOrdersItemUiModel> {
        val listVoucherOrdersUiModel = arrayListOf<LastApplyVoucherOrdersItemUiModel>()
        promoData.voucherOrders.forEach { voucherOrdersItem ->
            listVoucherOrdersUiModel.add(
                    LastApplyVoucherOrdersItemUiModel().apply {
                        code = voucherOrdersItem.code
                        uniqueId = voucherOrdersItem.uniqueId
                        message = mapLastApplyMessageUiModel(voucherOrdersItem.message)
                    }
            )
        }

        return listVoucherOrdersUiModel
    }

    private fun mapLastApplyMessageUiModel(message: Message): LastApplyMessageUiModel {
        return LastApplyMessageUiModel().apply {
            color = message.color
            state = message.state
            text = message.text
        }
    }

    private fun mapPromoGlobalCodes(promoData: Data): MutableList<String> {
        return promoData.codes.toMutableList()
    }

    private fun mapPromoCheckoutErrorDefault(errorDefault: ErrorDefault): PromoCheckoutErrorDefault {
        return PromoCheckoutErrorDefault().apply {
            title = errorDefault.title
            desc = errorDefault.description
        }
    }

    private fun mapCampaignTimer(campaignTimer: CampaignTimer): CampaignTimerUi {
        return CampaignTimerUi(
                campaignTimer.expiredTimerMessage.button,
                campaignTimer.expiredTimerMessage.description,
                campaignTimer.expiredTimerMessage.title,
                campaignTimer.showTimer,
                campaignTimer.timerDetail.deductTime,
                campaignTimer.description,
                campaignTimer.timerDetail.expiredTime,
                campaignTimer.timerDetail.expiredDuration,
                campaignTimer.timerDetail.serverTime,
                0,
                ""
        )
    }

    private fun mapCod(cod: Cod): CodModel {
        return CodModel().apply {
            isCod = cod.isCod
            counterCod = cod.counterCod
        }
    }

    private fun mapPopUp(popup: PopUp): PopUpData {
        return PopUpData().apply {
            button = mapButton(popup.button)
            description = popup.description
            title = popup.title
        }
    }

    private fun mapButton(button: Button): ButtonData {
        return ButtonData().apply {
            text = button.text
        }
    }

    private fun mapDonation(shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse): Donation {
        return Donation().apply {
            title = shipmentAddressFormDataResponse.donation.title
            description = shipmentAddressFormDataResponse.donation.description
            nominal = shipmentAddressFormDataResponse.donation.nominal
            isChecked = shipmentAddressFormDataResponse.isDonationCheckboxStatus
        }
    }

    private fun mapCrossSell(shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse): ArrayList<CrossSellModel> {
        val arrayListCrossSell: ArrayList<CrossSellModel> = arrayListOf()
        shipmentAddressFormDataResponse.crossSell.forEach { crossSell ->
            arrayListCrossSell.add(
                    CrossSellModel().apply {
                        id = crossSell.id
                        checkboxDisabled = crossSell.checkboxDisabled
                        isChecked = crossSell.isChecked
                        additionalVerticalId = crossSell.additionalVerticalId
                        transactionType = crossSell.transactionType
                        price = crossSell.price
                        bottomSheet = mapCrossSellBottomSheet(crossSell.bottomSheet)
                        info = mapCrossSellInfo(crossSell.info)
                        orderSummary = mapCrossSellOrderSummary(crossSell.orderSummary)
                    }
            )
        }
        return arrayListCrossSell
    }

    private fun mapCrossSellBottomSheet(bottomSheet: CrossSellBottomSheet): CrossSellBottomSheetModel {
        return CrossSellBottomSheetModel().apply {
            subtitle = bottomSheet.subtitle
            title = bottomSheet.title
        }
    }

    private fun mapCrossSellInfo(info: CrossSellInfoData): CrossSellInfoModel {
        return CrossSellInfoModel().apply {
            iconUrl = info.iconUrl
            subtitle = info.subtitle
            title = info.title
            tooltipText = info.tooltipText
        }
    }

    private fun mapCrossSellOrderSummary(crossSellOrderSummary: CrossSellOrderSummary): CrossSellOrderSummaryModel {
        return CrossSellOrderSummaryModel().apply {
            priceWording = crossSellOrderSummary.priceWording
            title = crossSellOrderSummary.title
        }
    }

    private fun mapEgold(shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse): EgoldAttributeModel {
        return EgoldAttributeModel().apply {
            isEligible = shipmentAddressFormDataResponse.egoldAttributes.isEligible
            isTiering = shipmentAddressFormDataResponse.egoldAttributes.isTiering
            isChecked = shipmentAddressFormDataResponse.egoldAttributes.isOptIn
            minEgoldRange = shipmentAddressFormDataResponse.egoldAttributes.egoldRange.minEgoldValue
            maxEgoldRange = shipmentAddressFormDataResponse.egoldAttributes.egoldRange.maxEgoldValue
            titleText = shipmentAddressFormDataResponse.egoldAttributes.egoldMessage.titleText
            subText = shipmentAddressFormDataResponse.egoldAttributes.egoldMessage.subText
            tickerText = shipmentAddressFormDataResponse.egoldAttributes.egoldMessage.tickerText
            tooltipText = shipmentAddressFormDataResponse.egoldAttributes.egoldMessage.tooltipText

            val tmpEgoldTieringModelArrayList: ArrayList<EgoldTieringModel> = arrayListOf()
            shipmentAddressFormDataResponse.egoldAttributes.egoldTieringDataArrayList.forEach {
                tmpEgoldTieringModelArrayList.add(
                        EgoldTieringModel().apply {
                            basisAmount = it.basisAmount
                            maxAmount = it.maxAmount
                            minAmount = it.minAmount
                            minTotalAmount = it.minTotalAmount
                        }
                )
            }
            egoldTieringModelArrayList = tmpEgoldTieringModelArrayList
        }
    }

    private fun mapPromoSpId(responseAdditionalInfo: AdditionalInfo?): List<PromoSpIdUiModel> {
        val promoSpIdUiModels: MutableList<PromoSpIdUiModel> = ArrayList()
        val promoSpIds = responseAdditionalInfo?.promoSpIds ?: emptyList()
        if (promoSpIds.isNotEmpty()) {
            for (promoSpId in promoSpIds) {
                val promoSpIdUiModel = PromoSpIdUiModel()
                promoSpIdUiModel.uniqueId = promoSpId.uniqueId
                if (promoSpId.mvcShippingBenefits.isNotEmpty()) {
                    val mvcShippingBenefitUiModels: MutableList<MvcShippingBenefitUiModel> = ArrayList()
                    for (mvcShippingBenefit in promoSpId.mvcShippingBenefits) {
                        val mvcShippingBenefitUiModel = MvcShippingBenefitUiModel().apply {
                            benefitAmount = mvcShippingBenefit.benefitAmount
                            spId = mvcShippingBenefit.spId
                        }
                        mvcShippingBenefitUiModels.add(mvcShippingBenefitUiModel)
                    }
                    promoSpIdUiModel.mvcShippingBenefits = mvcShippingBenefitUiModels
                }
                promoSpIdUiModels.add(promoSpIdUiModel)
            }
        }
        return promoSpIdUiModels
    }

    private fun mapAddressesData(shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse): AddressesData {
        val addressesData = AddressesData()

        // Set default address for normal checkout or tradein checkout
        val addressData = AddressData()
        if (shipmentAddressFormDataResponse.groupAddress.isNotEmpty()) {
            val defaultAddress = shipmentAddressFormDataResponse.groupAddress[0].userAddress
            val defaultAddressData = mapUserAddress(defaultAddress)
            addressData.defaultAddress = defaultAddressData
        }
        addressesData.data = addressData

        // Get trade in address if available
        val addresses = shipmentAddressFormDataResponse.addresses
        if (addresses.active.isNotEmpty()) {
            var tradeInDefaultAddress: UserAddress? = null
            var tradeInDropOffAddress: UserAddress? = null
            for ((key, value) in addresses.data) {
                if (key == AddressesData.DEFAULT_ADDRESS) {
                    tradeInDefaultAddress = value
                } else if (key == AddressesData.TRADE_IN_ADDRESS) {
                    tradeInDropOffAddress = value
                }
            }
            if (tradeInDefaultAddress != null) {
                addressData.defaultAddress = mapUserAddress(tradeInDefaultAddress)
            }
            if (tradeInDropOffAddress != null) {
                addressData.tradeInAddress = mapUserAddress(tradeInDropOffAddress)
            }
            addressesData.disableTabs = addresses.disableTabs
            addressesData.active = addresses.active
            addressesData.data = addressData
        }
        return addressesData
    }

    private fun mapUserAddress(defaultAddress: UserAddress): com.tokopedia.logisticCommon.data.entity.address.UserAddress {
        return com.tokopedia.logisticCommon.data.entity.address.UserAddress().apply {
            addressId = defaultAddress.addressId
            addressName = defaultAddress.addressName
            address = defaultAddress.address
            address2 = defaultAddress.address2
            cityId = defaultAddress.cityId
            cityName = defaultAddress.cityName
            isCorner = defaultAddress.isCorner
            cornerId = defaultAddress.cornerId
            country = defaultAddress.country
            districtId = defaultAddress.districtId
            districtName = defaultAddress.districtName
            latitude = defaultAddress.latitude
            longitude = defaultAddress.longitude
            phone = defaultAddress.phone
            postalCode = defaultAddress.postalCode
            provinceId = defaultAddress.provinceId
            provinceName = defaultAddress.provinceName
            receiverName = defaultAddress.receiverName
            status = defaultAddress.status
            shopId = defaultAddress.tokoNow.shopId
            warehouseId = defaultAddress.tokoNow.warehouseId
        }
    }

    private fun checkCartHasError(cartShipmentAddressFormData: CartShipmentAddressFormData): Boolean {
        var hasError = false
        for (groupAddress in cartShipmentAddressFormData.groupAddress) {
            if (groupAddress.isError) {
                hasError = true
                break
            }
            for (groupShop in groupAddress.groupShop) {
                if (groupShop.isError) {
                    hasError = true
                }
                var totalProductError = 0
                var defaultErrorMessage = ""
                var allProductsHaveSameError = true
                for ((isError, errorMessage) in groupShop.products) {
                    if (isError || errorMessage.isNotEmpty()) {
                        hasError = true
                        totalProductError++
                        if (defaultErrorMessage.isEmpty()) {
                            defaultErrorMessage = errorMessage
                        } else if (allProductsHaveSameError && defaultErrorMessage != errorMessage) {
                            allProductsHaveSameError = false
                        }
                    }
                }
                if (totalProductError == groupShop.products.size) {
                    if (!groupShop.isError) {
                        groupShop.isError = true
                        groupShop.errorMessage = defaultErrorMessage
                    }
                    for (product in groupShop.products) {
                        if (allProductsHaveSameError) {
                            product.isError = false
                            product.errorMessage = ""
                        }
                    }
                }
            }
        }
        return hasError
    }

    companion object {
        private const val SHOP_TYPE_OFFICIAL_STORE = "official_store"
        private const val SHOP_TYPE_GOLD_MERCHANT = "gold_merchant"
        private const val SHOP_TYPE_REGULER = "reguler"

        const val DISABLED_DROPSHIPPER = "dropshipper"
        const val DISABLED_ORDER_PRIORITY = "order_prioritas"
        const val DISABLED_EGOLD = "egold"
        const val DISABLED_PURCHASE_PROTECTION = "ppp"
        const val DISABLED_DONATION = "donation"
        const val DISABLED_CROSS_SELL = "cross_sell"

        const val BUNDLING_ITEM_DEFAULT = 0
        const val BUNDLING_ITEM_HEADER = 1
        const val BUNDLING_ITEM_FOOTER = 2
    }
}