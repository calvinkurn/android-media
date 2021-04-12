package com.tokopedia.checkout.domain.mapper

import com.tokopedia.checkout.data.model.response.shipmentaddressform.*
import com.tokopedia.checkout.data.model.response.shipmentaddressform.Shop
import com.tokopedia.checkout.domain.model.cartshipmentform.*
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel
import com.tokopedia.logisticcart.shipping.model.AnalyticsProductCheckoutData
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.logisticcart.shipping.model.ShipProd
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.*
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.Data
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.*
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MvcShippingBenefitUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoSpIdUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import com.tokopedia.purchase_platform.common.utils.Utils.isNotNullOrEmptyOrZero
import com.tokopedia.purchase_platform.common.utils.convertToString
import com.tokopedia.purchase_platform.common.utils.isNullOrEmpty
import java.util.*
import javax.inject.Inject

class ShipmentMapper @Inject constructor() {

    fun convertToShipmentAddressFormData(shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse): CartShipmentAddressFormData {
        val dataResult = CartShipmentAddressFormData()

        var isDisableEgold = false
        var isDisablePPP = false
        var isDisableDonation = false
        for (disabledFeature in shipmentAddressFormDataResponse.disabledFeatures) {
            when (disabledFeature) {
                dropshipper -> dataResult.isDropshipperDisable = true
                orderPrioritas -> dataResult.isOrderPrioritasDisable = true
                egold -> isDisableEgold = true
                ppp -> isDisablePPP = true
                donation -> isDisableDonation = true
            }
        }

        dataResult.keroDiscomToken = shipmentAddressFormDataResponse.keroDiscomToken
        dataResult.keroToken = shipmentAddressFormDataResponse.keroToken
        dataResult.keroUnixTime = shipmentAddressFormDataResponse.keroUnixTime
        dataResult.isHidingCourier = shipmentAddressFormDataResponse.hideCourier
        dataResult.isBlackbox = shipmentAddressFormDataResponse.isBlackbox == 1
        dataResult.errorCode = shipmentAddressFormDataResponse.errorCode
        dataResult.isError = !isNullOrEmpty(shipmentAddressFormDataResponse.errors)
        dataResult.errorMessage = convertToString(shipmentAddressFormDataResponse.errors)
        dataResult.isShowOnboarding = shipmentAddressFormDataResponse.isShowOnboarding
        dataResult.isIneligiblePromoDialogEnabled = shipmentAddressFormDataResponse.isIneligiblePromoDialogEnabled
        dataResult.isOpenPrerequisiteSite = shipmentAddressFormDataResponse.isOpenPrerequisiteSite
        dataResult.isEligibleNewShippingExperience = shipmentAddressFormDataResponse.isEligibleNewShippingExperience
        dataResult.addressesData = mapAddressesData(shipmentAddressFormDataResponse)

        if (shipmentAddressFormDataResponse.tickers.isNotEmpty()) {
            val ticker = shipmentAddressFormDataResponse.tickers[0]
            dataResult.tickerData = TickerData(ticker.id, ticker.message, ticker.page, "")
        }

        if (!isDisableEgold) {
            dataResult.egoldAttributes = mapEgold(shipmentAddressFormDataResponse)
        }

        if (!isDisableDonation) {
            dataResult.donation = mapDonation(shipmentAddressFormDataResponse)
        }

        dataResult.cod = mapCod(shipmentAddressFormDataResponse)
        dataResult.campaignTimerUi = mapCampaignTimer(shipmentAddressFormDataResponse.campaignTimer)
        dataResult.lastApplyData = mapPromoLastApply(shipmentAddressFormDataResponse.promoSAFResponse.lastApply?.data)
        dataResult.promoCheckoutErrorDefault = mapPromoCheckoutErrorDefault(shipmentAddressFormDataResponse.promoSAFResponse.errorDefault)

        if (!isNullOrEmpty(shipmentAddressFormDataResponse.groupAddress)) {
            val groupAddressListResult: MutableList<GroupAddress> = ArrayList()
            for (groupAddress in shipmentAddressFormDataResponse.groupAddress) {
                val groupAddressResult = GroupAddress()
                groupAddressResult.isError = !isNullOrEmpty(groupAddress.errors)
                groupAddressResult.errorMessage = convertToString(groupAddress.errors)

                val userAddressResult = com.tokopedia.logisticCommon.data.entity.address.UserAddress()
                userAddressResult.status = groupAddress.userAddress.status
                userAddressResult.address = groupAddress.userAddress.address
                userAddressResult.address2 = groupAddress.userAddress.address2
                userAddressResult.addressId = groupAddress.userAddress.addressId
                userAddressResult.addressName = groupAddress.userAddress.addressName
                userAddressResult.cityId = groupAddress.userAddress.cityId
                userAddressResult.cityName = groupAddress.userAddress.cityName
                userAddressResult.country = groupAddress.userAddress.country
                userAddressResult.districtId = groupAddress.userAddress.districtId
                userAddressResult.districtName = groupAddress.userAddress.districtName
                userAddressResult.latitude = groupAddress.userAddress.latitude
                userAddressResult.longitude = groupAddress.userAddress.longitude
                userAddressResult.phone = groupAddress.userAddress.phone
                userAddressResult.postalCode = groupAddress.userAddress.postalCode
                userAddressResult.provinceId = groupAddress.userAddress.provinceId
                userAddressResult.provinceName = groupAddress.userAddress.provinceName
                userAddressResult.receiverName = groupAddress.userAddress.receiverName
                userAddressResult.cornerId = groupAddress.userAddress.cornerId
                userAddressResult.isCorner = groupAddress.userAddress.isCorner
                userAddressResult.state = groupAddress.userAddress.state
                userAddressResult.stateDetail = groupAddress.userAddress.stateDetail
                groupAddressResult.userAddress = userAddressResult

                if (!isNullOrEmpty(groupAddress.groupShop)) {
                    val groupShopListResult: MutableList<GroupShop> = ArrayList()
                    for (groupShop in groupAddress.groupShop) {
                        val groupShopResult = GroupShop()
                        groupShopResult.isError = !isNullOrEmpty(groupShop.errors)
                        groupShopResult.errorMessage = convertToString(groupShop.errors)
                        groupShopResult.shippingId = groupShop.shippingId
                        groupShopResult.spId = groupShop.spId
                        groupShopResult.dropshipperName = groupShop.dropshiper.name
                        groupShopResult.dropshipperPhone = groupShop.dropshiper.telpNo
                        groupShopResult.isUseInsurance = groupShop.isInsurance
                        groupShopResult.cartString = groupShop.cartString
                        groupShopResult.isHasPromoList = groupShop.isHasPromoList
                        groupShopResult.isSaveStateFlag = groupShop.isSaveStateFlag
                        groupShopResult.isLeasingProduct = groupShop.vehicleLeasing.isLeasingProduct
                        groupShopResult.bookingFee = groupShop.vehicleLeasing.bookingFee
                        if (groupShop.listPromoCodes.isNotEmpty()) {
                            groupShopResult.listPromoCodes = groupShop.listPromoCodes
                        }
                        groupShopResult.isFulfillment = groupShop.isFulfillment
                        groupShopResult.fulfillmentId = groupShop.warehouse.warehouseId

                        val tokoCabangInfo = groupShop.tokoCabangInfo
                        groupShopResult.fulfillmentBadgeUrl = tokoCabangInfo.badgeUrl
                        groupShopResult.fulfillmentName = tokoCabangInfo.message

                        val shipmentInformation = groupShop.shipmentInformation
                        val freeShippingData = FreeShippingData()
                        freeShippingData.badgeUrl = shipmentInformation.freeShipping.badgeUrl
                        freeShippingData.eligible = shipmentInformation.freeShipping.eligible
                        val freeShippingExtra = shipmentInformation.freeShippingExtra
                        val freeShippingExtraData = FreeShippingData(freeShippingExtra.eligible, freeShippingExtra.badgeUrl)
                        val preorderData = PreorderData()
                        preorderData.duration = shipmentInformation.preorder.duration
                        preorderData.isPreorder = shipmentInformation.preorder.isPreorder
                        val shipmentInformationData = ShipmentInformationData()
                        shipmentInformationData.estimation = shipmentInformation.estimation
                        shipmentInformationData.shopLocation = shipmentInformation.shopLocation
                        shipmentInformationData.freeShipping = freeShippingData
                        shipmentInformationData.freeShippingExtra = freeShippingExtraData
                        shipmentInformationData.preorder = preorderData
                        groupShopResult.shipmentInformationData = shipmentInformationData
                        val shopResult = com.tokopedia.checkout.domain.model.cartshipmentform.Shop()
                        shopResult.shopId = groupShop.shop.shopId
                        shopResult.userId = groupShop.shop.userId
                        shopResult.shopName = groupShop.shop.shopName
                        shopResult.shopImage = groupShop.shop.shopImage
                        shopResult.shopUrl = groupShop.shop.shopUrl
                        shopResult.shopStatus = groupShop.shop.shopStatus
                        shopResult.isGold = groupShop.shop.goldMerchant.isGoldBadge
                        shopResult.isGoldBadge = groupShop.shop.goldMerchant.isGoldBadge
                        shopResult.isOfficial = groupShop.shop.isOfficial == 1
                        if (groupShop.shop.isOfficial == 1) {
                            shopResult.shopBadge = groupShop.shop.officialStore.osLogoUrl
                        } else if (groupShop.shop.goldMerchant.isGold == 1) {
                            shopResult.shopBadge = groupShop.shop.goldMerchant.goldMerchantLogoUrl
                        }
                        shopResult.isFreeReturns = groupShop.shop.isFreeReturns == 1
                        shopResult.addressId = groupShop.shop.addressId
                        shopResult.postalCode = groupShop.shop.postalCode
                        shopResult.latitude = groupShop.shop.latitude
                        shopResult.longitude = groupShop.shop.longitude
                        shopResult.districtId = groupShop.shop.districtId
                        shopResult.districtName = groupShop.shop.districtName
                        shopResult.origin = groupShop.shop.origin
                        shopResult.addressStreet = groupShop.shop.addressStreet
                        shopResult.provinceId = groupShop.shop.provinceId
                        shopResult.cityId = groupShop.shop.cityId
                        shopResult.cityName = groupShop.shop.cityName
                        shopResult.shopAlertMessage = groupShop.shop.shopAlertMessage
                        groupShopResult.shop = shopResult
                        if (!isNullOrEmpty(groupShop.shopShipments)) {
                            val shopShipmentListResult: MutableList<ShopShipment> = ArrayList()
                            for ((shipId, shipName, shipCode, shipLogo, shipProds, isDropshipEnabled) in groupShop.shopShipments) {
                                val shopShipmentResult = ShopShipment()
                                shopShipmentResult.isDropshipEnabled = isDropshipEnabled == 1
                                shopShipmentResult.shipCode = shipCode
                                shopShipmentResult.shipId = shipId
                                shopShipmentResult.shipLogo = shipLogo
                                shopShipmentResult.shipName = shipName
                                if (!isNullOrEmpty(shipProds)) {
                                    val shipProdListResult: MutableList<ShipProd> = ArrayList()
                                    for ((shipProdId, shipProdName, shipGroupName, shipGroupId, additionalFee, minimumWeight) in shipProds) {
                                        val shipProdResult = ShipProd()
                                        shipProdResult.additionalFee = additionalFee
                                        shipProdResult.minimumWeight = minimumWeight
                                        shipProdResult.shipGroupId = shipGroupId
                                        shipProdResult.shipGroupName = shipGroupName
                                        shipProdResult.shipProdId = shipProdId
                                        shipProdResult.shipProdName = shipProdName
                                        shipProdListResult.add(shipProdResult)
                                    }
                                    shopShipmentResult.shipProds = shipProdListResult
                                }
                                shopShipmentListResult.add(shopShipmentResult)
                            }
                            groupShopResult.shopShipments = shopShipmentListResult
                        }
                        if (!isNullOrEmpty(groupShop.products)) {
                            val productListResult: MutableList<Product> = ArrayList()
                            for ((errors, productId, cartId, productName, productPriceFmt, productPrice, productOriginalPrice, productWholesalePrice, productWholesalePriceFmt, productWeightFmt, productWeight, productCondition, productUrl, productReturnable, productIsFreeReturns, productIsPreorder, productCashback, productMinOrder, productInvenageValue, productSwitchInvenage, productPriceCurrency, productImageSrc200Square, productNotes, productQuantity, productMenuId, productFinsurance, productFcancelPartial, productCatId, productCatalogId, productCategory, productAlertMessage, productInformation, campaignId, pppDataMapping, productTrackerData, productPreorder, tradeInInfo, freeShipping, freeShippingExtra1, productTicker, variantDescriptionDetail) in groupShop.products) {
                                val productResult = Product()
                                val analyticsProductCheckoutData = AnalyticsProductCheckoutData()
                                analyticsProductCheckoutData.productId = productId.toString()
                                analyticsProductCheckoutData.productAttribution = productTrackerData.attribution
                                analyticsProductCheckoutData.productListName = productTrackerData.trackerListName
                                analyticsProductCheckoutData.productCategory = productCategory
                                analyticsProductCheckoutData.productCategoryId = productCatId.toString()
                                analyticsProductCheckoutData.productName = productName
                                analyticsProductCheckoutData.productPrice = productPrice.toString()
                                if (tradeInInfo.isValidTradeIn) {
                                    analyticsProductCheckoutData.productPrice = tradeInInfo.newDevicePrice.toString()
                                    productResult.productPrice = tradeInInfo.newDevicePrice.toLong()
                                }
                                analyticsProductCheckoutData.productShopId = groupShop.shop.shopId.toString()
                                analyticsProductCheckoutData.productShopName = groupShop.shop.shopName
                                analyticsProductCheckoutData.productShopType = generateShopType(groupShop.shop)
                                analyticsProductCheckoutData.productVariant = ""
                                analyticsProductCheckoutData.productBrand = ""
                                analyticsProductCheckoutData.productQuantity = productQuantity
                                analyticsProductCheckoutData.warehouseId = groupShop.warehouse.warehouseId.toString()
                                analyticsProductCheckoutData.productWeight = productWeight.toString()
                                if (groupAddressResult.userAddress != null) {
                                    analyticsProductCheckoutData.buyerAddressId = groupAddressResult.userAddress.addressId
                                }
                                analyticsProductCheckoutData.shippingDuration = ""
                                analyticsProductCheckoutData.courier = ""
                                analyticsProductCheckoutData.shippingPrice = ""
                                if (dataResult.cod != null) {
                                    analyticsProductCheckoutData.codFlag = dataResult.cod!!.isCod.toString()
                                } else {
                                    analyticsProductCheckoutData.codFlag = false.toString()
                                }
                                if (groupAddressResult.userAddress != null && isNotNullOrEmptyOrZero(groupAddressResult.userAddress.cornerId)) {
                                    analyticsProductCheckoutData.tokopediaCornerFlag = true.toString()
                                } else {
                                    analyticsProductCheckoutData.tokopediaCornerFlag = false.toString()
                                }
                                analyticsProductCheckoutData.isFulfillment = groupShop.isFulfillment.toString()
                                analyticsProductCheckoutData.isDiscountedPrice = productOriginalPrice > 0
                                analyticsProductCheckoutData.campaignId = campaignId
                                productResult.isError = !isNullOrEmpty(errors)
                                productResult.errorMessage = if (errors.size >= 1) errors[0] else ""
                                productResult.errorMessageDescription = if (errors.size >= 2) errors[1] else ""
                                productResult.productId = productId
                                productResult.cartId = cartId
                                productResult.productName = productName
                                productResult.productPriceFmt = productPriceFmt
                                productResult.productPrice = productPrice
                                productResult.productOriginalPrice = productOriginalPrice
                                productResult.productWholesalePrice = productWholesalePrice
                                productResult.productWholesalePriceFmt = productWholesalePriceFmt
                                productResult.productWeightFmt = productWeightFmt
                                productResult.productWeight = productWeight
                                productResult.productCondition = productCondition
                                productResult.productUrl = productUrl
                                productResult.isProductReturnable = productReturnable == 1
                                productResult.isProductIsFreeReturns = productIsFreeReturns == 1
                                productResult.isProductIsPreorder = productIsPreorder == 1
                                productResult.preOrderDurationDay = productPreorder.durationDay
                                if (productPreorder.durationText.length > 0) {
                                    productResult.productPreOrderInfo = "PO " + productPreorder.durationText
                                }
                                productResult.productCashback = productCashback
                                productResult.productMinOrder = productMinOrder
                                productResult.productInvenageValue = productInvenageValue
                                productResult.productSwitchInvenage = productSwitchInvenage
                                productResult.productPriceCurrency = productPriceCurrency
                                productResult.productImageSrc200Square = productImageSrc200Square
                                productResult.productNotes = productNotes
                                productResult.productQuantity = productQuantity
                                productResult.productMenuId = productMenuId
                                productResult.isProductFinsurance = productFinsurance == 1
                                productResult.isProductFcancelPartial = productFcancelPartial == 1
                                productResult.productCatId = productCatId
                                productResult.productCatalogId = productCatalogId
                                productResult.analyticsProductCheckoutData = analyticsProductCheckoutData
                                productResult.isShowTicker = productTicker.isShowTicker
                                productResult.tickerMessage = productTicker.message
                                if (freeShippingExtra1.eligible) {
                                    productResult.isFreeShippingExtra = true
                                }
                                if (freeShipping.eligible) {
                                    productResult.isFreeShipping = true
                                }
                                if (tradeInInfo.isValidTradeIn) {
                                    val tradeInInfoData = TradeInInfoData()
                                    tradeInInfoData.isValidTradeIn = tradeInInfo.isValidTradeIn
                                    tradeInInfoData.newDevicePrice = tradeInInfo.newDevicePrice
                                    tradeInInfoData.newDevicePriceFmt = tradeInInfo.newDevicePriceFmt
                                    tradeInInfoData.oldDevicePrice = tradeInInfo.oldDevicePrice
                                    tradeInInfoData.oldDevicePriceFmt = tradeInInfo.oldDevicePriceFmt
                                    tradeInInfoData.isDropOffEnable = tradeInInfo.isDropOffEnable
                                    tradeInInfoData.deviceModel = tradeInInfo.deviceModel
                                    tradeInInfoData.diagnosticId = tradeInInfo.diagnosticId
                                    productResult.tradeInInfoData = tradeInInfoData
                                }
                                if (!isDisablePPP) {
                                    if (pppDataMapping.protectionAvailable) {
                                        val purchaseProtectionPlanData = PurchaseProtectionPlanData()
                                        purchaseProtectionPlanData.isProtectionAvailable = pppDataMapping.protectionAvailable
                                        purchaseProtectionPlanData.protectionLinkText = pppDataMapping.protectionLinkText
                                        purchaseProtectionPlanData.protectionLinkUrl = pppDataMapping.protectionLinkUrl
                                        purchaseProtectionPlanData.isProtectionOptIn = pppDataMapping.protectionOptIn
                                        purchaseProtectionPlanData.protectionPrice = pppDataMapping.protectionPrice
                                        purchaseProtectionPlanData.protectionPricePerProduct = pppDataMapping.protectionPricePerProduct
                                        purchaseProtectionPlanData.protectionSubtitle = pppDataMapping.protectionSubtitle
                                        purchaseProtectionPlanData.protectionTitle = pppDataMapping.protectionTitle
                                        purchaseProtectionPlanData.protectionTypeId = pppDataMapping.protectionTypeId
                                        purchaseProtectionPlanData.isProtectionCheckboxDisabled = pppDataMapping.protectionCheckboxDisabled
                                        productResult.purchaseProtectionPlanData = purchaseProtectionPlanData
                                    }
                                }
                                productResult.variant = variantDescriptionDetail.variantDescription
                                productResult.productAlertMessage = productAlertMessage
                                productResult.productInformation = productInformation
                                val promoSAFResponse = shipmentAddressFormDataResponse.promoSAFResponse
                                if (promoSAFResponse.lastApply != null && promoSAFResponse.lastApply!!.data != null && promoSAFResponse.lastApply!!.data!!.trackingDetails != null) {
                                    val trackingDetailsItems = promoSAFResponse.lastApply!!.data!!.trackingDetails
                                    if (trackingDetailsItems!!.size > 0) {
                                        for (trackingDetail in trackingDetailsItems) {
                                            if (trackingDetail!!.productId != null && trackingDetail.productId == productResult.productId) {
                                                analyticsProductCheckoutData.promoCode = trackingDetail.promoCodesTracking
                                                analyticsProductCheckoutData.promoDetails = trackingDetail.promoDetailsTracking
                                            }
                                        }
                                    }
                                }
                                productListResult.add(productResult)
                            }
                            groupShopResult.products = productListResult
                        }
                        groupShopListResult.add(groupShopResult)
                    }
                    groupAddressResult.groupShop = groupShopListResult
                }
                groupAddressListResult.add(groupAddressResult)
            }
            dataResult.groupAddress = groupAddressListResult
            dataResult.isHasError = checkCartHasError(dataResult)
        }
        dataResult.popUpMessage = shipmentAddressFormDataResponse.popUpMessage
        return dataResult
    }

