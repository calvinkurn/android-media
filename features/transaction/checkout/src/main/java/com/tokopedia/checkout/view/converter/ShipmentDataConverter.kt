package com.tokopedia.checkout.view.converter

import com.tokopedia.checkout.domain.model.cartshipmentform.AddressesData
import com.tokopedia.checkout.domain.model.cartshipmentform.CartShipmentAddressFormData
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShop
import com.tokopedia.checkout.domain.model.cartshipmentform.GroupShopV2
import com.tokopedia.checkout.domain.model.cartshipmentform.NewUpsellData
import com.tokopedia.checkout.domain.model.cartshipmentform.Product
import com.tokopedia.checkout.domain.model.cartshipmentform.UpsellData
import com.tokopedia.checkout.view.uimodel.ShipmentCrossSellModel
import com.tokopedia.checkout.view.uimodel.ShipmentDonationModel
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.checkout.view.uimodel.ShipmentUpsellModel
import com.tokopedia.logisticCommon.data.entity.address.LocationDataModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.UserAddress
import com.tokopedia.logisticcart.shipping.model.CartItemExpandModel
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.logisticcart.shipping.model.CoachmarkPlusData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItem
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemTopModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnWordingModel
import com.tokopedia.purchase_platform.common.feature.gifting.domain.model.AddOnWordingData
import com.tokopedia.purchase_platform.common.utils.isNotBlankOrZero
import com.tokopedia.purchase_platform.common.utils.isNullOrEmpty
import javax.inject.Inject

/**
 * Originally authored by Kris, Aghny, Angga.
 * Modified by Irfan
 */
class ShipmentDataConverter @Inject constructor() {

    fun getRecipientAddressModel(cartShipmentAddressFormData: CartShipmentAddressFormData): RecipientAddressModel {
//        if (cartShipmentAddressFormData.groupAddress.isNotEmpty()) {
        var defaultAddress: UserAddress? = null
        var tradeInDropOffAddress: UserAddress? = null
        val addressesData = cartShipmentAddressFormData.addressesData
        val data = addressesData.data
        if (data != null) {
            if (data.defaultAddress?.addressId?.isNotBlankOrZero() == true) {
                defaultAddress = data.defaultAddress
            }
            if (data.tradeInAddress?.addressId?.isNotBlankOrZero() == true) {
                tradeInDropOffAddress = data.tradeInAddress
            }
        }
        var isTradeIn = false
        if (cartShipmentAddressFormData.groupAddress[0].groupShop.isNotEmpty()) {
            for (groupShop in cartShipmentAddressFormData.groupAddress[0].groupShop) {
                for (groupShopV2 in groupShop.groupShopData) {
                    if (groupShopV2.products.isNotEmpty()) {
                        var foundData = false
                        for (product in groupShopV2.products) {
                            if (product.tradeInInfoData.isValidTradeIn) {
                                isTradeIn = true
                                foundData = true
                                break
                            }
                        }
                        if (foundData) {
                            break
                        }
                    }
                }
            }
        }
        val recipientAddressModel =
            createRecipientAddressModel(defaultAddress, tradeInDropOffAddress, isTradeIn)
        recipientAddressModel.selectedTabIndex =
            RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT
        recipientAddressModel.disabledAddress = addressesData.disableTabs
        if (addressesData.active.equals(
                AddressesData.DEFAULT_ADDRESS,
                ignoreCase = true
            )
        ) {
            recipientAddressModel.selectedTabIndex =
                RecipientAddressModel.TAB_ACTIVE_ADDRESS_DEFAULT
        } else if (addressesData.active.equals(
                AddressesData.TRADE_IN_ADDRESS,
                ignoreCase = true
            )
        ) {
            recipientAddressModel.selectedTabIndex =
                RecipientAddressModel.TAB_ACTIVE_ADDRESS_TRADE_IN
        }
        return recipientAddressModel
//        }
//        return null
    }

