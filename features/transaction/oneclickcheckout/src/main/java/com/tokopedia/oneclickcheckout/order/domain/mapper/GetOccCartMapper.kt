package com.tokopedia.oneclickcheckout.order.domain.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.oneclickcheckout.order.data.get.*
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.oneclickcheckout.order.view.model.CourierSelectionError
import com.tokopedia.oneclickcheckout.order.view.model.ProductTrackerData
import com.tokopedia.oneclickcheckout.order.view.model.WholesalePrice
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.data.PurchaseProtectionPlanDataResponse
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import com.tokopedia.purchase_platform.common.utils.Utils
import javax.inject.Inject
import kotlin.math.min

class GetOccCartMapper @Inject constructor() {

    fun mapGetOccCartDataToOrderData(data: GetOccCartData): OrderData {
        val groupShop = data.groupShop.first()
        val orderCart = OrderCart().apply {
            cartString = groupShop.cartString
            paymentProfile = groupShop.paymentProfile
            shop = generateOrderShop(groupShop)
            val (productList, firstProductErrorIndex) = generateOrderProducts(groupShop, shop, data)
            products = productList
            shop.firstProductErrorIndex = firstProductErrorIndex
            kero = OrderKero(data.keroToken, data.keroDiscomToken, data.keroUnixTime)
        }
        return OrderData(
                ticker = mapTicker(data.tickers),
                onboarding = mapOnboarding(data.occMainOnboarding),
                cart = orderCart,
                preference = mapProfile(data.profileResponse, groupShop),
                promo = LastApplyMapper.mapPromo(data.promo),
                payment = mapOrderPayment(data),
                prompt = mapPrompt(data.prompt),
                errorCode = data.errorCode,
                popUpMessage = data.popUpMessage,
                totalProductPrice = data.totalProductPrice,
                profileCode = data.paymentAdditionalData.profileCode)
    }

    private fun generateShopShipment(shopShipments: List<OccShopShipment>): ArrayList<ShopShipment> {
        val shopShipmentListResult = ArrayList<ShopShipment>()
        if (shopShipments.isNotEmpty()) {
            for (shopShipment in shopShipments) {
                val shopShipmentResult = ShopShipment().apply {
                    isDropshipEnabled = shopShipment.isDropshipEnabled == 1
                    shipCode = shopShipment.shipCode
                    shipId = shopShipment.shipId
                    shipLogo = shopShipment.shipLogo
                    shipName = shopShipment.shipName
                }
                if (!shopShipment.shipProds.isNullOrEmpty()) {
                    val shipProdListResult = ArrayList<com.tokopedia.logisticcart.shipping.model.ShipProd>()
                    for (shipProd in shopShipment.shipProds) {
                        val shipProdResult = com.tokopedia.logisticcart.shipping.model.ShipProd().apply {
                            additionalFee = shipProd.additionalFee
                            minimumWeight = shipProd.minimumWeight
                            shipGroupId = shipProd.shipGroupId
                            shipGroupName = shipProd.shipGroupName
                            shipProdId = shipProd.shipProdId
                            shipProdName = shipProd.shipProdName
                        }
                        shipProdListResult.add(shipProdResult)
                    }
                    shopShipmentResult.shipProds = shipProdListResult
                }
                shopShipmentListResult.add(shopShipmentResult)
            }
        }
        return shopShipmentListResult
    }

    private fun generateOrderShop(groupShop: GroupShopOccResponse): OrderShop {
        val shop = groupShop.shop
        return OrderShop().apply {
            shopId = shop.shopId
            shopName = shop.shopName
            shopBadge = shop.shopType.badge
            shopTier = shop.shopType.shopTier
            shopTypeName = shop.shopType.title
            shopType = shop.shopType.titleFmt
            isGold = shop.isGold
            isOfficial = shop.isOfficial
            isTokoNow = shop.isTokoNow
            postalCode = shop.postalCode
            latitude = shop.latitude
            longitude = shop.longitude
            districtId = shop.districtId
            shopShipment = generateShopShipment(shop.shopShipments)
            errors = groupShop.errors
            warehouseId = groupShop.warehouse.warehouseId
            isFulfillment = groupShop.warehouse.isFulfillment
            fulfillmentBadgeUrl = groupShop.tokoCabangInfo.badgeUrl
            cityName = if (groupShop.warehouse.isFulfillment) groupShop.tokoCabangInfo.message else groupShop.shipmentInformation.shopLocation
            isFreeOngkirExtra = groupShop.shipmentInformation.freeShippingExtra.eligible
            isFreeOngkir = groupShop.shipmentInformation.freeShipping.eligible
            freeOngkirImg = when {
                isFreeOngkirExtra -> groupShop.shipmentInformation.freeShippingExtra.badgeUrl
                isFreeOngkir -> groupShop.shipmentInformation.freeShipping.badgeUrl
                else -> ""
            }
            preOrderLabel = if (groupShop.shipmentInformation.preorder.isPreorder) groupShop.shipmentInformation.preorder.duration else ""
            shopAlertMessage = shop.shopAlertMessage
            shopTicker = shop.shopTicker
            maximumWeightWording = shop.maximumWeightWording
            maximumWeight = shop.maximumShippingWeight
            unblockingErrorMessage = groupShop.unblockingErrors.firstOrNull() ?: ""
            boMetadata = groupShop.boMetadata
        }
    }

