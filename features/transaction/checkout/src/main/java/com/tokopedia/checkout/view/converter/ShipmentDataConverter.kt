package com.tokopedia.checkout.view.converter

import android.text.TextUtils
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
            val shipmentCartItemModel = ShipmentCartItemModel(
                cartString = groupShop.cartString,
                groupType = groupShop.groupType,
                uiGroupType = groupShop.uiGroupType,
                groupInfoName = groupShop.groupInfoName,
                groupInfoBadgeUrl = groupShop.groupInfoBadgeUrl
            )
            shipmentCartItemModel.isDropshipperDisable =
                cartShipmentAddressFormData.isDropshipperDisable
            shipmentCartItemModel.isOrderPrioritasDisable =
                cartShipmentAddressFormData.isOrderPrioritasDisable
            shipmentCartItemModel.isBlackbox = cartShipmentAddressFormData.isBlackbox
            shipmentCartItemModel.isHidingCourier = cartShipmentAddressFormData.isHidingCourier
            shipmentCartItemModel.addressId =
                cartShipmentAddressFormData.groupAddress[0].userAddress.addressId
            var orderIndex = 0
            if (groupShopList.size > 1) {
                orderIndex = groupShopIndex + 1
            }
            shipmentCartItemModel.isFulfillment = groupShop.isFulfillment
            shipmentCartItemModel.fulfillmentId = groupShop.fulfillmentId
            shipmentCartItemModel.fulfillmentBadgeUrl = groupShop.fulfillmentBadgeUrl
            getShipmentItem(
                shipmentCartItemModel,
                userAddress,
                groupShop,
                cartShipmentAddressFormData.keroToken,
                cartShipmentAddressFormData.keroUnixTime.toString(),
                hasTradeInDropOffAddress,
                orderIndex,
                cartShipmentAddressFormData.addOnWording,
                username
            )
            if (groupShop.isFulfillment) {
                shipmentCartItemModel.shopLocation = groupShop.fulfillmentName
            }
            setCartItemModelError(shipmentCartItemModel)
            shipmentCartItemModel.isEligibleNewShippingExperience =
                cartShipmentAddressFormData.isEligibleNewShippingExperience
            shipmentCartItemModel.isDisableChangeCourier = groupShop.isDisableChangeCourier
            shipmentCartItemModel.isAutoCourierSelection = groupShop.autoCourierSelection
            shipmentCartItemModel.hasGeolocation =
                !TextUtils.isEmpty(userAddress.longitude) && !TextUtils.isEmpty(userAddress.latitude)
            shipmentCartItemModel.courierSelectionErrorTitle =
                groupShop.courierSelectionErrorData.title
            shipmentCartItemModel.courierSelectionErrorDescription =
                groupShop.courierSelectionErrorData.description
            // todo: fix group shop
            shipmentCartItemModel.isTokoNow = groupShop.groupShopData.first().shop.isTokoNow
            shipmentCartItemModel.shopTickerTitle = groupShop.groupShopData.first().shop.shopTickerTitle
            shipmentCartItemModel.shopTicker = groupShop.groupShopData.first().shop.shopTicker
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
            shipmentCartItemModel.enablerLabel = groupShop.groupShopData.first().shop.enablerLabel

            // top
            val shipmentCartItemTopModel =
                ShipmentCartItemTopModel(
                    cartString = groupShop.cartString,
                    isError = shipmentCartItemModel.isError,
                    errorTitle = shipmentCartItemModel.errorTitle ?: "",
                    errorDescription = shipmentCartItemModel.errorDescription ?: "",
                    isHasUnblockingError = shipmentCartItemModel.isHasUnblockingError,
                    unblockingErrorMessage = shipmentCartItemModel.unblockingErrorMessage ?: "",
                    firstProductErrorIndex = shipmentCartItemModel.firstProductErrorIndex,
                    isCustomEpharmacyError = shipmentCartItemModel.isCustomEpharmacyError,
                    shopId = shipmentCartItemModel.shopId,
                    shopName = shipmentCartItemModel.groupInfoName,
                    orderNumber = shipmentCartItemModel.orderNumber,
                    preOrderInfo = shipmentCartItemModel.preOrderInfo ?: "",
                    freeShippingBadgeUrl = shipmentCartItemModel.freeShippingBadgeUrl ?: "",
                    isFreeShippingPlus = shipmentCartItemModel.isFreeShippingPlus,
                    shopLocation = shipmentCartItemModel.shopLocation ?: "",
                    shopAlertMessage = shipmentCartItemModel.shopAlertMessage ?: "",
                    shopTypeInfoData = shipmentCartItemModel.shopTypeInfoData,
                    shopTickerTitle = shipmentCartItemModel.shopTickerTitle,
                    shopTicker = shipmentCartItemModel.shopTicker,
                    enablerLabel = shipmentCartItemModel.enablerLabel,
                    hasTradeInItem = shipmentCartItemModel.cartItemModels.firstOrNull { it.isValidTradeIn } != null,
                    isFulfillment = shipmentCartItemModel.isFulfillment,
                    fulfillmentBadgeUrl = shipmentCartItemModel.fulfillmentBadgeUrl ?: "",
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

    private fun getShipmentItem(
        shipmentCartItemModel: ShipmentCartItemModel,
        userAddress: UserAddress,
        groupShop: GroupShop,
        keroToken: String,
        keroUnixTime: String,
        hasTradeInDropOffAddress: Boolean,
        orderIndex: Int,
        addOnWording: AddOnWordingData,
        username: String
    ) {
        shipmentCartItemModel.shopShipmentList = groupShop.shopShipments
        shipmentCartItemModel.isError = groupShop.isError
        if (shipmentCartItemModel.isError) {
            shipmentCartItemModel.isAllItemError = true
        }
        shipmentCartItemModel.isHasUnblockingError = groupShop.hasUnblockingError
        shipmentCartItemModel.unblockingErrorMessage = groupShop.unblockingErrorMessage
        shipmentCartItemModel.errorTitle = groupShop.errorMessage
        shipmentCartItemModel.firstProductErrorIndex = groupShop.firstProductErrorIndex
        if (orderIndex > 0) {
            shipmentCartItemModel.orderNumber = orderIndex
        }
        val shipmentInformationData = groupShop.shipmentInformationData
        if (shipmentInformationData.preorder.isPreorder) {
            shipmentCartItemModel.preOrderInfo = shipmentInformationData.preorder.duration
        }
        shipmentCartItemModel.freeShippingBadgeUrl =
            shipmentInformationData.freeShippingGeneral.badgeUrl
        shipmentCartItemModel.isFreeShippingPlus =
            shipmentInformationData.freeShippingGeneral.isBoTypePlus()
        shipmentCartItemModel.shopLocation = shipmentInformationData.shopLocation
        val shop = groupShop.groupShopData.first().shop
        shipmentCartItemModel.shopId = shop.shopId
        shipmentCartItemModel.shopName = shop.shopName
        shipmentCartItemModel.shopAlertMessage = shop.shopAlertMessage
        shipmentCartItemModel.shopTypeInfoData = shop.shopTypeInfoData
//        shipmentCartItemModel.cartString = groupShop.cartString
        shipmentCartItemModel.shippingId = groupShop.shippingId
        shipmentCartItemModel.spId = groupShop.spId
        shipmentCartItemModel.boCode = groupShop.boCode
        shipmentCartItemModel.dropshiperName = groupShop.dropshipperName
        shipmentCartItemModel.dropshiperPhone = groupShop.dropshipperPhone
        shipmentCartItemModel.isInsurance = groupShop.isUseInsurance
        shipmentCartItemModel.hasPromoList = groupShop.isHasPromoList
        shipmentCartItemModel.isSaveStateFlag = groupShop.isSaveStateFlag
        shipmentCartItemModel.isLeasingProduct = groupShop.isLeasingProduct
        shipmentCartItemModel.bookingFee = groupShop.bookingFee
        shipmentCartItemModel.listPromoCodes = groupShop.listPromoCodes
        shipmentCartItemModel.isHasSetDropOffLocation = hasTradeInDropOffAddress
        shipmentCartItemModel.addOnsOrderLevelModel = groupShop.addOns
        shipmentCartItemModel.addOnWordingModel = convertFromAddOnWordingData(addOnWording)
        shipmentCartItemModel.addOnDefaultFrom = username
        shipmentCartItemModel.timeslotId = groupShop.scheduleDelivery.timeslotId
        shipmentCartItemModel.scheduleDate = groupShop.scheduleDelivery.scheduleDate
        shipmentCartItemModel.validationMetadata = groupShop.scheduleDelivery.validationMetadata
        shipmentCartItemModel.ratesValidationFlow = groupShop.ratesValidationFlow
        var receiverName = ""
        if (userAddress.status == ACTIVE_ADDRESS) {
            receiverName = userAddress.receiverName
        }
        shipmentCartItemModel.addOnDefaultTo = receiverName
        val cartItemModels = arrayListOf<CartItemModel>()
        var cartItemIndex = 0
        groupShop.groupShopData.forEach {
            val productList = convertFromProductList(
                cartItemIndex,
                it,
                groupShop,
                username,
                receiverName,
                shipmentCartItemModel.addOnWordingModel
            )
            cartItemModels.addAll(productList)
            cartItemIndex += productList.size
        }
//        val products = groupShop.groupShopData
//        val cartItemModels = convertFromProductList(products, groupShop, username, receiverName, shipmentCartItemModel.addOnWordingModel)

        // This is something that not well planned
        val fobject = levelUpParametersFromProductToCartSeller(cartItemModels)
        shipmentCartItemModel.isProductFcancelPartial = fobject.isFcancelPartial == 1
        shipmentCartItemModel.cartItemModels = cartItemModels
        shipmentCartItemModel.isProductIsPreorder = fobject.isPreOrder == 1
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
                keroToken,
                keroUnixTime
            )
    }

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
        val cartItemModel = CartItemModel(
            cartString = groupShop.cartString,
            shouldShowShopInfo = product.shouldShowShopInfo,
            shopTypeInfoData = product.shopTypeInfoData,
            cartStringOrder = groupShopV2.cartStringOrder,
            originWarehouseIds = product.originWarehouseIds
        )
        cartItemModel.cartId = product.cartId
        cartItemModel.productId = product.productId
        cartItemModel.productCatId = product.productCatId.toLong()
        cartItemModel.name = product.productName
        cartItemModel.shopId = groupShopV2.shop.shopId.toString()
        cartItemModel.shopName = product.shopName
        cartItemModel.imageUrl = product.productImageSrc200Square
        cartItemModel.currency = product.productPriceCurrency
        if (product.productWholesalePrice != 0.0) {
            cartItemModel.price = product.productWholesalePrice
            cartItemModel.isWholesalePrice = true
        } else {
            cartItemModel.price = product.productPrice
            cartItemModel.isWholesalePrice = false
        }
        cartItemModel.originalPrice = product.productOriginalPrice
        cartItemModel.quantity = product.productQuantity
        cartItemModel.weight = product.productWeight.toDouble()
        cartItemModel.weightFmt = product.productWeightFmt
        cartItemModel.weightActual = product.productWeightActual.toDouble()
        cartItemModel.noteToSeller = product.productNotes
        cartItemModel.isPreOrder = product.isProductIsPreorder
        cartItemModel.preOrderInfo = product.productPreOrderInfo
        cartItemModel.preOrderDurationDay = product.preOrderDurationDay
        cartItemModel.isFreeReturn = product.isProductIsFreeReturns
        cartItemModel.cashback = product.productCashback
        cartItemModel.isCashback = !isNullOrEmpty(product.productCashback)
        cartItemModel.fInsurance = product.isProductFcancelPartial
        cartItemModel.fCancelPartial = product.isProductFinsurance
        cartItemModel.isError = product.isError
        cartItemModel.errorMessage = product.errorMessage
        cartItemModel.errorMessageDescription = product.errorMessageDescription
        cartItemModel.isFreeShippingExtra = product.isFreeShippingExtra
        cartItemModel.isFreeShipping = product.isFreeShipping
        cartItemModel.freeShippingName = product.freeShippingName
        cartItemModel.isShowTicker = product.isShowTicker
        cartItemModel.tickerMessage = product.tickerMessage
        cartItemModel.variant = product.variant
        cartItemModel.variantParentId = product.variantParentId
        cartItemModel.productAlertMessage = product.productAlertMessage
        cartItemModel.productInformation = product.productInformation
        if (product.tradeInInfoData.isValidTradeIn) {
            cartItemModel.isValidTradeIn = true
            cartItemModel.oldDevicePrice = product.tradeInInfoData.oldDevicePrice
            cartItemModel.newDevicePrice = product.tradeInInfoData.newDevicePrice
            cartItemModel.deviceModel = product.tradeInInfoData.deviceModel
            cartItemModel.diagnosticId = product.tradeInInfoData.diagnosticId
        }
        if (product.purchaseProtectionPlanData.isProtectionAvailable) {
            val ppp = product.purchaseProtectionPlanData
            cartItemModel.isProtectionAvailable = ppp.isProtectionAvailable
            cartItemModel.protectionPricePerProduct = ppp.protectionPricePerProduct
            cartItemModel.protectionPrice = ppp.protectionPrice
            cartItemModel.protectionTitle = ppp.protectionTitle
            cartItemModel.protectionSubTitle = ppp.protectionSubtitle
            cartItemModel.protectionLinkText = ppp.protectionLinkText
            cartItemModel.protectionLinkUrl = ppp.protectionLinkUrl
            cartItemModel.isProtectionOptIn = ppp.isProtectionOptIn
            cartItemModel.isProtectionCheckboxDisabled = ppp.isProtectionCheckboxDisabled
        }
        cartItemModel.isBundlingItem = product.isBundlingItem
        cartItemModel.bundlingItemPosition = product.bundlingItemPosition
        cartItemModel.bundleId = product.bundleId
        cartItemModel.bundleGroupId = product.bundleGroupId
        cartItemModel.bundleType = product.bundleType
        cartItemModel.bundleTitle = product.bundleTitle
        cartItemModel.bundlePrice = product.bundlePrice
        cartItemModel.bundleSlashPriceLabel = product.bundleSlashPriceLabel
        cartItemModel.bundleOriginalPrice = product.bundleOriginalPrice
        cartItemModel.bundleQuantity = product.bundleQuantity
        cartItemModel.bundleIconUrl = product.bundleIconUrl
        cartItemModel.analyticsProductCheckoutData = product.analyticsProductCheckoutData
        cartItemModel.addOnProductLevelModel = product.addOnProduct
        cartItemModel.ethicalDrugDataModel = product.ethicalDrugs
        cartItemModel.addOnDefaultFrom = username
        cartItemModel.addOnDefaultTo = receiverName
//        cartItemModel.cartString = groupShop.cartString
        cartItemModel.warehouseId = groupShop.fulfillmentId.toString()
        cartItemModel.isTokoCabang = groupShop.isFulfillment
        cartItemModel.cartItemPosition = index
        cartItemModel.addOnOrderLevelModel = addOnWordingModel
        return cartItemModel
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