    fun getShipmentDonationModel(cartShipmentAddressFormData: CartShipmentAddressFormData): ShipmentDonationModel? {
        val donation = cartShipmentAddressFormData.donation
        return if (donation != null) {
            val shipmentDonationModel = ShipmentDonationModel()
            shipmentDonationModel.isChecked = donation.isChecked
            shipmentDonationModel.donation = donation
            shipmentDonationModel
        } else {
            null
        }
    }

    fun getListShipmentCrossSellModel(cartShipmentAddressFormData: CartShipmentAddressFormData): ArrayList<ShipmentCrossSellModel>? {
        val listCrossSellModel = ArrayList<ShipmentCrossSellModel>()
        return if (cartShipmentAddressFormData.crossSell.isNotEmpty()) {
            for (i in cartShipmentAddressFormData.crossSell.indices) {
                val shipmentCrossSellModel = ShipmentCrossSellModel()
                shipmentCrossSellModel.isChecked =
                    cartShipmentAddressFormData.crossSell[i].isChecked
                shipmentCrossSellModel.isEnabled =
                    !cartShipmentAddressFormData.crossSell[i].checkboxDisabled
                shipmentCrossSellModel.crossSellModel = cartShipmentAddressFormData.crossSell[i]
                shipmentCrossSellModel.index = i
                listCrossSellModel.add(shipmentCrossSellModel)
            }
            listCrossSellModel
        } else {
            null
        }
    }

    private fun createRecipientAddressModel(
        defaultAddress: UserAddress?,
        tradeInAddress: UserAddress?,
        isTradeIn: Boolean
    ): RecipientAddressModel {
        val recipientAddress = RecipientAddressModel()
        if (defaultAddress != null) {
            recipientAddress.id = defaultAddress.addressId
            recipientAddress.addressStatus = defaultAddress.status
            recipientAddress.addressName = defaultAddress.addressName
            recipientAddress.countryName = defaultAddress.country
            recipientAddress.provinceName = defaultAddress.provinceName
            recipientAddress.destinationDistrictName = defaultAddress.districtName
            recipientAddress.cityName = defaultAddress.cityName
            recipientAddress.destinationDistrictId = defaultAddress.districtId
            recipientAddress.street = defaultAddress.address
            recipientAddress.postalCode = defaultAddress.postalCode
            recipientAddress.cityId = defaultAddress.cityId
            recipientAddress.provinceId = defaultAddress.provinceId
            recipientAddress.recipientName = defaultAddress.receiverName
            recipientAddress.recipientPhoneNumber = defaultAddress.phone
            recipientAddress.latitude =
                if (!isNullOrEmpty(defaultAddress.latitude)) defaultAddress.latitude else null
            recipientAddress.longitude =
                if (!isNullOrEmpty(defaultAddress.longitude)) defaultAddress.longitude else null
            recipientAddress.isSelected =
                defaultAddress.status == PRIME_ADDRESS
            recipientAddress.cornerId = defaultAddress.cornerId
            recipientAddress.isTradeIn = isTradeIn
            recipientAddress.isCornerAddress = defaultAddress.isCorner
        }
        if (tradeInAddress != null) {
            val locationDataModel = LocationDataModel()
            locationDataModel.address1 = tradeInAddress.address
            locationDataModel.address2 = tradeInAddress.address2
            locationDataModel.addrId = tradeInAddress.addressId
            locationDataModel.addrName = tradeInAddress.addressName
            locationDataModel.city = tradeInAddress.cityId
            locationDataModel.cityName = tradeInAddress.cityName
            locationDataModel.country = tradeInAddress.country
            locationDataModel.district = tradeInAddress.districtId
            locationDataModel.districtName = tradeInAddress.districtName
            locationDataModel.latitude = tradeInAddress.latitude
            locationDataModel.longitude = tradeInAddress.longitude
            locationDataModel.phone = tradeInAddress.phone
            locationDataModel.postalCode = tradeInAddress.postalCode
            locationDataModel.province = tradeInAddress.provinceId
            locationDataModel.provinceName = tradeInAddress.provinceName
            locationDataModel.receiverName = tradeInAddress.receiverName
            locationDataModel.status = tradeInAddress.status
            recipientAddress.dropOffAddressName = locationDataModel.addrName
            recipientAddress.dropOffAddressDetail = locationDataModel.address1
            recipientAddress.locationDataModel = locationDataModel
        }
        return recipientAddress
    }

