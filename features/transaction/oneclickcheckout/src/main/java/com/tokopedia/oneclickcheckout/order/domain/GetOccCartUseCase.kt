package com.tokopedia.oneclickcheckout.order.domain

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.common.data.model.*
import com.tokopedia.oneclickcheckout.order.data.get.*
import com.tokopedia.oneclickcheckout.order.domain.mapper.LastApplyMapper
import com.tokopedia.oneclickcheckout.order.view.card.OrderProductCard
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.oneclickcheckout.order.view.model.ProductTrackerData
import com.tokopedia.oneclickcheckout.order.view.model.WholesalePrice
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import kotlin.math.min

class GetOccCartUseCase @Inject constructor(val context: Context, val graphqlUseCase: GraphqlUseCase<GetOccCartGqlResponse>) : UseCase<OrderData>() {

    fun createRequestParams(source: String): RequestParams {
        return RequestParams.create().apply {
            putString(PARAM_SOURCE, source)
        }
    }

    override suspend fun executeOnBackground(): OrderData {
        graphqlUseCase.setTypeClass(GetOccCartGqlResponse::class.java)
        val graphqlRequest = GraphqlHelper.loadRawString(context.resources,
                R.raw.mutation_get_occ)
        graphqlUseCase.setGraphqlQuery(graphqlRequest)
        graphqlUseCase.setRequestParams(useCaseRequestParams.parameters)
        val response = graphqlUseCase.executeOnBackground()
        if (response.response.status.equals(STATUS_OK, true)) {
            if (response.response.data.errors.isNotEmpty()) {
                throw MessageErrorException(response.response.data.errors[0])
            } else if (response.response.data.cartList.isNotEmpty()) {
                val cart = response.response.data.cartList[0]
                val orderCart = OrderCart().apply {
                    cartId = cart.cartId
                    cartString = cart.cartString
                    product = generateOrderProduct(cart.product).apply {
                        quantity = mapQuantity(response.response.data)
                        tickerMessage = mapProductTickerMessage(response.response.data.tickerMessage)
                    }
                    shop = generateOrderShop(cart.shop).apply {
                        errors = cart.errors
                    }
                    kero = OrderKero(response.response.data.keroToken, response.response.data.keroDiscomToken, response.response.data.keroUnixTime)
                }
                return OrderData(response.response.data.occMainOnboarding, orderCart, response.response.data.profileIndex, response.response.data.profileRecommendation,
                        mapProfile(response.response.data.profileResponse), LastApplyMapper.mapPromo(response.response.data.promo))
            } else {
                throw MessageErrorException(DEFAULT_ERROR_MESSAGE)
            }
        } else {
            if (response.response.errorMessages.isNotEmpty()) {
                throw MessageErrorException(response.response.errorMessages[0])
            } else {
                throw MessageErrorException(DEFAULT_ERROR_MESSAGE)
            }
        }
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

    private fun generateOrderShop(shop: ShopDataResponse): OrderShop {
        return OrderShop().apply {
            shopId = shop.shopId
            userId = shop.userId
            shopName = shop.shopName
            shopImage = shop.shopImage
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
            cityName = shop.cityName
            shopShipment = generateShopShipment(shop.shopShipments)
        }
    }

    private fun generateOrderProduct(product: ProductDataResponse): OrderProduct {
        val orderProduct = OrderProduct()
        orderProduct.apply {
            parentId = product.parentId
            productId = product.productId
            productName = product.productName
            productPrice = product.productPrice
            productImageUrl = product.productImage.imageSrc
            maxOrderQuantity = product.productMaxOrder
            minOrderQuantity = product.productMinOrder
            originalPrice = product.productPriceOriginalFmt
            weight = product.productWeight
            isFreeOngkir = product.freeShipping.eligible
            freeOngkirImg = product.freeShipping.badgeUrl
            wholesalePrice = mapWholesalePrice(product.wholesalePrice)
            notes = if (product.productNotes.length > OrderProductCard.MAX_NOTES_LENGTH) product.productNotes.substring(0, OrderProductCard.MAX_NOTES_LENGTH) else product.productNotes
            cashback = if (product.productCashback.isNotBlank()) "Cashback ${product.productCashback}" else ""
            warehouseId = product.wareHouseId
            isPreorder = product.isPreorder
            categoryId = product.categoryId
            category = product.category
            productFinsurance = product.productFinsurance
            isSlashPrice = product.isSlashPrice
            productTrackerData = ProductTrackerData(product.productTrackerData.attribution, product.productTrackerData.trackerListName)
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

    private fun mapProfile(profileResponse: ProfileResponse): OrderProfile {
        return OrderProfile(profileResponse.onboardingHeaderMessage, profileResponse.onboardingComponent, profileResponse.hasPreference, profileResponse.isChangedProfile,
                profileResponse.profileId, profileResponse.status, profileResponse.enable, profileResponse.message, mapAddress(profileResponse.address),
                mapPayment(profileResponse.payment), mapShipment(profileResponse.shipment))
    }

    private fun mapShipment(shipment: Shipment): OrderProfileShipment {
        return OrderProfileShipment(shipment.serviceName, shipment.serviceId, shipment.serviceDuration)
    }

    private fun mapPayment(payment: Payment): OrderProfilePayment {
        return OrderProfilePayment(payment.enable, payment.active, payment.gatewayCode, payment.gatewayName, payment.image, payment.description,
                payment.url, payment.minimumAmount, payment.maximumAmount, payment.fee, payment.walletAmount, payment.metadata, payment.mdr,
                mapPaymentCreditCard(payment.creditCard), mapPaymentErrorMessage(payment.errorMessage)
        )
    }

    private fun mapPaymentErrorMessage(errorMessage: PaymentErrorMessage): OrderPaymentErrorMessage {
        return OrderPaymentErrorMessage(errorMessage.message,
                OrderPaymentErrorMessageButton(errorMessage.button.text, errorMessage.button.link)
        )
    }

    private fun mapPaymentCreditCard(creditCard: PaymentCreditCard): OrderPaymentCreditCard {
//        val availableTerms = mapPaymentInstallmentTerm(creditCard.availableTerms)
//        return OrderPaymentCreditCard(creditCard.totalCards, availableTerms, creditCard.bankCode, creditCard.cardType,
//                creditCard.isExpired, creditCard.tncInfo, availableTerms.firstOrNull { it.isSelected })
        val availableTerms = mapPaymentInstallmentTerm(listOf(
                InstallmentTerm(0, 1.5f, 0.5f, 10000, true),
                InstallmentTerm(3, 1.5f, 0.5f, 100000, false),
                InstallmentTerm(6, 1.5f, 0.5f, 200000, false)
        ))
        return OrderPaymentCreditCard(2, availableTerms, "bankCode", "cardType",
                false, "tncInfo", availableTerms.firstOrNull { it.isSelected })
    }

    private fun mapPaymentInstallmentTerm(availableTerms: List<InstallmentTerm>): List<OrderPaymentInstallmentTerm> {
        return availableTerms.map { OrderPaymentInstallmentTerm(it.term, it.mdr, it.mdrSubsidize, it.minAmount, it.isSelected) }
    }

    private fun mapAddress(address: Address): OrderProfileAddress {
        return OrderProfileAddress(address.addressId, address.receiverName, address.addressName, address.addressStreet, address.districtId,
                address.districtName, address.cityId, address.cityName, address.provinceId, address.provinceName, address.phone, address.longitude,
                address.latitude, address.postalCode)
    }

    companion object {
        private const val PARAM_SOURCE = "source"
    }
}