    private fun mapPromoLastApply(promoData: Data?): LastApplyUiModel {
        return LastApplyUiModel().apply {
            codes = mapPromoGlobalCodes(promoData)
            voucherOrders = mapPromoVoucherOrders(promoData)
            additionalInfo = mapLastApplyAdditionalInfoUiModel(promoData?.additionalInfo)
            message = mapLastApplyMessageUiModel(promoData?.message)
            listRedPromos = mapListRedPromos(promoData)
            listAllPromoCodes = mapListAllPromos(promoData)
        }
    }

    private fun mapListAllPromos(promoData: Data?): List<String> {
        val listAllPromoCodes = arrayListOf<String>()
        if (promoData?.codes != null) {
            promoData.codes?.forEach {
                it?.let {
                    listAllPromoCodes.add(it)
                }
            }
        }
        promoData?.voucherOrders?.forEach {
            it?.code?.let {
                listAllPromoCodes.add(it)
            }
        }
        return listAllPromoCodes
    }

    private fun mapListRedPromos(promoData: Data?): List<String> {
        val listRedStates = arrayListOf<String>()
        if (promoData?.message?.state.equals(CheckoutConstant.STATE_RED, ignoreCase = true)) {
            promoData?.codes?.forEach {
                it?.let { code ->
                    listRedStates.add(code)
                }
            }
        }
        promoData?.voucherOrders?.forEach {
            it?.let {
                if (it.message?.state.equals(CheckoutConstant.STATE_RED, ignoreCase = true)) {
                    it.code?.let {
                        listRedStates.add(it)
                    }
                }
            }
        }
        return listRedStates
    }