    fun getShipmentItems(
        cartShipmentAddressFormData: CartShipmentAddressFormData,
        hasTradeInDropOffAddress: Boolean,
        username: String
    ): List<ShipmentCartItem> {
        val shipmentCartItemModels: MutableList<ShipmentCartItem> = ArrayList()
        if (cartShipmentAddressFormData.groupAddress.isEmpty()) {
            return shipmentCartItemModels
        }
        val userAddress = cartShipmentAddressFormData.groupAddress[0].userAddress
        val groupShopList = cartShipmentAddressFormData.groupAddress[0].groupShop
        var isFirstPlusProductHasPassed = false
        for ((groupShopIndex, groupShop) in groupShopList.withIndex()) {
            var orderIndex = 0
            if (groupShopList.size > 1) {
                orderIndex = groupShopIndex + 1
            }
            val shipmentInformationData = groupShop.shipmentInformationData
            val shop = groupShop.groupShopData.first().shop
            var receiverName = ""
            if (userAddress.status == ACTIVE_ADDRESS) {
                receiverName = userAddress.receiverName
            }
            val addOnWordingModel = convertFromAddOnWordingData(cartShipmentAddressFormData.addOnWording)
            val cartItemModels = arrayListOf<CartItemModel>()
            var cartItemIndex = 0
            groupShop.groupShopData.forEach {
                val productList = convertFromProductList(
                    cartItemIndex,
                    it,
                    groupShop,
                    username,
                    receiverName,
                    addOnWordingModel
                )
                cartItemModels.addAll(productList)
                cartItemIndex += productList.size
            }
            val fobject = levelUpParametersFromProductToCartSeller(cartItemModels)
            val shipmentCartItemModel = ShipmentCartItemModel(
                cartString = groupShop.cartString,
                groupType = groupShop.groupType,
                uiGroupType = groupShop.uiGroupType,
                groupInfoName = groupShop.groupInfoName,
                groupInfoBadgeUrl = groupShop.groupInfoBadgeUrl,
                isDropshipperDisable = cartShipmentAddressFormData.isDropshipperDisable,
                isOrderPrioritasDisable = cartShipmentAddressFormData.isOrderPrioritasDisable,
                isBlackbox = cartShipmentAddressFormData.isBlackbox,
                isHidingCourier = cartShipmentAddressFormData.isHidingCourier,
                addressId = cartShipmentAddressFormData.groupAddress[0].userAddress.addressId,
                isFulfillment = groupShop.isFulfillment,
                fulfillmentId = groupShop.fulfillmentId,
                fulfillmentBadgeUrl = groupShop.fulfillmentBadgeUrl,
                shopShipmentList = groupShop.shopShipments,
                isError = groupShop.isError,
                isAllItemError = groupShop.isError,
                isHasUnblockingError = groupShop.hasUnblockingError,
                unblockingErrorMessage = groupShop.unblockingErrorMessage,
                errorTitle = groupShop.errorMessage,
                firstProductErrorIndex = groupShop.firstProductErrorIndex,
                orderNumber = if (orderIndex > 0) orderIndex else 0,
                preOrderInfo = if (shipmentInformationData.preorder.isPreorder) shipmentInformationData.preorder.duration else "",
                freeShippingBadgeUrl = shipmentInformationData.freeShippingGeneral.badgeUrl,
                isFreeShippingPlus = shipmentInformationData.freeShippingGeneral.isBoTypePlus(),
                shopLocation = shipmentInformationData.shopLocation,
                shopId = shop.shopId,
                shopName = shop.shopName,
                shopAlertMessage = shop.shopAlertMessage,
                shopTypeInfoData = shop.shopTypeInfoData,
                shippingId = groupShop.shippingId,
                spId = groupShop.spId,
                boCode = groupShop.boCode,
                dropshiperName = groupShop.dropshipperName,
                dropshiperPhone = groupShop.dropshipperPhone,
                isInsurance = groupShop.isUseInsurance,
                hasPromoList = groupShop.isHasPromoList,
                isSaveStateFlag = groupShop.isSaveStateFlag,
                isLeasingProduct = groupShop.isLeasingProduct,
                bookingFee = groupShop.bookingFee,
                listPromoCodes = groupShop.listPromoCodes,
                isHasSetDropOffLocation = hasTradeInDropOffAddress,
                addOnsOrderLevelModel = groupShop.addOns,
                addOnWordingModel = addOnWordingModel,
                addOnDefaultFrom = username,
                timeslotId = groupShop.scheduleDelivery.timeslotId,
                scheduleDate = groupShop.scheduleDelivery.scheduleDate,
                validationMetadata = groupShop.scheduleDelivery.validationMetadata,
                ratesValidationFlow = groupShop.ratesValidationFlow,
                addOnDefaultTo = receiverName,
                isProductFcancelPartial = fobject.isFcancelPartial == 1,
                cartItemModels = cartItemModels,
                isProductIsPreorder = fobject.isPreOrder == 1,
                isTokoNow = shop.isTokoNow,
                shopTickerTitle = shop.shopTickerTitle,
                shopTicker = shop.shopTicker,
                enablerLabel = shop.enablerLabel,

                isEligibleNewShippingExperience = cartShipmentAddressFormData.isEligibleNewShippingExperience,
                isDisableChangeCourier = groupShop.isDisableChangeCourier,
                isAutoCourierSelection = groupShop.autoCourierSelection,
                hasGeolocation = userAddress.longitude.isNotEmpty() && userAddress.latitude.isNotEmpty(),
                courierSelectionErrorTitle = groupShop.courierSelectionErrorData.title,
                courierSelectionErrorDescription = groupShop.courierSelectionErrorData.description
            )
            for (cartItemModel in cartItemModels) {
                if (cartItemModel.ethicalDrugDataModel.needPrescription && !cartItemModel.isError) {
                    shipmentCartItemModel.hasEthicalProducts = true
                } else if (!cartItemModel.isError) {
                    shipmentCartItemModel.hasNonEthicalProducts = true
                }
            }
            shipmentCartItemModel.shipmentCartData = RatesDataConverter()
                .getShipmentCartData(
                    userAddress,
                    groupShop,
                    shipmentCartItemModel,
                    cartShipmentAddressFormData.keroToken,
                    cartShipmentAddressFormData.keroUnixTime.toString()
                )
            if (groupShop.isFulfillment) {
                shipmentCartItemModel.shopLocation = groupShop.fulfillmentName
            }
            setCartItemModelError(shipmentCartItemModel)
            // todo: fix group shop
            if (shipmentCartItemModel.isFreeShippingPlus && !isFirstPlusProductHasPassed) {
                val coachmarkPlusData = CoachmarkPlusData(
                    cartShipmentAddressFormData.coachmarkPlus.isShown,
                    cartShipmentAddressFormData.coachmarkPlus.title,
                    cartShipmentAddressFormData.coachmarkPlus.content
                )
                shipmentCartItemModel.coachmarkPlus = coachmarkPlusData
                isFirstPlusProductHasPassed = true
            } else {
                shipmentCartItemModel.coachmarkPlus = CoachmarkPlusData()
            }

            // top
            val shipmentCartItemTopModel =
                ShipmentCartItemTopModel(
                    cartString = groupShop.cartString,
                    isError = shipmentCartItemModel.isError,
                    errorTitle = shipmentCartItemModel.errorTitle,
                    errorDescription = shipmentCartItemModel.errorDescription,
                    isHasUnblockingError = shipmentCartItemModel.isHasUnblockingError,
                    unblockingErrorMessage = shipmentCartItemModel.unblockingErrorMessage,
                    firstProductErrorIndex = shipmentCartItemModel.firstProductErrorIndex,
                    isCustomEpharmacyError = shipmentCartItemModel.isCustomEpharmacyError,
                    shopId = shipmentCartItemModel.shopId,
                    shopName = shipmentCartItemModel.groupInfoName,
                    orderNumber = shipmentCartItemModel.orderNumber,
                    preOrderInfo = shipmentCartItemModel.preOrderInfo,
                    freeShippingBadgeUrl = shipmentCartItemModel.freeShippingBadgeUrl,
                    isFreeShippingPlus = shipmentCartItemModel.isFreeShippingPlus,
                    shopLocation = shipmentCartItemModel.shopLocation,
                    shopAlertMessage = shipmentCartItemModel.shopAlertMessage,
                    shopTypeInfoData = shipmentCartItemModel.shopTypeInfoData,
                    shopTickerTitle = shipmentCartItemModel.shopTickerTitle,
                    shopTicker = shipmentCartItemModel.shopTicker,
                    enablerLabel = shipmentCartItemModel.enablerLabel,
                    hasTradeInItem = shipmentCartItemModel.cartItemModels.firstOrNull { it.isValidTradeIn } != null,
                    isFulfillment = shipmentCartItemModel.isFulfillment,
                    fulfillmentBadgeUrl = shipmentCartItemModel.fulfillmentBadgeUrl,
                    uiGroupType = shipmentCartItemModel.uiGroupType,
                    groupInfoBadgeUrl = shipmentCartItemModel.groupInfoBadgeUrl
                )
            shipmentCartItemModels.add(shipmentCartItemTopModel)
            if (shipmentCartItemModel.isStateAllItemViewExpanded) {
                shipmentCartItemModel.cartItemModels.forEach {
                    shipmentCartItemModels.add(it)
                }
            } else {
                shipmentCartItemModels.add(shipmentCartItemModel.cartItemModels.first())
            }
            if (shipmentCartItemModel.cartItemModels.size > 1) {
                shipmentCartItemModels.add(
                    CartItemExpandModel(
                        cartString = shipmentCartItemModel.cartString,
                        cartSize = shipmentCartItemModel.cartItemModels.size
                    )
                )
            }
            shipmentCartItemModels.add(shipmentCartItemModel)
        }
        return shipmentCartItemModels
    }

