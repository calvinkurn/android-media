package com.tokopedia.oneclickcheckout.order.domain.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.oneclickcheckout.common.data.model.*
import com.tokopedia.oneclickcheckout.order.data.get.*
import com.tokopedia.oneclickcheckout.order.view.card.OrderProductCard
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.oneclickcheckout.order.view.model.ProductTrackerData
import com.tokopedia.oneclickcheckout.order.view.model.WholesalePrice
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.data.PurchaseProtectionPlanDataResponse
import com.tokopedia.purchase_platform.common.feature.purchaseprotection.domain.PurchaseProtectionPlanData
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.Ticker
import com.tokopedia.purchase_platform.common.feature.tickerannouncement.TickerData
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.math.min

class GetOccCartMapper @Inject constructor() {

    fun mapGetOccCartDataToOrderData(data: GetOccCartData): OrderData {
        val cart = data.cartList.first()
        val orderCart = OrderCart().apply {
            cartId = cart.cartId
            cartString = cart.cartString
            paymentProfile = cart.paymentProfile
            product = generateOrderProduct(cart.product).apply {
                quantity = mapQuantity(data)
                tickerMessage = mapProductTickerMessage(data.tickerMessage)
                purchaseProtectionPlanData = mapPurchaseProtectionPlanData(cart.purchaseProtectionPlanDataResponse)
            }
            shop = generateOrderShop(cart)
            kero = OrderKero(data.keroToken, data.keroDiscomToken, data.keroUnixTime)
        }
        return OrderData(mapTicker(data.tickers),
                data.occMainOnboarding,
                orderCart,
                data.profileIndex,
                data.profileRecommendation,
                mapProfile(data.profileResponse),
                LastApplyMapper.mapPromo(data.promo),
                mapOrderPayment(data),
                mapPrompt(data.prompt),
                mapOccRevamp(data.revamp),
                data.errorCode,
                data.popUpMessage,
                mapOccRemoveProfile(data.removeProfile))
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

    private fun generateOrderShop(cart: CartDataResponse): OrderShop {
        val shop = cart.shop
        return OrderShop().apply {
            shopId = shop.shopId
            userId = shop.userId
            shopName = shop.shopName
            shopBadge = when {
                shop.isOfficial == 1 -> shop.officialStore.osLogoUrl
                shop.isGoldBadge -> shop.goldMerchant.goldMerchantLogoUrl
                else -> ""
            }
            shopUrl = shop.shopUrl
            isGold = shop.isGold
            isOfficial = shop.isOfficial
            addressId = shop.addressId
            postalCode = shop.postalCode
            latitude = shop.latitude
            longitude = shop.longitude
            districtId = shop.districtId
            districtName = shop.districtName
            origin = shop.origin
            addressStreet = shop.addressStreet
            provinceId = shop.provinceId
            cityId = shop.cityId
            shopShipment = generateShopShipment(shop.shopShipments)
            errors = cart.errors
            isFulfillment = cart.warehouse.isFulfillment
            fulfillmentBadgeUrl = cart.tokoCabangInfo.badgeUrl
            cityName = if (cart.warehouse.isFulfillment) cart.tokoCabangInfo.message else shop.cityName
        }
    }

    private fun generateOrderProduct(product: ProductDataResponse): OrderProduct {
        val orderProduct = OrderProduct()
        orderProduct.apply {
            parentId = product.parentId
            productId = product.productId
            productName = product.productName
            productPrice = product.productPrice
            productImageUrl = product.productImage.imageSrc200Square
            maxOrderQuantity = product.productMaxOrder
            minOrderQuantity = product.productMinOrder
            originalPrice = product.productPriceOriginalFmt
            weight = product.productWeight
            isFreeOngkirExtra = product.freeShippingExtra.eligible
            isFreeOngkir = product.freeShipping.eligible
            freeOngkirImg = when {
                isFreeOngkirExtra -> product.freeShippingExtra.badgeUrl
                isFreeOngkir -> product.freeShipping.badgeUrl
                else -> ""
            }
            wholesalePrice = mapWholesalePrice(product.wholesalePrice)
            notes = if (product.productNotes.length > OrderProductCard.MAX_NOTES_LENGTH) {
                product.productNotes.substring(0, OrderProductCard.MAX_NOTES_LENGTH)
            } else {
                product.productNotes
            }
            cashback = if (product.productCashback.isNotBlank()) "Cashback ${product.productCashback}" else ""
            warehouseId = product.wareHouseId
            isPreorder = product.isPreorder
            categoryId = product.categoryId
            category = product.category
            campaignId = product.campaignId
            productFinsurance = product.productFinsurance
            isSlashPrice = product.productOriginalPrice > product.productPrice
            productTrackerData = ProductTrackerData(product.productTrackerData.attribution, product.productTrackerData.trackerListName)
            preorderDuration = product.productPreorder.durationDay.toIntOrZero()
        }
        return orderProduct
    }

    private fun mapWholesalePrice(wholesalePrice: List<com.tokopedia.oneclickcheckout.order.data.get.WholesalePrice>): List<WholesalePrice> {
        return wholesalePrice.map { WholesalePrice(it.qtyMinFmt, it.qtyMaxFmt, it.prdPrcFmt, it.qtyMin, it.qtyMax, it.prdPrc) }
    }

    private fun mapQuantity(data: GetOccCartData): QuantityUiModel {
        val product = data.cartList[0].product
        val quantityViewModel = QuantityUiModel()
        quantityViewModel.isStateError = false
        quantityViewModel.maxOrderQuantity = product.productMaxOrder
        quantityViewModel.maxOrderStock = if (product.productSwitchInvenage == 0) {
            product.productMaxOrder
        } else {
            min(product.productMaxOrder, product.productInvenageValue)
        }
        quantityViewModel.minOrderQuantity = product.productMinOrder
        quantityViewModel.orderQuantity = product.productQuantity
        quantityViewModel.errorProductMaxQuantity = data.messages.messageErrorMaxQuantity
        quantityViewModel.errorProductMinQuantity = data.messages.messageErrorMinQuantity
        quantityViewModel.errorProductAvailableStock = data.messages.messageErrorAvailableStock
        return quantityViewModel
    }

    private fun mapProductTickerMessage(tickerMessage: OccTickerMessage): ProductTickerMessage {
        return ProductTickerMessage(tickerMessage.message, tickerMessage.replacement.map { ProductTickerMessageReplacement(it.identifier, it.value) })
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

    private fun mapProfile(profileResponse: ProfileResponse): OrderProfile {
        return OrderProfile(profileResponse.onboardingHeaderMessage, profileResponse.onboardingComponent, profileResponse.hasPreference,
                profileResponse.profileRevampWording, profileResponse.isRecom,
                profileResponse.profileId, profileResponse.status, profileResponse.enable,
                profileResponse.message, mapAddress(profileResponse.address), mapPayment(profileResponse.payment),
                mapShipment(profileResponse.shipment))
    }

    private fun mapShipment(shipment: Shipment): OrderProfileShipment {
        return OrderProfileShipment(shipment.serviceName, shipment.serviceId, shipment.serviceDuration, shipment.spId, shipment.recommendationServiceId, shipment.recommendationSpId, shipment.isFreeShippingSelected)
    }

    private fun mapPayment(payment: Payment): OrderProfilePayment {
        return OrderProfilePayment(payment.enable, payment.active, payment.gatewayCode, payment.gatewayName, payment.image,
                payment.description, payment.metadata, payment.tickerMessage
        )
    }

    private fun mapOrderPayment(data: GetOccCartData): OrderPayment {
        val payment = data.profileResponse.payment
        return OrderPayment(payment.enable != 0, false, payment.gatewayCode, payment.gatewayName,
                payment.minimumAmount, payment.maximumAmount, payment.fee, payment.walletAmount,
                mapPaymentCreditCard(payment, data), mapPaymentErrorMessage(payment.errorMessage), mapPaymentRevampErrorMessage(payment.occRevampErrorMessage), data.errorTicker,
                payment.isEnableNextButton, payment.isDisablePayButton, payment.isOvoOnlyCampaign, mapPaymentOvoData(payment.ovoAdditionalData, data))
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
        return OrderPaymentCreditCard(mapPaymentCreditCardNumber(creditCard.numberOfCards), availableTerms, creditCard.bankCode, creditCard.cardType,
                creditCard.isExpired, creditCard.tncInfo, availableTerms.firstOrNull { it.isSelected }, mapPaymentCreditCardAdditionalData(data), payment.gatewayCode == OrderPaymentCreditCard.DEBIT_GATEWAY_CODE)
    }

    private fun mapPaymentCreditCardNumber(numberOfCards: PaymentCreditCardsNumber): OrderPaymentCreditCardsNumber {
        return OrderPaymentCreditCardsNumber(numberOfCards.availableCards, numberOfCards.unavailableCards,
                numberOfCards.totalCards)
    }

    private fun mapPaymentCreditCardAdditionalData(data: GetOccCartData): OrderPaymentCreditCardAdditionalData {
        return OrderPaymentCreditCardAdditionalData(data.customerData.id, data.customerData.name, data.customerData.email, data.customerData.msisdn,
                data.paymentAdditionalData.merchantCode, data.paymentAdditionalData.profileCode, data.paymentAdditionalData.signature,
                data.paymentAdditionalData.changeCcLink, data.paymentAdditionalData.callbackUrl)
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

    private fun mapPaymentOvoActionData(ovoActionData: OvoActionData): OrderPaymentOvoActionData {
        return OrderPaymentOvoActionData(ovoActionData.isRequired, ovoActionData.buttonTitle, ovoActionData.errorMessage, ovoActionData.errorTicker, ovoActionData.isHideDigital)
    }

    private fun mapPaymentOvoCustomerData(data: CustomerData): OrderPaymentOvoCustomerData {
        return OrderPaymentOvoCustomerData(data.name, data.email, data.msisdn)
    }

    private fun mapAddress(address: Address): OrderProfileAddress {
        return OrderProfileAddress(address.addressId, address.receiverName, address.addressName, address.addressStreet, address.districtId,
                address.districtName, address.cityId, address.cityName, address.provinceId, address.provinceName, address.phone, address.longitude,
                address.latitude, address.postalCode, address.state, address.stateDetail)
    }

    private fun mapTicker(tickers: List<Ticker>): TickerData? {
        val ticker = tickers.firstOrNull() ?: return null
        return TickerData(ticker.id, ticker.message, ticker.page, ticker.title)
    }

    private fun mapPrompt(promptResponse: OccPromptResponse): OccPrompt {
        return OccPrompt(promptResponse.type.toLowerCase(Locale.ROOT), promptResponse.title,
                promptResponse.description, promptResponse.imageUrl, promptResponse.buttons.map {
            OccPromptButton(it.text, it.link, it.action.toLowerCase(Locale.ROOT), it.color.toLowerCase(Locale.ROOT))
        })
    }

    private fun mapOccRevamp(revamp: OccRevampResponse): OccRevampData {
        return OccRevampData(revamp.isEnable, revamp.totalProfile, revamp.changeTemplateText)
    }

    private fun mapOccRemoveProfile(removeProfileResponse: OccRemoveProfileResponse): OccRemoveProfileData {
        return OccRemoveProfileData(removeProfileResponse.enable, removeProfileResponse.type,
                OccRemoveProfileMessageData(removeProfileResponse.message.title, removeProfileResponse.message.description))
    }
}