    private fun generateOrderProducts(groupShop: GroupShopOccResponse, shop: OrderShop, data: GetOccCartData): Pair<MutableList<OrderProduct>, Int> {
        val productList = ArrayList<OrderProduct>()
        var firstProductErrorIndex = -1
        val cartDetail = groupShop.cartDetails.first()
        for (index in cartDetail.products.indices) {
            val product = generateOrderProduct(cartDetail.products[index], shop, data)
            if (product.isError && firstProductErrorIndex == -1) {
                firstProductErrorIndex = index
            }
            productList.add(product)
        }
        return productList to firstProductErrorIndex
    }

    private fun generateOrderProduct(product: ProductDataResponse, shop: OrderShop, data: GetOccCartData): OrderProduct {
        val orderProduct = OrderProduct()
        orderProduct.apply {
            cartId = product.cartId
            productId = product.productId
            parentId = product.parentId
            productName = product.productName
            productPrice = product.productPrice
            finalPrice = product.productPrice
            productImageUrl = product.productImage.imageSrc200Square
            maxOrderQuantity = product.productMaxOrder
            minOrderQuantity = product.productMinOrder
            maxOrderStock = if (product.productSwitchInvenage == 0) {
                product.productMaxOrder
            } else {
                min(product.productMaxOrder, product.productInvenageValue)
            }
            orderQuantity = product.productQuantity
            originalPrice = product.productOriginalPrice
            initialPrice = product.initialPrice
            weight = product.productWeight
            weightActual = product.productWeightActual
            isFreeOngkirExtra = product.freeShippingExtra.eligible
            isFreeOngkir = product.freeShipping.eligible
            wholesalePriceList = mapWholesalePrice(product.wholesalePrice)
            maxCharNote = data.maxCharNote
            notes = if (product.productNotes.length > data.maxCharNote) {
                Utils.getHtmlFormat(product.productNotes.substring(0, data.maxCharNote))
            } else {
                Utils.getHtmlFormat(product.productNotes)
            }
            cashback = if (product.productCashback.isNotBlank()) "Cashback ${product.productCashback}" else ""
            warehouseId = product.warehouseId
            isPreOrder = product.isPreOrder
            categoryId = product.categoryId
            category = product.category
            campaignId = product.campaignId
            productFinsurance = product.productFinsurance
            isSlashPrice = product.productOriginalPrice > product.productPrice
            productTrackerData = ProductTrackerData(product.productTrackerData.attribution, product.productTrackerData.trackerListName)
            preOrderDuration = product.productPreorder.durationDay.toIntOrZero()
            purchaseProtectionPlanData = mapPurchaseProtectionPlanData(product.purchaseProtectionPlanDataResponse)
            variant = product.variantDescriptionDetail.variantName.joinToString(", ")
            productWarningMessage = product.productWarningMessage
            productAlertMessage = product.productAlertMessage
            slashPriceLabel = product.slashPriceLabel
            productInformation = product.productInformation.reversed()
            errorMessage = product.errors.firstOrNull() ?: ""
            isError = errorMessage.isNotEmpty() || shop.isError
        }
        return orderProduct
    }

    private fun mapWholesalePrice(wholesalePrice: List<com.tokopedia.oneclickcheckout.order.data.get.WholesalePrice>): List<WholesalePrice> {
        return wholesalePrice.map { WholesalePrice(it.qtyMinFmt, it.qtyMaxFmt, it.prdPrcFmt, it.qtyMin, it.qtyMax, it.prdPrc) }
    }