    private fun setCartItemModelError(shipmentCartItemModel: ShipmentCartItemModel) {
        if (shipmentCartItemModel.isAllItemError) {
            for (cartItemModel in shipmentCartItemModel.cartItemModels) {
                cartItemModel.isError = true
                cartItemModel.isShopError = true
            }
        }
    }

//    private fun getShipmentItem(
//        shipmentCartItemModel: ShipmentCartItemModel,
//        userAddress: UserAddress,
//        groupShop: GroupShop,
//        keroToken: String,
//        keroUnixTime: String,
//        hasTradeInDropOffAddress: Boolean,
//        orderIndex: Int,
//        addOnWording: AddOnWordingData,
//        username: String
//    ) {
//        shipmentCartItemModel.shopShipmentList = groupShop.shopShipments
//        shipmentCartItemModel.isError = groupShop.isError
//        if (shipmentCartItemModel.isError) {
//            shipmentCartItemModel.isAllItemError = true
//        }
//        shipmentCartItemModel.isHasUnblockingError = groupShop.hasUnblockingError
//        shipmentCartItemModel.unblockingErrorMessage = groupShop.unblockingErrorMessage
//        shipmentCartItemModel.errorTitle = groupShop.errorMessage
//        shipmentCartItemModel.firstProductErrorIndex = groupShop.firstProductErrorIndex
//        if (orderIndex > 0) {
//            shipmentCartItemModel.orderNumber = orderIndex
//        }
//        val shipmentInformationData = groupShop.shipmentInformationData
//        if (shipmentInformationData.preorder.isPreorder) {
//            shipmentCartItemModel.preOrderInfo = shipmentInformationData.preorder.duration
//        }
//        shipmentCartItemModel.freeShippingBadgeUrl =
//            shipmentInformationData.freeShippingGeneral.badgeUrl
//        shipmentCartItemModel.isFreeShippingPlus =
//            shipmentInformationData.freeShippingGeneral.isBoTypePlus()
//        shipmentCartItemModel.shopLocation = shipmentInformationData.shopLocation
//        val shop = groupShop.groupShopData.first().shop
//        shipmentCartItemModel.shopId = shop.shopId
//        shipmentCartItemModel.shopName = shop.shopName
//        shipmentCartItemModel.shopAlertMessage = shop.shopAlertMessage
//        shipmentCartItemModel.shopTypeInfoData = shop.shopTypeInfoData
//        shipmentCartItemModel.cartString = groupShop.cartString
//        shipmentCartItemModel.shippingId = groupShop.shippingId,
//        shipmentCartItemModel.spId = groupShop.spId,
//        shipmentCartItemModel.boCode = groupShop.boCode,
//        shipmentCartItemModel.dropshiperName = groupShop.dropshipperName,
//        shipmentCartItemModel.dropshiperPhone = groupShop.dropshipperPhone,
//        shipmentCartItemModel.isInsurance = groupShop.isUseInsurance,
//        shipmentCartItemModel.hasPromoList = groupShop.isHasPromoList,
//        shipmentCartItemModel.isSaveStateFlag = groupShop.isSaveStateFlag,
//        shipmentCartItemModel.isLeasingProduct = groupShop.isLeasingProduct,
//        shipmentCartItemModel.bookingFee = groupShop.bookingFee,
//        shipmentCartItemModel.listPromoCodes = groupShop.listPromoCodes,
//        shipmentCartItemModel.isHasSetDropOffLocation = hasTradeInDropOffAddress,
//        shipmentCartItemModel.addOnsOrderLevelModel = groupShop.addOns,
//        shipmentCartItemModel.addOnWordingModel = convertFromAddOnWordingData(addOnWording),
//        shipmentCartItemModel.addOnDefaultFrom = username,
//        shipmentCartItemModel.timeslotId = groupShop.scheduleDelivery.timeslotId,
//        shipmentCartItemModel.scheduleDate = groupShop.scheduleDelivery.scheduleDate,
//        shipmentCartItemModel.validationMetadata = groupShop.scheduleDelivery.validationMetadata,
//        shipmentCartItemModel.ratesValidationFlow = groupShop.ratesValidationFlow,
//        var receiverName = ""
//        if (userAddress.status == ACTIVE_ADDRESS) {
//            receiverName = userAddress.receiverName
//        }
//        shipmentCartItemModel.addOnDefaultTo = receiverName
//        val cartItemModels = arrayListOf<CartItemModel>()
//        var cartItemIndex = 0
//        groupShop.groupShopData.forEach {
//            val productList = convertFromProductList(
//                cartItemIndex,
//                it,
//                groupShop,
//                username,
//                receiverName,
//                shipmentCartItemModel.addOnWordingModel
//            )
//            cartItemModels.addAll(productList)
//            cartItemIndex += productList.size
//        }
//        val products = groupShop.groupShopData
//        val cartItemModels = convertFromProductList(products, groupShop, username, receiverName, shipmentCartItemModel.addOnWordingModel)