    private fun mapLastApplyAdditionalInfoUiModel(additionalInfo: AdditionalInfo?): LastApplyAdditionalInfoUiModel {
        return LastApplyAdditionalInfoUiModel().apply {
            emptyCartInfo = mapLastApplyEmptyCartInfoUiModel(additionalInfo?.cartEmptyInfo)
            errorDetail = mapLastApplyErrorDetailUiModel(additionalInfo?.errorDetail)
            messageInfo = mapLastApplyMessageInfoUiModel(additionalInfo?.messageInfo)
            promoSpIds = mapPromoSpId(additionalInfo)
            usageSummaries = mapLastApplyUsageSummariesUiModel(additionalInfo?.listUsageSummaries)
        }
    }

    private fun mapLastApplyUsageSummariesUiModel(listUsageSummaries: List<UsageSummaries>?): List<LastApplyUsageSummariesUiModel> {
        val tmplistUsageSummaries = arrayListOf<LastApplyUsageSummariesUiModel>()
        listUsageSummaries?.forEach {
            tmplistUsageSummaries.add(
                    LastApplyUsageSummariesUiModel().apply {
                        description = it.desc ?: ""
                        type = it.type ?: ""
                        amountStr = it.amountStr ?: ""
                        amount = it.amount ?: 0
                        currencyDetailsStr = it.currencyDetailsStr
                    }
            )
        }
        return tmplistUsageSummaries
    }