    private fun mapPurchaseProtectionPlanData(purchaseProtectionPlanDataResponse: PurchaseProtectionPlanDataResponse): PurchaseProtectionPlanData {
        return PurchaseProtectionPlanData(
                isProtectionAvailable = purchaseProtectionPlanDataResponse.protectionAvailable,
                protectionTypeId = purchaseProtectionPlanDataResponse.protectionTypeId,
                protectionPricePerProduct = purchaseProtectionPlanDataResponse.protectionPricePerProduct,
                protectionPrice = purchaseProtectionPlanDataResponse.protectionPrice,
                protectionTitle = purchaseProtectionPlanDataResponse.protectionTitle,
                protectionSubtitle = purchaseProtectionPlanDataResponse.protectionSubtitle,
                protectionLinkText = purchaseProtectionPlanDataResponse.protectionLinkText,
                protectionLinkUrl = purchaseProtectionPlanDataResponse.protectionLinkUrl,
                isProtectionOptIn = purchaseProtectionPlanDataResponse.protectionOptIn,
                isProtectionCheckboxDisabled = purchaseProtectionPlanDataResponse.protectionCheckboxDisabled,
                unit = purchaseProtectionPlanDataResponse.unit,
                source = purchaseProtectionPlanDataResponse.source
        )
    }

    private fun mapProfile(profileResponse: ProfileResponse, groupShop: GroupShopOccResponse): OrderProfile {
        return OrderProfile(
                address = mapAddress(profileResponse.address),
                shipment = mapShipment(profileResponse.shipment, groupShop),
                payment = mapPayment(profileResponse.payment)
        )
    }

    private fun mapAddress(address: Address): OrderProfileAddress {
        return OrderProfileAddress(
                addressId = address.addressId,
                receiverName = address.receiverName,
                addressName = address.addressName,
                addressStreet = address.addressStreet,
                districtId = address.districtId,
                districtName = address.districtName,
                cityId = address.cityId,
                cityName = address.cityName,
                provinceId = address.provinceId,
                provinceName = address.provinceName,
                phone = address.phone,
                longitude = address.longitude,
                latitude = address.latitude,
                postalCode = address.postalCode,
                state = address.state,
                stateDetail = address.stateDetail,
                status = address.status,
                tokoNow = OrderProfileAddressTokoNow(
                    isModified = address.tokoNow.isModified,
                    shopId = address.tokoNow.shopId,
                    warehouseId = address.tokoNow.warehouseId,
                    warehouses = address.tokoNow.warehouses,
                    serviceType = address.tokoNow.serviceType
                )
        )
    }

    private fun mapShipment(shipment: Shipment, groupShop: GroupShopOccResponse): OrderProfileShipment {
        return OrderProfileShipment(
                serviceName = shipment.serviceName,
                serviceId = shipment.serviceId,
                serviceDuration = shipment.serviceDuration,
                spId = shipment.spId,
                recommendationServiceId = shipment.recommendationServiceId,
                recommendationSpId = shipment.recommendationSpId,
                isFreeShippingSelected = shipment.isFreeShippingSelected,
                isDisableChangeCourier = groupShop.isDisableChangeCourier,
                autoCourierSelection = groupShop.autoCourierSelection,
                courierSelectionError = CourierSelectionError(groupShop.courierSelectionError.title, groupShop.courierSelectionError.description)
        )
    }

    private fun mapPayment(payment: Payment): OrderProfilePayment {
        return OrderProfilePayment(
                enable = payment.enable,
                active = payment.active,
                gatewayCode = payment.gatewayCode,
                gatewayName = payment.gatewayName,
                image = payment.image,
                description = payment.description,
                metadata = payment.metadata,
                tickerMessage = payment.tickerMessage
        )
    }