    // This is something that not well planned
//        val fobject = levelUpParametersFromProductToCartSeller(cartItemModels)
//        shipmentCartItemModel.isProductFcancelPartial = fobject.isFcancelPartial == 1
//        shipmentCartItemModel.cartItemModels = cartItemModels
//        shipmentCartItemModel.isProductIsPreorder = fobject.isPreOrder == 1
//        for (cartItemModel in cartItemModels) {
//            if (cartItemModel.ethicalDrugDataModel.needPrescription && !cartItemModel.isError) {
//                shipmentCartItemModel.hasEthicalProducts = true
//            } else if (!cartItemModel.isError) {
//                shipmentCartItemModel.hasNonEthicalProducts = true
//            }
//        }
//        shipmentCartItemModel.shipmentCartData = RatesDataConverter()
//            .getShipmentCartData(
//                userAddress,
//                groupShop,
//                shipmentCartItemModel,
//                keroToken,
//                keroUnixTime
//            )
//    }

    private fun convertFromProductList(
        index: Int,
        groupShopV2: GroupShopV2,
        groupShop: GroupShop,
        username: String,
        receiverName: String,
        addOnOrderLevelModel: AddOnWordingModel
    ): List<CartItemModel> {
        var counterIndex = index
        return groupShopV2.products.map { product ->
            val cartItem = convertFromProduct(
                counterIndex,
                product,
                groupShop,
                username,
                receiverName,
                addOnOrderLevelModel,
                groupShopV2
            )
            counterIndex += 1
            cartItem
        }
    }

