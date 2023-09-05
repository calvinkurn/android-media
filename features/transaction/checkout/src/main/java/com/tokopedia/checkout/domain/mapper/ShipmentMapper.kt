package com.tokopedia.checkout.domain.mapper

import com.tokopedia.checkout.data.model.response.shipmentaddressform.AddOnWording
import com.tokopedia.checkout.data.model.response.shipmentaddressform.AddOnsProduct
import com.tokopedia.checkout.data.model.response.shipmentaddressform.CampaignTimer
import com.tokopedia.checkout.data.model.response.shipmentaddressform.Cod
import com.tokopedia.checkout.data.model.response.shipmentaddressform.CrossSellBottomSheet
import com.tokopedia.checkout.data.model.response.shipmentaddressform.CrossSellInfoData
import com.tokopedia.checkout.data.model.response.shipmentaddressform.CrossSellOrderSummary
import com.tokopedia.checkout.data.model.response.shipmentaddressform.FreeShipping
import com.tokopedia.checkout.data.model.response.shipmentaddressform.FreeShippingGeneral
import com.tokopedia.checkout.data.model.response.shipmentaddressform.NewUpsell
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ScheduleDelivery
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentAddressFormDataResponse
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentInformation
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentPlatformFee
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipmentSummaryAddOn
import com.tokopedia.checkout.data.model.response.shipmentaddressform.SubtotalAddOn
import com.tokopedia.checkout.data.model.response.shipmentaddressform.TradeInInfo
import com.tokopedia.checkout.data.model.response.shipmentaddressform.Upsell
import com.tokopedia.checkout.domain.model.cartshipmentform.AddressData
import com.tokopedia.checkout.domain.model.cartshipmentform.AddressesData
import com.tokopedia.checkout.domain.model.cartshipmentform.CampaignTimerUi
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.CheckoutCoachmarkPlusData
import com.tokopedia.checkout.domain.model.cartshipmentform.CourierSelectionErrorData
import com.tokopedia.checkout.domain.model.cartshipmentform.Donation
import com.tokopedia.checkout.domain.model.cartshipmentform.EpharmacyData
import com.tokopedia.checkout.domain.model.cartshipmentform.FreeShippingData
import com.tokopedia.checkout.domain.model.cartshipmentform.FreeShippingGeneralData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupAddress
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop.Companion.UI_GROUP_TYPE_OWOC
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShopV2
import com.tokopedia.checkout.domain.model.cartshipmentform.NewUpsellData
import com.tokopedia.checkout.domain.model.cartshipmentform.PreorderData
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.checkout.domain.model.cartshipmentform.ScheduleDeliveryData
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentInformationData
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentPlatformFeeData
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentSubtotalAddOnData
import com.tokopedia.checkout.domain.model.cartshipmentform.ShipmentSummaryAddOnData
import com.tokopedia.checkout.domain.model.cartshipmentform.Shop
import com.tokopedia.checkout.domain.model.cartshipmentform.TradeInInfoData
import com.tokopedia.checkout.domain.model.cartshipmentform.UpsellData
import com.tokopedia.checkout.view.uimodel.CrossSellBottomSheetModel
import com.tokopedia.checkout.view.uimodel.CrossSellInfoModel
import com.tokopedia.checkout.view.uimodel.CrossSellModel
import com.tokopedia.checkout.view.uimodel.CrossSellOrderSummaryModel
import com.tokopedia.checkout.view.uimodel.EgoldAttributeModel
import com.tokopedia.checkout.view.uimodel.EgoldTieringModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticCommon.data.entity.address.UserAddressTokoNow
import com.tokopedia.logisticcart.shipping.model.AnalyticsProductCheckoutData
import com.tokopedia.logisticcart.shipping.model.CodModel
import com.tokopedia.logisticcart.shipping.model.ShipProd
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticcart.shipping.model.ShopTypeInfoData
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductBottomSheetModel
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataItemModel
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataModel
import com.tokopedia.purchase_platform.common.feature.coachmarkplus.CoachmarkPlusResponse
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.model.EthicalDrugDataModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.EpharmacyEnablerResponse
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.EthicalDrugResponse
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.ImageUploadResponse
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingBottomSheetModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingButtonModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingDataModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingMetadataItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingNoteItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingProductItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnGiftingTickerModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.response.AddOnGiftingResponse
import com.tokopedia.purchase_platform.common.feature.gifting.data.response.Button
import com.tokopedia.purchase_platform.common.feature.gifting.data.response.PopUp
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnWordingData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.ButtonData
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.PopUpData
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.AdditionalInfo
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.CartEmptyInfo
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.Data
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.ErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.ErrorDetail
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.Message
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.MessageInfo
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.PromoSAFResponse
import com.tokopedia.purchase_platform.common.feature.promo.domain.model.UsageSummaries
import com.tokopedia.purchase_platform.common.feature.promo.view.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyAdditionalInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyBebasOngkirInfo
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyEmptyCartInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyErrorDetailUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageInfoUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyMessageUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyUsageSummariesUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply.LastApplyVoucherOrdersItemUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.MvcShippingBenefitUiModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.PromoSpIdUiModel
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.data.PurchaseProtectionPlanDataResponse
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import javax.inject.Inject
import com.tokopedia.checkout.data.model.response.shipmentaddressform.GroupAddress as GroupAddressResponse
import com.tokopedia.checkout.data.model.response.shipmentaddressform.GroupShop as GroupShopResponse
import com.tokopedia.checkout.data.model.response.shipmentaddressform.GroupShopV2 as GroupShopV2Response
import com.tokopedia.checkout.data.model.response.shipmentaddressform.Product as ProductResponse
import com.tokopedia.checkout.data.model.response.shipmentaddressform.ShipProd as ShipProdResponse
import com.tokopedia.checkout.data.model.response.shipmentaddressform.Shop as ShopResponse
import com.tokopedia.checkout.data.model.response.shipmentaddressform.UserAddress as UserAddressResponse

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
            epharmacyData = mapEpharmacyData(shipmentAddressFormDataResponse.imageUpload)
            keroDiscomToken = shipmentAddressFormDataResponse.keroDiscomToken
            keroToken = shipmentAddressFormDataResponse.keroToken
            keroUnixTime = shipmentAddressFormDataResponse.keroUnixTime
            isHidingCourier = shipmentAddressFormDataResponse.hideCourier
            isBlackbox = shipmentAddressFormDataResponse.isBlackbox == 1
            errorCode = shipmentAddressFormDataResponse.errorCode
            isError = shipmentAddressFormDataResponse.errors.isNotEmpty()
            errorMessage = shipmentAddressFormDataResponse.errors.joinToString()
            isShowOnboarding = shipmentAddressFormDataResponse.isShowOnboarding
            isIneligiblePromoDialogEnabled =
                shipmentAddressFormDataResponse.isIneligiblePromoDialogEnabled
            isOpenPrerequisiteSite = shipmentAddressFormDataResponse.isOpenPrerequisiteSite
            isEligibleNewShippingExperience =
                shipmentAddressFormDataResponse.isEligibleNewShippingExperience
            addressesData = mapAddressesData(shipmentAddressFormDataResponse)
            cod = mapCod(shipmentAddressFormDataResponse.cod)
            campaignTimerUi = mapCampaignTimer(shipmentAddressFormDataResponse.campaignTimer)
            lastApplyData =
                mapPromoLastApply(shipmentAddressFormDataResponse.promoSAFResponse.lastApply.data)
            promoCheckoutErrorDefault =
                mapPromoCheckoutErrorDefault(shipmentAddressFormDataResponse.promoSAFResponse.errorDefault)
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
            addOnWording = mapAddOnWording(shipmentAddressFormDataResponse.addOnWording)
            upsell = mapUpsell(shipmentAddressFormDataResponse.upsell)
            newUpsell = mapUpsell(shipmentAddressFormDataResponse.newUpsell)
            cartData = shipmentAddressFormDataResponse.cartData
            coachmarkPlus = mapCoachmarkPlus(shipmentAddressFormDataResponse.coachmark)
            isUsingDdp = shipmentAddressFormDataResponse.dynamicDataPassing.isDdp
            dynamicData = shipmentAddressFormDataResponse.dynamicDataPassing.dynamicData
            shipmentPlatformFee =
                mapPlatformFee(shipmentAddressFormDataResponse.shipmentPlatformFee)
            listSummaryAddons = mapSummaryAddOn(shipmentAddressFormDataResponse.listSummaryAddOns)
        }
    }

    private fun mapEpharmacyData(imageUpload: ImageUploadResponse): EpharmacyData {
        return EpharmacyData(
            showImageUpload = imageUpload.showImageUpload,
            uploadText = imageUpload.text,
            leftIconUrl = imageUpload.leftIconUrl,
            checkoutId = imageUpload.checkoutId,
            frontEndValidation = imageUpload.frontEndValidation,
            consultationFlow = imageUpload.consultationFlow,
            rejectedWording = imageUpload.rejectedWording
        )
    }

    private fun mapTickerData(it: Ticker): TickerData {
        return TickerData().apply {
            id = it.id
            message = it.message
            page = it.page
            title = ""
        }
    }

    private fun mapGroupAddresses(
        shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse,
        isDisablePPP: Boolean
    ): MutableList<GroupAddress> {
        val groupAddressListResult = arrayListOf<GroupAddress>()
        for (groupAddress in shipmentAddressFormDataResponse.groupAddress) {
            groupAddressListResult.add(
                GroupAddress().apply {
                    isError =
                        groupAddress.errors.isNotEmpty() || shipmentAddressFormDataResponse.errorTicker.isNotEmpty()
                    errorMessage = groupAddress.errors.joinToString()
                    userAddress = mapUserAddress(groupAddress)
                    groupShop =
                        mapGroupShops(groupAddress, shipmentAddressFormDataResponse, isDisablePPP)
                }
            )
        }
        return groupAddressListResult
    }

    private fun mapGroupShops(
        groupAddress: GroupAddressResponse,
        shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse,
        isDisablePPP: Boolean
    ): MutableList<GroupShop> {
        val groupShopListResult = arrayListOf<GroupShop>()
        groupAddress.groupShop.forEach {
            groupShopListResult.add(
                GroupShop(
                    groupType = it.groupType,
                    uiGroupType = it.uiGroupType,
                    groupInfoName = it.groupInformation.name,
                    groupInfoBadgeUrl = it.groupInformation.badgeUrl,
                    groupInfoDescription = it.groupInformation.description,
                    groupInfoDescriptionBadgeUrl = it.groupInformation.descriptionBadgeUrl
                ).apply {
                    isError =
                        it.errors.isNotEmpty() || shipmentAddressFormDataResponse.errorTicker.isNotEmpty()
                    errorMessage =
                        if (shipmentAddressFormDataResponse.errorTicker.isNotEmpty()) "" else it.errors.joinToString()
                    hasUnblockingError = it.unblockingErrors.isNotEmpty()
                    unblockingErrorMessage = it.unblockingErrors.joinToString()
                    shippingId = it.shippingId
                    spId = it.spId
                    boCode = it.boCode
                    boUniqueId = if (it.boCode.isNotEmpty()) {
                        shipmentAddressFormDataResponse.promoSAFResponse.lastApply.data.voucherOrders.first { voucher -> voucher.code == it.boCode && voucher.cartStringGroup == it.cartString }.uniqueId
                    } else {
                        ""
                    }
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
                    fulfillmentBadgeUrl = it.groupInformation.descriptionBadgeUrl
                    fulfillmentName = it.groupInformation.description
                    shipmentInformationData = mapShipmentInformationData(it.shipmentInformation)
                    val mapGroupShopV2List = mapGroupShopV2List(
                        it,
                        it.groupShopV2,
                        groupAddress,
                        shipmentAddressFormDataResponse,
                        isDisablePPP
                    )
                    groupShopData = mapGroupShopV2List.first
                    firstProductErrorIndex = mapGroupShopV2List.second
                    addOns = mapAddOnsGiftingData(it.addOns)
                    shopShipments = mapShopShipments(it.shopShipments)
                    isDisableChangeCourier = it.isDisableChangeCourier
                    autoCourierSelection = it.autoCourierSelection
                    boMetadata = it.boMetadata
                    courierSelectionErrorData = CourierSelectionErrorData(
                        it.courierSelectionError.title,
                        it.courierSelectionError.description
                    )
                    scheduleDelivery = mapScheduleDelivery(it.scheduledDelivery)
                    ratesValidationFlow = it.ratesValidationFlow
                    listSubtotalAddOn = mapSubtotalAddOn(it.listSubtotalAddOns)
                }
            )
        }
        return groupShopListResult
    }

    private fun mapProducts(
        groupShop: GroupShopResponse,
        groupShopV2: GroupShopV2Response,
        groupAddress: GroupAddressResponse,
        shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse,
        isDisablePPP: Boolean,
        shopTypeInfoData: ShopTypeInfoData
    ): Pair<MutableList<Product>, Int> {
        val productListResult = arrayListOf<Product>()
        var firstErrorIndex = 0
        var hasError = false
        groupShopV2.cartDetails.forEachIndexed { index, cartDetail ->
            cartDetail.products.forEachIndexed { productIndex, product ->
                val productResult = Product(
                    shouldShowShopInfo = groupShop.uiGroupType == UI_GROUP_TYPE_OWOC && index == 0 && productIndex == 0,
                    shopName = groupShopV2.shop.shopName,
                    shopTypeInfoData = shopTypeInfoData,
                    originWarehouseIds = product.originWarehouseIds
                ).apply {
                    analyticsProductCheckoutData = mapAnalyticsProductCheckoutData(
                        groupShop,
                        product,
                        groupAddress.userAddress,
                        groupShopV2,
                        shipmentAddressFormDataResponse.cod,
                        shipmentAddressFormDataResponse.promoSAFResponse,
                        shopTypeInfoData
                    )
                    if (product.tradeInInfo.isValidTradeIn) {
                        productPrice = product.tradeInInfo.newDevicePrice
                    }
                    isError = product.errors.isNotEmpty() ||
                        shipmentAddressFormDataResponse.errorTicker.isNotEmpty() ||
                        (cartDetail.bundleDetail.bundleId.isNotBlankOrZero() && cartDetail.errors.isNotEmpty())
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
                    errorMessageDescription =
                        if (shipmentAddressFormDataResponse.errorTicker.isNotEmpty()) "" else if (product.errors.size >= 2) product.errors[1] else ""
                    if (isError && !hasError) {
                        hasError = true
                    } else if (!hasError) {
                        firstErrorIndex += 1
                    }
                    productId = product.productId
                    cartId = product.cartId
                    productName = product.productName
                    productPrice = product.productPrice
                    productOriginalPrice = product.productOriginalPrice
                    productWholesalePrice = product.productWholesalePrice
                    productWeightFmt = product.productWeightFmt
                    productWeight = product.productWeight
                    productWeightActual = product.productWeightActual
                    isProductIsFreeReturns = product.productIsFreeReturns == 1
                    isProductIsPreorder = product.productIsPreorder == 1
                    preOrderDurationDay = product.productPreorder.durationDay
                    if (product.productPreorder.durationText.isNotEmpty()) {
                        productPreOrderInfo = "PO " + product.productPreorder.durationText
                    }
                    productCashback = product.productCashback
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
                    freeShippingName = product.freeShippingGeneral.boName
                    if (product.tradeInInfo.isValidTradeIn) {
                        tradeInInfoData = mapTradeInInfoData(product.tradeInInfo)
                    }
                    if (!isDisablePPP && product.purchaseProtectionPlanDataResponse.protectionAvailable) {
                        purchaseProtectionPlanData =
                            mapPurchaseProtectionData(product.purchaseProtectionPlanDataResponse)
                    }
                    variantParentId = product.productVariantsResponse.parentId
                    variant = product.variantDescriptionDetail.variantDescription
                    productAlertMessage = product.productAlertMessage
                    productInformation = product.productInformation
                    if (cartDetail.bundleDetail.bundleId.isNotBlankOrZero()) {
                        isBundlingItem = true
                        bundlingItemPosition =
                            if (cartDetail.products.firstOrNull()?.productId == productId) {
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
                    if (cartDetail.cartDetailInfo.cartDetailType.lowercase() == CART_DETAIL_TYPE_BMGM) {
                        isBmgmItem = true
                        bmgmOfferId = cartDetail.cartDetailInfo.bmgmData.offerId
                        bmgmIconUrl = cartDetail.cartDetailInfo.bmgmData.offerIcon
                        bmgmOfferName = cartDetail.cartDetailInfo.bmgmData.offerName
                        bmgmOfferMessage = cartDetail.cartDetailInfo.bmgmData.offerMessage
                        bmgmOfferStatus = cartDetail.cartDetailInfo.bmgmData.offerStatus
                        bmgmItemPosition = if (cartDetail.products.firstOrNull()?.productId == productId) {
                            BMGM_ITEM_HEADER
                        } else {
                            BMGM_ITEM_DEFAULT
                        }
                        bmgmTotalDiscount = cartDetail.cartDetailInfo.bmgmData.totalDiscount
                        bmgmTierProductList = cartDetail.cartDetailInfo.bmgmData.tierProductList
                    } else {
                        isBmgmItem = false
                    }
                    addOnGiftingProduct = mapAddOnsGiftingData(product.addOns)
                    ethicalDrugs = mapEthicalDrugData(product.ethicalDrugResponse)
                    addOnProduct = mapAddOnsProductData(product.addOnsProduct, product.productQuantity)
                    campaignId = product.campaignId
                }
                productListResult.add(productResult)
            }
        }
        return productListResult to if (hasError) {
            firstErrorIndex
        } else {
            -1
        }
    }

    private fun mapAnalyticsProductCheckoutData(
        groupShop: GroupShopResponse,
        product: ProductResponse,
        userAddress: UserAddressResponse,
        groupShopV2: GroupShopV2Response,
        cod: Cod,
        promoSAFResponse: PromoSAFResponse,
        shopTypeInfoData: ShopTypeInfoData
    ): AnalyticsProductCheckoutData {
        var promoCode = ""
        var promoDetails = ""
        promoSAFResponse.lastApply.data.trackingDetails.forEach {
            if (it.productId == product.productId) {
                promoCode = it.promoCodesTracking
                promoDetails = it.promoDetailsTracking
            }
        }
        return AnalyticsProductCheckoutData(
            productId = product.productId.toString(),
            productAttribution = product.productTrackerData.attribution,
            productListName = product.productTrackerData.trackerListName,
            productCategory = product.productCategory,
            productCategoryId = product.productCatId.toString(),
            productName = product.productName,
            productPrice = if (product.tradeInInfo.isValidTradeIn) product.tradeInInfo.newDevicePrice.toString() else product.productPrice.toString(),
            productShopId = groupShopV2.shop.shopId.toString(),
            productShopName = groupShopV2.shop.shopName,
            productShopType = shopTypeInfoData.shopType,
            productVariant = "",
            productBrand = "",
            productQuantity = product.productQuantity,
            warehouseId = groupShop.warehouse.warehouseId.toString(),
            productWeight = product.productWeight.toString(),
            buyerAddressId = userAddress.addressId,
            shippingDuration = "",
            courier = "",
            shippingPrice = "",
            codFlag = cod.isCod.toString(),
            tokopediaCornerFlag = if (userAddress.cornerId.isNotBlankOrZero()) {
                true.toString()
            } else {
                false.toString()
            },
            isFulfillment = groupShop.isFulfillment.toString(),
            isDiscountedPrice = product.productOriginalPrice > 0,
            campaignId = product.campaignId,
            promoCode = promoCode,
            promoDetails = promoDetails
        )
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

    private fun mapShipProds(shipProds: List<ShipProdResponse>): List<ShipProd> {
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

    private fun mapGroupShopV2List(
        groupShop: GroupShopResponse,
        groupShopV2List: List<GroupShopV2Response>,
        groupAddress: GroupAddressResponse,
        shipmentAddressFormDataResponse: ShipmentAddressFormDataResponse,
        isDisablePPP: Boolean
    ): Pair<List<GroupShopV2>, Int> {
        var firstProductErrorIndex = -1
        var hasErrorProduct = false
        return groupShopV2List.map {
            val shop = mapShopData(it.shop)
            val mapProducts = mapProducts(
                groupShop,
                it,
                groupAddress,
                shipmentAddressFormDataResponse,
                isDisablePPP,
                shop.shopTypeInfoData
            )
            val products = mapProducts.first
            if (!hasErrorProduct) {
                if (mapProducts.second > -1) {
                    hasErrorProduct = true
                    if (firstProductErrorIndex == -1) {
                        firstProductErrorIndex = mapProducts.second
                    } else {
                        firstProductErrorIndex += mapProducts.second
                    }
                } else if (firstProductErrorIndex == -1) {
                    firstProductErrorIndex = products.size
                } else {
                    firstProductErrorIndex += products.size
                }
            }
            GroupShopV2(
                it.cartStringOrder,
                shop,
                products
            )
        } to if (hasErrorProduct) firstProductErrorIndex else -1
    }

    private fun mapShopData(shop: ShopResponse): Shop {
        return Shop().apply {
            shopId = shop.shopId
            shopName = shop.shopName
            shopTypeInfoData = mapShopTypeInfo(shop)
            postalCode = shop.postalCode
            latitude = shop.latitude
            longitude = shop.longitude
            districtId = shop.districtId
            shopAlertMessage = shop.shopAlertMessage
            isTokoNow = shop.isTokoNow
            shopTickerTitle = shop.shopTickerTitle
            shopTicker = shop.shopTicker
            enablerLabel = mapEnablerLabel(shop.enabler)
        }
    }

    private fun mapEnablerLabel(enablerResponse: EpharmacyEnablerResponse): String {
        return if (enablerResponse.showLabel) {
            enablerResponse.labelName
        } else {
            ""
        }
    }

    private fun mapAddOnsGiftingData(addOns: AddOnGiftingResponse): AddOnGiftingDataModel {
        return AddOnGiftingDataModel().apply {
            status = addOns.status
            addOnsButtonModel = mapAddOnButton(addOns.addOnButton)
            addOnsBottomSheetModel = mapAddOnBottomSheet(addOns.addOnBottomsheet)
            addOnsDataItemModelList = mapAddOnListData(addOns.addOnData)
        }
    }

    private fun mapAddOnsProductData(addOn: AddOnsProduct, productQuantity: Int): AddOnProductDataModel {
        return AddOnProductDataModel().apply {
            iconUrl = addOn.iconUrl
            title = addOn.title
            bottomsheet = mapAddOnProductBottomSheet(addOn.bottomsheet)
            listAddOnProductData = mapAddOnProductListData(addOn.addOnsDataList, productQuantity)
        }
    }

    private fun mapAddOnProductBottomSheet(addOnBottomSheet: AddOnsProduct.AddOnsProductBottomSheet): AddOnProductBottomSheetModel {
        return AddOnProductBottomSheetModel().apply {
            title = addOnBottomSheet.title
            applink = addOnBottomSheet.applink
            isShown = addOnBottomSheet.isShown
        }
    }

    private fun mapAddOnProductListData(addOnsDataList: List<AddOnsProduct.AddOnsData>, productQuantity: Int): ArrayList<AddOnProductDataItemModel> {
        val listAddOnDataItem = arrayListOf<AddOnProductDataItemModel>()
        addOnsDataList.forEach { item ->
            listAddOnDataItem.add(
                AddOnProductDataItemModel().apply {
                    id = item.id
                    price = item.price
                    infoLink = item.infoLink
                    name = item.name
                    status = item.status
                    type = item.type
                    qty = productQuantity
                    uniqueId = item.uniqueId
                    iconUrl = item.iconUrl
                }
            )
        }
        return listAddOnDataItem
    }
    private fun mapSubtotalAddOn(subtotalAddOns: List<SubtotalAddOn>): List<ShipmentSubtotalAddOnData> {
        val listSubtotal = arrayListOf<ShipmentSubtotalAddOnData>()

        subtotalAddOns.forEach {
            val shipmentSubtotalAddOnData = ShipmentSubtotalAddOnData(
                wording = it.wording,
                type = it.type
            )
            listSubtotal.add(shipmentSubtotalAddOnData)
        }

        return listSubtotal
    }

    private fun mapEthicalDrugData(addOns: EthicalDrugResponse): EthicalDrugDataModel {
        return EthicalDrugDataModel(
            needPrescription = addOns.needPrescription,
            text = addOns.text,
            iconUrl = addOns.iconUrl
        )
    }

    private fun mapAddOnListData(addOnData: List<AddOnGiftingResponse.AddOnDataItem>): MutableList<AddOnGiftingDataItemModel> {
        val listAddOnDataItem = arrayListOf<AddOnGiftingDataItemModel>()
        addOnData.forEach { item ->
            listAddOnDataItem.add(
                AddOnGiftingDataItemModel().apply {
                    addOnPrice = item.addOnPrice
                    addOnId = item.addOnId
                    addOnUniqueId = item.addOnUniqueId
                    addOnMetadata = mapAddOnMetadata(item.addOnMetadata)
                    addOnQty = item.addOnQty
                }
            )
        }
        return listAddOnDataItem
    }

    private fun mapAddOnMetadata(addOnMetadata: AddOnGiftingResponse.AddOnDataItem.AddOnMetadata): AddOnGiftingMetadataItemModel {
        return AddOnGiftingMetadataItemModel(mapAddOnNote(addOnMetadata.addOnNote))
    }

    private fun mapAddOnNote(addOnNote: AddOnGiftingResponse.AddOnDataItem.AddOnMetadata.AddOnNote): AddOnGiftingNoteItemModel {
        return AddOnGiftingNoteItemModel().apply {
            isCustomNote = addOnNote.isCustomNote
            to = addOnNote.to
            from = addOnNote.from
            notes = addOnNote.notes
        }
    }

    private fun mapAddOnButton(addOnButton: AddOnGiftingResponse.AddOnButton): AddOnGiftingButtonModel {
        return AddOnGiftingButtonModel().apply {
            leftIconUrl = addOnButton.leftIconUrl
            rightIconUrl = addOnButton.rightIconUrl
            description = addOnButton.description
            action = addOnButton.action
            title = addOnButton.title
        }
    }

    private fun mapAddOnBottomSheet(addOnBottomsheet: AddOnGiftingResponse.AddOnBottomsheet): AddOnGiftingBottomSheetModel {
        return AddOnGiftingBottomSheetModel().apply {
            headerTitle = addOnBottomsheet.headerTitle
            description = addOnBottomsheet.description
            ticker = mapAddOnTicker(addOnBottomsheet.ticker)
            products = mapAddOnProducts(addOnBottomsheet.products)
        }
    }

    private fun mapAddOnTicker(ticker: AddOnGiftingResponse.AddOnBottomsheet.Ticker): AddOnGiftingTickerModel {
        return AddOnGiftingTickerModel().apply {
            text = ticker.text
        }
    }

    private fun mapAddOnProducts(products: List<AddOnGiftingResponse.AddOnBottomsheet.ProductsItem>): MutableList<AddOnGiftingProductItemModel> {
        val listAddOnProductItem = arrayListOf<AddOnGiftingProductItemModel>()
        products.forEach { productItem ->
            listAddOnProductItem.add(
                AddOnGiftingProductItemModel().apply {
                    productImageUrl = productItem.productImageUrl
                    productName = productItem.productName
                }
            )
        }
        return listAddOnProductItem
    }

    private fun mapShopTypeInfo(shop: ShopResponse): ShopTypeInfoData {
        val shopTypeInfo = shop.shopTypeInfo
        val tmpShopType =
            when {
                shop.isGold == 1 -> SHOP_TYPE_GOLD_MERCHANT
                shop.isOfficial == 1 -> SHOP_TYPE_OFFICIAL_STORE
                else -> SHOP_TYPE_REGULER
            }
        return ShopTypeInfoData(
            shopTier = shopTypeInfo.shopTier,
            shopGrade = shopTypeInfo.shopGrade,
            shopBadge = shopTypeInfo.shopBadge,
            badgeSvg = shopTypeInfo.badgeSvg,
            title = shopTypeInfo.title,
            titleFmt = shopTypeInfo.titleFmt,
            shopType = tmpShopType
        )
    }

    private fun mapShipmentInformationData(shipmentInformation: ShipmentInformation): ShipmentInformationData {
        return ShipmentInformationData().apply {
            preorder = mapPreorderData(shipmentInformation)
            estimation = shipmentInformation.estimation
            shopLocation = shipmentInformation.shopLocation
            freeShipping = mapFreeShippingData(shipmentInformation.freeShipping)
            freeShippingExtra = mapFreeShippingData(shipmentInformation.freeShippingExtra)
            freeShippingGeneral = mapFreeShippingGeneral(shipmentInformation.freeShippingGeneral)
        }
    }

    private fun mapFreeShippingData(freeShipping: FreeShipping): FreeShippingData {
        return FreeShippingData().apply {
            badgeUrl = freeShipping.badgeUrl
            eligible = freeShipping.eligible
        }
    }

    private fun mapFreeShippingGeneral(freeShippingGeneral: FreeShippingGeneral): FreeShippingGeneralData {
        return FreeShippingGeneralData(
            badgeUrl = freeShippingGeneral.badgeUrl,
            boType = freeShippingGeneral.boType,
            boName = freeShippingGeneral.boName
        )
    }

    private fun mapPreorderData(shipmentInformation: ShipmentInformation): PreorderData {
        return PreorderData().apply {
            duration = shipmentInformation.preorder.duration
            isPreorder = shipmentInformation.preorder.isPreorder
        }
    }

    private fun mapUserAddress(groupAddress: GroupAddressResponse): UserAddress {
        return UserAddress().apply {
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
            tokoNow = UserAddressTokoNow(
                isModified = groupAddress.userAddress.tokoNow.isModified,
                shopId = groupAddress.userAddress.tokoNow.shopId,
                warehouseId = groupAddress.userAddress.tokoNow.warehouseId,
                warehouses = groupAddress.userAddress.tokoNow.warehouses,
                serviceType = groupAddress.userAddress.tokoNow.serviceType
            )
        }
    }

    private fun mapTradeInInfoData(tradeInInfo: TradeInInfo): TradeInInfoData {
        return TradeInInfoData().apply {
            isValidTradeIn = tradeInInfo.isValidTradeIn
            newDevicePrice = tradeInInfo.newDevicePrice.toLong()
            newDevicePriceFmt = tradeInInfo.newDevicePriceFmt
            oldDevicePrice = tradeInInfo.oldDevicePrice.toLong()
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

    private fun mapLastApplyAdditionalInfoUiModel(additionalInfo: AdditionalInfo): LastApplyAdditionalInfoUiModel {
        return LastApplyAdditionalInfoUiModel().apply {
            emptyCartInfo = mapLastApplyEmptyCartInfoUiModel(additionalInfo.cartEmptyInfo)
            errorDetail = mapLastApplyErrorDetailUiModel(additionalInfo.errorDetail)
            messageInfo = mapLastApplyMessageInfoUiModel(additionalInfo.messageInfo)
            promoSpIds = mapPromoSpId(additionalInfo)
            usageSummaries = mapLastApplyUsageSummariesUiModel(additionalInfo.listUsageSummaries)
            pomlAutoApplied = additionalInfo.pomlAutoApplied
            bebasOngkirInfo =
                LastApplyBebasOngkirInfo(additionalInfo.bebasOngkirInfo.isBoUnstackEnabled)
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
                    type = voucherOrdersItem.type
                    cartStringGroup = voucherOrdersItem.cartStringGroup
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
        return CodModel(cod.isCod, cod.counterCod)
    }

    private fun mapPopUp(popup: PopUp): PopUpData {
        return PopUpData().apply {
            button = mapButton(popup.button)
            description = popup.description
            title = popup.title
        }
    }

    private fun mapAddOnWording(addOnWording: AddOnWording): AddOnWordingData {
        return AddOnWordingData().apply {
            packagingAndGreetingCard = addOnWording.packagingAndGreetingCard
            onlyGreetingCard = addOnWording.onlyGreetingCard
            invoiceNotSendToRecipient = addOnWording.invoiceNotSendToRecipient
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
            iconUrl = shipmentAddressFormDataResponse.donation.iconUrl
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
            tooltipTitleText = shipmentAddressFormDataResponse.egoldAttributes.egoldMessage.tooltipTitleText
            hyperlinkText = shipmentAddressFormDataResponse.egoldAttributes.hyperlinkText.text
            hyperlinkUrl = shipmentAddressFormDataResponse.egoldAttributes.hyperlinkText.url
            isShowHyperlink = shipmentAddressFormDataResponse.egoldAttributes.hyperlinkText.isShow
            iconUrl = shipmentAddressFormDataResponse.egoldAttributes.iconUrl

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
                    val mvcShippingBenefitUiModels: MutableList<MvcShippingBenefitUiModel> =
                        ArrayList()
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
            var tradeInDefaultAddress: UserAddressResponse? = null
            var tradeInDropOffAddress: UserAddressResponse? = null
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

    private fun mapUserAddress(defaultAddress: UserAddressResponse): UserAddress {
        return UserAddress().apply {
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
            tokoNow = UserAddressTokoNow(
                isModified = defaultAddress.tokoNow.isModified,
                shopId = defaultAddress.tokoNow.shopId,
                warehouseId = defaultAddress.tokoNow.warehouseId,
                warehouses = defaultAddress.tokoNow.warehouses,
                serviceType = defaultAddress.tokoNow.serviceType
            )
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
                var allProductsError = true
                var defaultErrorMessage = ""
                var allProductsHaveSameError = true
                for (groupShopV2 in groupShop.groupShopData) {
                    for (product in groupShopV2.products) {
                        if (product.isError || product.errorMessage.isNotEmpty()) {
                            hasError = true
                            if (defaultErrorMessage.isEmpty()) {
                                defaultErrorMessage = product.errorMessage
                            } else if (allProductsHaveSameError && defaultErrorMessage != product.errorMessage) {
                                allProductsHaveSameError = false
                            }
                        } else {
                            allProductsError = false
                        }
                    }
                }
                if (allProductsError) {
                    if (!groupShop.isError) {
                        groupShop.isError = true
                        groupShop.errorMessage = defaultErrorMessage
                    }
                    if (allProductsHaveSameError) {
                        for (groupShopV2 in groupShop.groupShopData) {
                            for (product in groupShopV2.products) {
                                product.isError = false
                                product.errorMessage = ""
                            }
                        }
                    }
                }
            }
        }
        return hasError
    }

    private fun mapUpsell(upsell: Upsell): UpsellData {
        return UpsellData(
            upsell.isShow,
            upsell.title,
            upsell.description,
            upsell.appLink,
            upsell.image
        )
    }

    private fun mapUpsell(upsell: NewUpsell): NewUpsellData {
        return NewUpsellData(
            upsell.isShow,
            upsell.isSelected,
            upsell.description,
            upsell.appLink,
            upsell.image,
            upsell.price,
            upsell.priceWording,
            upsell.duration,
            upsell.summaryInfo,
            upsell.button.text,
            upsell.id,
            upsell.additionalVerticalId,
            upsell.transactionType
        )
    }

    private fun mapScheduleDelivery(scheduleDelivery: ScheduleDelivery): ScheduleDeliveryData {
        return ScheduleDeliveryData(
            scheduleDelivery.timeslotId,
            scheduleDelivery.scheduleDate,
            scheduleDelivery.validationMetadata
        )
    }

    private fun mapCoachmarkPlus(coachmarkPlus: CoachmarkPlusResponse): CheckoutCoachmarkPlusData {
        return CheckoutCoachmarkPlusData(
            isShown = coachmarkPlus.plus.isShown,
            title = coachmarkPlus.plus.title,
            content = coachmarkPlus.plus.content
        )
    }

    private fun mapPlatformFee(platformFee: ShipmentPlatformFee): ShipmentPlatformFeeData {
        return ShipmentPlatformFeeData(
            isEnable = platformFee.isEnable,
            errorWording = platformFee.errorWording,
            additionalData = platformFee.additionalData,
            profileCode = platformFee.profileCode
        )
    }

    private fun mapSummaryAddOn(listSummaryAddOns: List<ShipmentSummaryAddOn>): List<ShipmentSummaryAddOnData> {
        val listShipmentSummaryAddOn: ArrayList<ShipmentSummaryAddOnData> = arrayListOf()
        listSummaryAddOns.forEach { item ->
            val shipmentSummaryAddOnData = ShipmentSummaryAddOnData(
                wording = item.wording,
                type = item.type
            )
            listShipmentSummaryAddOn.add(shipmentSummaryAddOnData)
        }
        return listShipmentSummaryAddOn
    }

    companion object {
        private const val SHOP_TYPE_OFFICIAL_STORE = "official_store"
        private const val SHOP_TYPE_GOLD_MERCHANT = "gold_merchant"
        private const val SHOP_TYPE_REGULER = "reguler"

        private const val CART_DETAIL_TYPE_BMGM = "bmgm"

        const val DISABLED_DROPSHIPPER = "dropshipper"
        const val DISABLED_ORDER_PRIORITY = "order_prioritas"
        const val DISABLED_EGOLD = "egold"
        const val DISABLED_PURCHASE_PROTECTION = "ppp"
        const val DISABLED_DONATION = "donation"
        const val DISABLED_CROSS_SELL = "cross_sell"

        const val BUNDLING_ITEM_DEFAULT = 0
        const val BUNDLING_ITEM_HEADER = 1
        const val BUNDLING_ITEM_FOOTER = 2

        const val BMGM_ITEM_DEFAULT = 0
        const val BMGM_ITEM_HEADER = 1
    }
}