    private fun mapLastApplyMessageInfoUiModel(messageInfo: MessageInfo?): LastApplyMessageInfoUiModel {
        return LastApplyMessageInfoUiModel().apply {
            detail = messageInfo?.detail ?: ""
            message = messageInfo?.message ?: ""
        }
    }

    private fun mapLastApplyErrorDetailUiModel(errorDetail: ErrorDetail?): LastApplyErrorDetailUiModel {
        return LastApplyErrorDetailUiModel().apply {
            message = errorDetail?.message ?: ""
        }
    }

    private fun mapLastApplyEmptyCartInfoUiModel(cartEmptyInfo: CartEmptyInfo?): LastApplyEmptyCartInfoUiModel {
        return LastApplyEmptyCartInfoUiModel().apply {
            detail = cartEmptyInfo?.detail ?: ""
            imgUrl = cartEmptyInfo?.imageUrl ?: ""
            message = cartEmptyInfo?.message ?: ""
        }
    }

    private fun mapPromoVoucherOrders(promoData: Data?): List<LastApplyVoucherOrdersItemUiModel> {
        val listVoucherOrdersUiModel = arrayListOf<LastApplyVoucherOrdersItemUiModel>()
        promoData?.voucherOrders?.forEach {
            it?.let { voucherOrdersItem ->
                listVoucherOrdersUiModel.add(
                        LastApplyVoucherOrdersItemUiModel().apply {
                            code = voucherOrdersItem.code ?: ""
                            uniqueId = voucherOrdersItem.uniqueId ?: ""
                            message = mapLastApplyMessageUiModel(voucherOrdersItem.message)
                        }
                )
            }
        }

        return listVoucherOrdersUiModel
    }