    private fun mapOrderPayment(data: GetOccCartData): OrderPayment {
        val payment = data.profileResponse.payment
        return OrderPayment(isEnable = payment.enable != 0,
                isCalculationError = false,
                gatewayCode = payment.gatewayCode,
                gatewayName = payment.gatewayName,
                minimumAmount = payment.minimumAmount,
                maximumAmount = payment.maximumAmount,
                fee = payment.fee,
                walletAmount = payment.walletAmount,
                creditCard = mapPaymentCreditCard(payment, data),
                errorMessage = mapPaymentErrorMessage(payment.errorMessage),
                revampErrorMessage = mapPaymentRevampErrorMessage(payment.occRevampErrorMessage),
                isDisablePayButton = payment.isDisablePayButton,
                isOvoOnlyCampaign = payment.isOvoOnlyCampaign,
                ovoData = mapPaymentOvoData(payment.ovoAdditionalData, data),
                walletErrorData = null,
                errorData = null,
                bid = payment.bid,
                specificGatewayCampaignOnlyType = payment.specificGatewayCampaignOnlyType,
                walletData = mapPaymentWalletData(payment.walletAdditionalData, data.paymentAdditionalData.callbackUrl)
        )
    }

    private fun mapPaymentErrorMessage(errorMessage: PaymentErrorMessage): OrderPaymentErrorMessage {
        return OrderPaymentErrorMessage(errorMessage.message,
                OrderPaymentErrorMessageButton(errorMessage.button.text, errorMessage.button.link)
        )
    }

    private fun mapPaymentRevampErrorMessage(errorMessage: PaymentRevampErrorMessage): OrderPaymentRevampErrorMessage {
        return OrderPaymentRevampErrorMessage(errorMessage.message,
                OrderPaymentRevampErrorMessageButton(errorMessage.button.text, errorMessage.button.action)
        )
    }

    private fun mapPaymentCreditCard(payment: Payment, data: GetOccCartData): OrderPaymentCreditCard {
        val creditCard = payment.creditCard
        val availableTerms = mapPaymentInstallmentTerm(creditCard.availableTerms)
        return OrderPaymentCreditCard(
                numberOfCards = mapPaymentCreditCardNumber(creditCard.numberOfCards),
                availableTerms = availableTerms,
                bankCode = creditCard.bankCode,
                cardType = creditCard.cardType,
                isExpired = creditCard.isExpired,
                tncInfo = creditCard.tncInfo,
                selectedTerm = availableTerms.firstOrNull { it.isSelected },
                additionalData = mapPaymentCreditCardAdditionalData(data),
                isDebit = payment.gatewayCode == OrderPaymentCreditCard.DEBIT_GATEWAY_CODE,
                isAfpb = creditCard.isAfpb,
                unixTimestamp = creditCard.unixTimestamp,
                tokenId = creditCard.tokenId,
                tenorSignature = creditCard.tenorSignature
        )
    }

    private fun mapPaymentCreditCardNumber(numberOfCards: PaymentCreditCardsNumber): OrderPaymentCreditCardsNumber {
        return OrderPaymentCreditCardsNumber(numberOfCards.availableCards, numberOfCards.unavailableCards,
                numberOfCards.totalCards)
    }

    private fun mapPaymentCreditCardAdditionalData(data: GetOccCartData): OrderPaymentCreditCardAdditionalData {
        return OrderPaymentCreditCardAdditionalData(data.customerData.id, data.customerData.name, data.customerData.email, data.customerData.msisdn,
                data.paymentAdditionalData.merchantCode, data.paymentAdditionalData.profileCode, data.paymentAdditionalData.signature,
                data.paymentAdditionalData.changeCcLink, data.paymentAdditionalData.callbackUrl, data.totalProductPrice)
    }

    private fun mapPaymentInstallmentTerm(availableTerms: List<InstallmentTerm>): List<OrderPaymentInstallmentTerm> {
        var hasSelection = false
        val installmentTerms = availableTerms.map {
            if (!hasSelection) {
                hasSelection = it.isSelected
            }
            OrderPaymentInstallmentTerm(it.term, it.mdr, it.mdrSubsidize, it.minAmount, it.isSelected)
        }.toMutableList()
        if (!hasSelection && installmentTerms.isNotEmpty()) {
            installmentTerms[0] = installmentTerms[0].copy(isSelected = true)
        }
        return installmentTerms
    }

    private fun mapPaymentOvoData(ovoAdditionalData: OvoAdditionalData, data: GetOccCartData): OrderPaymentOvoAdditionalData {
        return OrderPaymentOvoAdditionalData(
                activation = mapPaymentOvoActionData(ovoAdditionalData.ovoActivationData),
                topUp = mapPaymentOvoActionData(ovoAdditionalData.ovoTopUpData),
                phoneNumber = mapPaymentOvoActionData(ovoAdditionalData.phoneNumberRegistered),
                callbackUrl = data.paymentAdditionalData.callbackUrl,
                customerData = mapPaymentOvoCustomerData(data.customerData)
        )
    }

