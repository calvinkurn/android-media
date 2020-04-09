package com.tokopedia.purchase_platform.features.one_click_checkout.order.domain

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply.*
import com.tokopedia.purchase_platform.common.utils.isNullOrEmpty
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ShipProd
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ShopShipment
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.promo_checkout.PromoSAFResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.purchase_platform.features.one_click_checkout.common.STATUS_OK
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.GetOccCartGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProductDataResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ShopDataResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.card.OrderProductCard
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model.*
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject
import kotlin.math.min

class GetOccCartUseCase @Inject constructor(@ApplicationContext val context: Context, val graphqlUseCase: GraphqlUseCase<GetOccCartGqlResponse>) : UseCase<OrderData>() {

    override suspend fun executeOnBackground(): OrderData {
        graphqlUseCase.setTypeClass(GetOccCartGqlResponse::class.java)
        val graphqlRequest = GraphqlHelper.loadRawString(context.resources,
                R.raw.mutation_get_occ)
        graphqlUseCase.setGraphqlQuery(graphqlRequest)
        val response = graphqlUseCase.executeOnBackground()
        if (response.response.status.equals(STATUS_OK, true)) {
            if (response.response.data.cartList.isNotEmpty()) {
                val cart = response.response.data.cartList[0]
                val orderCart = OrderCart().apply {
                    product = generateOrderProduct(cart.product)
                    shop = generateOrderShop(cart.shop).apply {
                        cartResponse = cart
                        errors = cart.errors
                    }
                    kero = Kero(response.response.data.keroToken, response.response.data.keroDiscomToken, response.response.data.keroUnixTime)
                }
                return OrderData(orderCart, response.response.data.profileIndex, response.response.data.profileResponse, mapPromo(response.response.data.promo))
            } else if (response.response.data.errors.isNotEmpty()) {
                throw MessageErrorException(response.response.data.errors[0])
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

    private fun mapPromo(promo: PromoSAFResponse?): OrderPromo {
        val lastApply = promo?.lastApply
        val orderPromo = OrderPromo()
        if (lastApply?.data != null) {
            val lastApplyUiModel = LastApplyUiModel()
            // set codes
            val codes = lastApply.data.codes ?: emptyList()
            val mappedCodes = ArrayList<String>()
            for (code in codes) {
                if (code != null) {
                    mappedCodes.add(code)
                }
            }
            lastApplyUiModel.codes = mappedCodes
            // set voucher orders
            val listVoucherOrdersUiModel = ArrayList<LastApplyVoucherOrdersItemUiModel>()
            if (lastApply.data.voucherOrders != null) {
                for (i in lastApply.data.voucherOrders.indices) {
                    val lastApplyVoucherOrdersItemUiModel = LastApplyVoucherOrdersItemUiModel()
                    val (_, code, uniqueId, _, _, _, _, message) = lastApply.data.voucherOrders[i]!!
                    if (!code.isNullOrEmpty() && !uniqueId.isNullOrEmpty() && message != null && !message.color.isNullOrEmpty() && !message.state.isNullOrEmpty() && !message.text.isNullOrEmpty()) {
                        lastApplyVoucherOrdersItemUiModel.code = code
                        lastApplyVoucherOrdersItemUiModel.uniqueId = uniqueId
                        val lastApplyMessageInfoUiModel = LastApplyMessageUiModel()
                        lastApplyMessageInfoUiModel.color = message.color
                        lastApplyMessageInfoUiModel.state = message.state
                        lastApplyMessageInfoUiModel.text = message.text
                        lastApplyVoucherOrdersItemUiModel.message = lastApplyMessageInfoUiModel
                        listVoucherOrdersUiModel.add(lastApplyVoucherOrdersItemUiModel)
                    }
                }
                lastApplyUiModel.voucherOrders = listVoucherOrdersUiModel
            }
            // set additional info
            val responseAdditionalInfo = lastApply.data.additionalInfo
            val responseCartEmptyInfo = responseAdditionalInfo!!.cartEmptyInfo
            val errorDetail = responseAdditionalInfo.errorDetail
            val messageInfo = responseAdditionalInfo.messageInfo
            val lastApplyAdditionalInfoUiModel = LastApplyAdditionalInfoUiModel()
            val lastApplyEmptyCartInfoUiModel = LastApplyEmptyCartInfoUiModel()
            lastApplyEmptyCartInfoUiModel.detail = responseCartEmptyInfo!!.detail!!
            lastApplyEmptyCartInfoUiModel.imgUrl = responseCartEmptyInfo.imageUrl!!
            lastApplyEmptyCartInfoUiModel.message = responseCartEmptyInfo.message!!
            lastApplyAdditionalInfoUiModel.emptyCartInfo = lastApplyEmptyCartInfoUiModel
            val lastApplyErrorDetailUiModel = LastApplyErrorDetailUiModel()
            lastApplyErrorDetailUiModel.message = errorDetail!!.message!!
            lastApplyAdditionalInfoUiModel.errorDetail = lastApplyErrorDetailUiModel
            val lastApplyMessageInfoUiModel = LastApplyMessageInfoUiModel()
            lastApplyMessageInfoUiModel.detail = messageInfo!!.detail!!
            lastApplyMessageInfoUiModel.message = messageInfo.message!!
            lastApplyAdditionalInfoUiModel.messageInfo = lastApplyMessageInfoUiModel
            lastApplyAdditionalInfoUiModel.messageInfo = lastApplyMessageInfoUiModel
            lastApplyAdditionalInfoUiModel.errorDetail = lastApplyErrorDetailUiModel
            lastApplyAdditionalInfoUiModel.emptyCartInfo = lastApplyEmptyCartInfoUiModel
            // set usage summaries
            val listUsageSummaries = ArrayList<LastApplyUsageSummariesUiModel>()
            for ((desc, type, amountStr, amount) in responseAdditionalInfo.listUsageSummaries!!) {
                val lastApplyUsageSummariesUiModel = LastApplyUsageSummariesUiModel()
                lastApplyUsageSummariesUiModel.description = desc!!
                lastApplyUsageSummariesUiModel.type = type!!
                lastApplyUsageSummariesUiModel.amountStr = amountStr!!
                lastApplyUsageSummariesUiModel.amount = amount!!
                listUsageSummaries.add(lastApplyUsageSummariesUiModel)
            }
            lastApplyAdditionalInfoUiModel.usageSummaries = listUsageSummaries
            lastApplyUiModel.additionalInfo = lastApplyAdditionalInfoUiModel
            // set message
            if (lastApply.data.message != null) {
                val lastApplyMessage = lastApply.data.message
                val lastApplyMessageUiModel = LastApplyMessageUiModel()
                lastApplyMessageUiModel.text = lastApplyMessage.text!!
                lastApplyMessageUiModel.state = lastApplyMessage.state!!
                lastApplyMessageUiModel.color = lastApplyMessage.color!!
                lastApplyUiModel.message = lastApplyMessageUiModel
                val listRedStates = ArrayList<String>()
                if (lastApply.data.message.state != null) {
                    if (lastApply.data.message.state.equals(CheckoutConstant.STATE_RED, ignoreCase = true)) {
                        for (code in mappedCodes) {
                            listRedStates.add(code)
                        }
                    }
                    if (lastApply.data.voucherOrders != null) {
                        for (voucherOrdersItem in lastApply.data.voucherOrders) {
                            if (voucherOrdersItem?.message?.state.equals(CheckoutConstant.STATE_RED, ignoreCase = true) && voucherOrdersItem?.code != null) {
                                listRedStates.add(voucherOrdersItem.code)
                            }
                        }
                    }
                }
                lastApplyUiModel.listRedPromos = listRedStates
            }
            val listAllPromoCodes = ArrayList<String>()
            for (i in mappedCodes.indices) {
                listAllPromoCodes.add(mappedCodes[i])
            }
            if (lastApply.data.voucherOrders != null) {
                for (voucherOrdersItem in lastApply.data.voucherOrders) {
                    val element = voucherOrdersItem?.code
                    if (element != null) {
                        listAllPromoCodes.add(element)
                    }
                }
            }
            lastApplyUiModel.listAllPromoCodes = listAllPromoCodes
            orderPromo.lastApply = lastApplyUiModel
        }

        if (promo?.errorDefault != null && promo.errorDefault.title?.isEmpty() != true && promo.errorDefault.description?.isEmpty() != true) {
            val promoCheckoutErrorDefault = PromoCheckoutErrorDefault()
            promoCheckoutErrorDefault.title = promo.errorDefault.title ?: ""
            promoCheckoutErrorDefault.desc = promo.errorDefault.description ?: ""
            orderPromo.promoErrorDefault = promoCheckoutErrorDefault
        }
        return orderPromo
    }

    private fun generateShopShipment(shopShipments: List<ShopShipment>): ArrayList<com.tokopedia.logisticcart.shipping.model.ShopShipment> {
        val shopShipmentListResult = ArrayList<com.tokopedia.logisticcart.shipping.model.ShopShipment>()
        if (shopShipments.isNotEmpty()) {
            for (shopShipment in shopShipments) {
                val shopShipmentResult = com.tokopedia.logisticcart.shipping.model.ShopShipment().apply {
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
            shopName = shop.shopName ?: ""
            shopImage = shop.shopImage ?: ""
            shopUrl = shop.shopUrl ?: ""
            shopStatus = shop.shopStatus
            isGold = shop.isGold
            isGoldBadge = shop.isGoldBadge
            isOfficial = shop.isOfficial
            isFreeReturns = shop.isFreeReturns
            addressId = shop.addressId
            postalCode = shop.postalCode ?: ""
            latitude = shop.latitude ?: ""
            longitude = shop.longitude ?: ""
            districtId = shop.districtId
            districtName = shop.districtName ?: ""
            origin = shop.origin
            addressStreet = shop.addressStreet ?: ""
            provinceId = shop.provinceId
            cityId = shop.cityId
            cityName = shop.cityName ?: ""
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
            originalPrice = product.productOriginalPrice
            weight = product.productWeight
            isFreeOngkir = product.freeShipping.eligible
            freeOngkirImg = product.freeShipping.badgeUrl
            productResponse = product
            wholesalePrice = product.wholesalePrice
            quantity = mapQuantity(product)
            cashback = if (product.productCashback.isNotBlank()) "Cashback ${product.productCashback}" else ""
            notes = if (product.productNotes.length > OrderProductCard.MAX_NOTES_LENGTH) product.productNotes.substring(0, OrderProductCard.MAX_NOTES_LENGTH) else product.productNotes
        }
        return orderProduct
    }

    private fun mapQuantity(product: ProductDataResponse): QuantityUiModel {
        val quantityViewModel = QuantityUiModel()
        quantityViewModel.isStateError = false

        quantityViewModel.maxOrderQuantity = product.productMaxOrder
        quantityViewModel.maxOrderQuantity = if (product.productSwitchInvenage == 0) {
            product.productMaxOrder
        } else {
            min(product.productMaxOrder, product.productInvenageValue)
        }
        quantityViewModel.minOrderQuantity = product.productMinOrder
        quantityViewModel.orderQuantity = product.productQuantity
        quantityViewModel.stockWording = ""
        return quantityViewModel
    }
}