    private fun convertFromProduct(
        index: Int,
        product: Product,
        groupShop: GroupShop,
        username: String,
        receiverName: String,
        addOnWordingModel: AddOnWordingModel,
        groupShopV2: GroupShopV2
    ): CartItemModel {
        val ppp = product.purchaseProtectionPlanData
        return CartItemModel(
            cartString = groupShop.cartString,
            shouldShowShopInfo = product.shouldShowShopInfo,
            shopTypeInfoData = product.shopTypeInfoData,
            cartStringOrder = groupShopV2.cartStringOrder,
            originWarehouseIds = product.originWarehouseIds,
            cartId = product.cartId,
            productId = product.productId,
            productCatId = product.productCatId.toLong(),
            name = product.productName,
            shopId = groupShopV2.shop.shopId.toString(),
            shopName = product.shopName,
            currency = product.productPriceCurrency,
            imageUrl = product.productImageSrc200Square,
            price = if (product.productWholesalePrice != 0.0) product.productWholesalePrice else product.productPrice,
            isWholesalePrice = product.productWholesalePrice != 0.0,
            originalPrice = product.productOriginalPrice,
            quantity = product.productQuantity,
            weight = product.productWeight.toDouble(),
            weightFmt = product.productWeightFmt,
            weightActual = product.productWeightActual.toDouble(),
            noteToSeller = product.productNotes,
            isPreOrder = product.isProductIsPreorder,
            preOrderInfo = product.productPreOrderInfo,
            preOrderDurationDay = product.preOrderDurationDay,
            isFreeReturn = product.isProductIsFreeReturns,
            cashback = product.productCashback,
            isCashback = product.productCashback.isNotEmpty(),
            fInsurance = product.isProductFinsurance,
            fCancelPartial = product.isProductFcancelPartial,
            isError = product.isError,
            errorMessage = product.errorMessage,
            errorMessageDescription = product.errorMessageDescription,
            isFreeShippingExtra = product.isFreeShippingExtra,
            isFreeShipping = product.isFreeShipping,
            freeShippingName = product.freeShippingName,
            isShowTicker = product.isShowTicker,
            tickerMessage = product.tickerMessage,
            variant = product.variant,
            variantParentId = product.variantParentId,
            productAlertMessage = product.productAlertMessage,
            productInformation = product.productInformation,
            isValidTradeIn = product.tradeInInfoData.isValidTradeIn,
            oldDevicePrice = if (product.tradeInInfoData.isValidTradeIn) product.tradeInInfoData.oldDevicePrice else 0,
            newDevicePrice = if (product.tradeInInfoData.isValidTradeIn) product.tradeInInfoData.newDevicePrice else 0,
            deviceModel = if (product.tradeInInfoData.isValidTradeIn) product.tradeInInfoData.deviceModel else "",
            diagnosticId = if (product.tradeInInfoData.isValidTradeIn) product.tradeInInfoData.diagnosticId else "",
            isProtectionAvailable = ppp.isProtectionAvailable,
            protectionPricePerProduct = if (ppp.isProtectionAvailable) ppp.protectionPricePerProduct else 0,
            protectionPrice = if (ppp.isProtectionAvailable) ppp.protectionPrice else 0.0,
            protectionTitle = if (ppp.isProtectionAvailable) ppp.protectionTitle else "",
            protectionSubTitle = if (ppp.isProtectionAvailable) ppp.protectionSubtitle else "",
            protectionLinkText = if (ppp.isProtectionAvailable) ppp.protectionLinkText else "",
            protectionLinkUrl = if (ppp.isProtectionAvailable) ppp.protectionLinkUrl else "",
            isProtectionOptIn = if (ppp.isProtectionAvailable) ppp.isProtectionOptIn else false,
            isProtectionCheckboxDisabled = if (ppp.isProtectionAvailable) ppp.isProtectionCheckboxDisabled else false,
            isBundlingItem = product.isBundlingItem,
            bundlingItemPosition = product.bundlingItemPosition,
            bundleId = product.bundleId,
            bundleGroupId = product.bundleGroupId,
            bundleType = product.bundleType,
            bundleTitle = product.bundleTitle,
            bundlePrice = product.bundlePrice,
            bundleSlashPriceLabel = product.bundleSlashPriceLabel,
            bundleOriginalPrice = product.bundleOriginalPrice,
            bundleQuantity = product.bundleQuantity,
            bundleIconUrl = product.bundleIconUrl,
            analyticsProductCheckoutData = product.analyticsProductCheckoutData,
            addOnProductLevelModel = product.addOnProduct,
            ethicalDrugDataModel = product.ethicalDrugs,
            addOnDefaultFrom = username,
            addOnDefaultTo = receiverName,
            warehouseId = groupShop.fulfillmentId.toString(),
            isTokoCabang = groupShop.isFulfillment,
            cartItemPosition = index,
            addOnOrderLevelModel = addOnWordingModel
        )
    }