    private fun mapPaymentWalletData(walletAdditionalData: WalletAdditionalData, callbackUrl: String): OrderPaymentWalletAdditionalData {
        return OrderPaymentWalletAdditionalData(
                walletType = walletAdditionalData.walletType,
                enableWalletAmountValidation = walletAdditionalData.enableWalletAmountValidation,
                callbackUrl = callbackUrl,
                activation = mapPaymentWalletActionData(walletAdditionalData.activation),
                topUp = mapPaymentWalletActionData(walletAdditionalData.topUp),
                phoneNumber = mapPaymentWalletActionData(walletAdditionalData.phoneNumberRegistered),
                goCicilData = mapPaymentGoCicilData(walletAdditionalData.goCicilData),
        )
    }

    private fun mapPaymentOvoActionData(ovoActionData: OvoActionData): OrderPaymentOvoActionData {
        return OrderPaymentOvoActionData(
                isRequired = ovoActionData.isRequired,
                buttonTitle = ovoActionData.buttonTitle,
                errorMessage = ovoActionData.errorMessage,
                errorTicker = ovoActionData.errorTicker,
                isHideDigital = ovoActionData.isHideDigital
        )
    }

    private fun mapPaymentWalletActionData(walletData: WalletData): OrderPaymentWalletActionData {
        return OrderPaymentWalletActionData(
                isRequired = walletData.isRequired,
                buttonTitle = walletData.buttonTitle,
                successToaster = walletData.successToaster,
                errorToaster = walletData.errorToaster,
                errorMessage = walletData.errorMessage,
                isHideDigital = walletData.isHideDigital,
                headerTitle = walletData.headerTitle,
                urlLink = walletData.urlLink
        )
    }

    private fun mapPaymentGoCicilData(goCicilData: GoCicilData): OrderPaymentGoCicilData {
        return OrderPaymentGoCicilData(
                errorMessageInvalidTenure = goCicilData.errorMessageInvalidTenure,
                errorMessageBottomLimit = goCicilData.errorMessageBottomLimit,
                errorMessageTopLimit = goCicilData.errorMessageTopLimit,
                errorMessageUnavailableTenures = goCicilData.errorMessageUnavailableTenures,
                selectedTenure = goCicilData.selectedTenure,
        )
    }

    private fun mapPaymentOvoCustomerData(data: CustomerData): OrderPaymentOvoCustomerData {
        return OrderPaymentOvoCustomerData(data.name, data.email, data.msisdn)
    }

    private fun mapTicker(tickers: List<Ticker>): TickerData? {
        val ticker = tickers.firstOrNull() ?: return null
        return TickerData(ticker.id, ticker.message, ticker.page, ticker.title)
    }

    private fun mapOnboarding(onboardingResponse: OccMainOnboardingResponse): OccOnboarding {
        return OccOnboarding(
                isForceShowCoachMark = onboardingResponse.isForceShowCoachMark,
                isShowOnboardingTicker = onboardingResponse.isShowOnboardingTicker,
                coachmarkType = onboardingResponse.coachmarkType,
                onboardingTicker = OccOnboardingTicker(
                        title = onboardingResponse.onboardingTicker.title,
                        message = onboardingResponse.onboardingTicker.message,
                        image = onboardingResponse.onboardingTicker.image,
                        showActionButton = onboardingResponse.onboardingTicker.showActionButton,
                        actionText = onboardingResponse.onboardingTicker.actionText,
                ),
                onboardingCoachMark = OccOnboardingCoachMark(
                        skipButtonText = onboardingResponse.onboardingCoachMark.skipButtonText,
                        details = onboardingResponse.onboardingCoachMark.details.map {
                            OccOnboardingCoachMarkDetail(
                                    step = it.step,
                                    title = it.title,
                                    message = it.message
                            )
                        }
                )
        )
    }

    private fun mapPrompt(promptResponse: OccPromptResponse): OccPrompt {
        return OccPrompt(promptResponse.type.lowercase(), promptResponse.title,
                promptResponse.description, promptResponse.imageUrl, promptResponse.buttons.map {
            OccPromptButton(it.text, it.link, it.action.lowercase(), it.color.lowercase())
        })
    }
}