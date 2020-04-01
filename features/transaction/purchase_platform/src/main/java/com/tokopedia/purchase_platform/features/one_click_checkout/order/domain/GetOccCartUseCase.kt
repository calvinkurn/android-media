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
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.GetOccCartGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProductDataResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ShopDataResponse
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
        if (response.response.status.equals("OK", true)) {
            if (response.response.data.cartList.isNotEmpty()) {
                val orderCart = OrderCart()
                val cart = response.response.data.cartList[0]
                val profileIndex = response.response.data.profileIndex
                val orderProduct = generateOrderProduct(cart.product)
                orderCart.product = orderProduct
                val orderShop = generateOrderShop(cart.shop)
                generateShopShipment(orderShop, cart.shop.shopShipments)
                orderShop.cartResponse = cart
                orderCart.shop = orderShop
                orderCart.kero = Kero(response.response.data.keroToken, response.response.data.keroDiscomToken, response.response.data.keroUnixTime)
                val promo = response.response.data.promo
                return OrderData(orderCart, profileIndex, response.response.data.profileResponse, mapPromo(promo))
            } else if (response.response.data.errors.isNotEmpty()) {
                throw MessageErrorException(response.response.data.errors[0])
            } else {
                throw MessageErrorException("Terjadi kesalahan")
            }
        } else {
            if (response.response.errorMessages.isNotEmpty()) {
                throw MessageErrorException(response.response.errorMessages[0])
            } else {
                throw MessageErrorException("Terjadi kesalahan")
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
                    val (_, code, _, _, _, _, _, message) = lastApply.data.voucherOrders[i]!!
                    lastApplyVoucherOrdersItemUiModel.code = code!!
                    val lastApplyMessageInfoUiModel = LastApplyMessageUiModel()
                    lastApplyMessageInfoUiModel.color = message!!.color!!
                    lastApplyMessageInfoUiModel.state = message.state!!
                    lastApplyMessageInfoUiModel.text = message.text!!
                    lastApplyVoucherOrdersItemUiModel.message = lastApplyMessageInfoUiModel
                    listVoucherOrdersUiModel.add(lastApplyVoucherOrdersItemUiModel)
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

    private fun generateShopShipment(orderShop: OrderShop, shopShipments: List<ShopShipment>) {
        if (shopShipments.isNotEmpty()) {
            val shopShipmentListResult: MutableList<com.tokopedia.logisticcart.shipping.model.ShopShipment> = java.util.ArrayList()
            for (shopShipment in shopShipments) {
                val shopShipmentResult = com.tokopedia.logisticcart.shipping.model.ShopShipment()
                shopShipmentResult.isDropshipEnabled = shopShipment.isDropshipEnabled == 1
                shopShipmentResult.shipCode = shopShipment.shipCode
                shopShipmentResult.shipId = shopShipment.shipId
                shopShipmentResult.shipLogo = shopShipment.shipLogo
                shopShipmentResult.shipName = shopShipment.shipName
                if (!isNullOrEmpty<ShipProd>(shopShipment.shipProds)) {
                    val shipProdListResult: MutableList<com.tokopedia.logisticcart.shipping.model.ShipProd> = java.util.ArrayList()
                    for (shipProd in shopShipment.shipProds) {
                        val shipProdResult = com.tokopedia.logisticcart.shipping.model.ShipProd()
                        shipProdResult.additionalFee = shipProd.additionalFee
                        shipProdResult.minimumWeight = shipProd.minimumWeight
                        shipProdResult.shipGroupId = shipProd.shipGroupId
                        shipProdResult.shipGroupName = shipProd.shipGroupName
                        shipProdResult.shipProdId = shipProd.shipProdId
                        shipProdResult.shipProdName = shipProd.shipProdName
                        shipProdListResult.add(shipProdResult)
                    }
                    shopShipmentResult.shipProds = shipProdListResult
                }
                shopShipmentListResult.add(shopShipmentResult)
            }
            orderShop.shopShipment = shopShipmentListResult
        }
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
        }
//        mapVariant(orderProduct, product)
        mapQuantity(orderProduct, product)
        return orderProduct
    }

    private fun mapQuantity(orderProduct: OrderProduct, product: ProductDataResponse) {
        val quantityViewModel = QuantityUiModel()
//        quantityViewModel.errorFieldBetween = messagesModel?.get(Message.ERROR_FIELD_BETWEEN) ?: ""
//        quantityViewModel.errorFieldMaxChar = messagesModel?.get(Message.ERROR_FIELD_MAX_CHAR) ?: ""
//        quantityViewModel.errorFieldRequired = messagesModel?.get(Message.ERROR_FIELD_REQUIRED) ?: ""
//        quantityViewModel.errorProductAvailableStock = messagesModel?.get(Message.ERROR_PRODUCT_AVAILABLE_STOCK) ?: ""
//        quantityViewModel.errorProductAvailableStockDetail = messagesModel?.get(Message.ERROR_PRODUCT_AVAILABLE_STOCK_DETAIL) ?: ""
//        quantityViewModel.errorProductMaxQuantity = messagesModel?.get(Message.ERROR_PRODUCT_MAX_QUANTITY) ?: ""
//        quantityViewModel.errorProductMinQuantity = messagesModel?.get(Message.ERROR_PRODUCT_MIN_QUANTITY) ?: ""
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
        orderProduct.quantity = quantityViewModel
    }
}