    private fun convertFromAddOnWordingData(addOnWordingData: AddOnWordingData): AddOnWordingModel {
        val addOnWordingModel = AddOnWordingModel()
        addOnWordingModel.onlyGreetingCard = addOnWordingData.onlyGreetingCard
        addOnWordingModel.packagingAndGreetingCard = addOnWordingData.packagingAndGreetingCard
        addOnWordingModel.invoiceNotSendToRecipient = addOnWordingData.invoiceNotSendToRecipient
        return addOnWordingModel
    }

    fun getShipmentUpsellModel(upsellData: UpsellData): ShipmentUpsellModel {
        val shipmentUpsellModel = ShipmentUpsellModel()
        shipmentUpsellModel.isShow = upsellData.isShow
        shipmentUpsellModel.title = upsellData.title
        shipmentUpsellModel.description = upsellData.description
        shipmentUpsellModel.appLink = upsellData.appLink
        shipmentUpsellModel.image = upsellData.image
        return shipmentUpsellModel
    }

    fun getShipmentNewUpsellModel(upsellData: NewUpsellData): ShipmentNewUpsellModel {
        val shipmentUpsellModel = ShipmentNewUpsellModel()
        shipmentUpsellModel.isShow = upsellData.isShow
        shipmentUpsellModel.isSelected = upsellData.isSelected
        shipmentUpsellModel.description = upsellData.description
        shipmentUpsellModel.appLink = upsellData.appLink
        shipmentUpsellModel.image = upsellData.image
        shipmentUpsellModel.price = upsellData.price
        shipmentUpsellModel.priceWording = upsellData.priceWording
        shipmentUpsellModel.duration = upsellData.duration
        shipmentUpsellModel.summaryInfo = upsellData.summaryInfo
        shipmentUpsellModel.buttonText = upsellData.buttonText
        shipmentUpsellModel.id = upsellData.id
        shipmentUpsellModel.additionalVerticalId = upsellData.additionalVerticalId
        shipmentUpsellModel.transactionType = upsellData.transactionType
        shipmentUpsellModel.isLoading = false
        return shipmentUpsellModel
    }

    private fun levelUpParametersFromProductToCartSeller(cartItemList: List<CartItemModel>): Fobject {
        var isPreOrder = 0
        var isFcancelPartial = 0
        var isFinsurance = 0
        for (cartItem in cartItemList) {
            if (cartItem.isPreOrder) {
                isPreOrder = 1
            }
            if (cartItem.fInsurance) {
                isFcancelPartial = 1
            }
            if (cartItem.fCancelPartial) {
                isFinsurance = 1
            }
        }
        return Fobject(isPreOrder, isFcancelPartial, isFinsurance)
    }

    private inner class Fobject(
        var isPreOrder: Int,
        var isFcancelPartial: Int,
        var isFinsurance: Int
    )

    companion object {
        private const val ACTIVE_ADDRESS = 1
        private const val PRIME_ADDRESS = 2
        private const val MERCHANT_VOUCHER_TYPE = "merchant"
        private const val LOGISTIC_VOUCHER_TYPE = "logistic"
    }
}