    private fun mapLastApplyMessageUiModel(message: Message?): LastApplyMessageUiModel {
        return LastApplyMessageUiModel().apply {
            color = message?.color ?: ""
            state = message?.state ?: ""
            text = message?.text ?: ""
        }
    }

    private fun mapPromoGlobalCodes(promoData: Data?): MutableList<String> {
        val tmpCodes = mutableListOf<String>()
        promoData?.codes?.forEach {
            it?.let {
                tmpCodes.add(it)
            }
        }
        return tmpCodes
    }

    private fun mapPromoCheckoutErrorDefault(errorDefault: ErrorDefault?): PromoCheckoutErrorDefault {
        return PromoCheckoutErrorDefault().apply {
            title = errorDefault?.title ?: ""
            desc = errorDefault?.description ?: ""
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

    private fun mapCod(shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse): CodModel {
        val cod = CodModel()
        cod.isCod = shipmentAddressFormDataResponse.cod.isCod
        cod.counterCod = shipmentAddressFormDataResponse.cod.counterCod
        return cod
    }

    private fun mapDonation(shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse): Donation {
        val donation = Donation()
        donation.title = shipmentAddressFormDataResponse.donation.title
        donation.description = shipmentAddressFormDataResponse.donation.description
        donation.nominal = shipmentAddressFormDataResponse.donation.nominal
        donation.isChecked = shipmentAddressFormDataResponse.isDonationCheckboxStatus
        return donation
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
                        val mvcShippingBenefitUiModel = MvcShippingBenefitUiModel()
                        mvcShippingBenefitUiModel.benefitAmount = mvcShippingBenefit.benefitAmount
                        mvcShippingBenefitUiModel.spId = mvcShippingBenefit.spId
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
                    break
                }
                var totalProductError = 0
                var defaultErrorMessage = ""
                for ((isError, errorMessage) in groupShop.products) {
                    if (isError || !isNullOrEmpty(errorMessage)) {
                        hasError = true
                        totalProductError++
                        if (isNullOrEmpty(defaultErrorMessage)) {
                            defaultErrorMessage = errorMessage
                        }
                    }
                }
                if (totalProductError == groupShop.products.size) {
                    groupShop.isError = true
                    groupShop.errorMessage = defaultErrorMessage
                    for (product in groupShop.products) {
                        product.isError = false
                        product.errorMessage = ""
                    }
                }
            }
        }
        return hasError
    }

    private fun generateShopType(shop: Shop): String {
        return if (shop.isOfficial == 1) SHOP_TYPE_OFFICIAL_STORE else if (shop.goldMerchant.isGoldBadge) SHOP_TYPE_GOLD_MERCHANT else SHOP_TYPE_REGULER
    }

    companion object {
        private const val SHOP_TYPE_OFFICIAL_STORE = "official_store"
        private const val SHOP_TYPE_GOLD_MERCHANT = "gold_merchant"
        private const val SHOP_TYPE_REGULER = "reguler"
    }
}