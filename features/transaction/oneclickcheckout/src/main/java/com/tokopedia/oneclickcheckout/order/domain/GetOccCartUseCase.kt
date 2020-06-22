package com.tokopedia.oneclickcheckout.order.domain

import android.content.Context
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.STATUS_OK
import com.tokopedia.oneclickcheckout.order.data.GetOccCartGqlResponse
import com.tokopedia.oneclickcheckout.order.data.ProductDataResponse
import com.tokopedia.oneclickcheckout.order.data.ShopDataResponse
import com.tokopedia.oneclickcheckout.order.data.ShopShipment
import com.tokopedia.oneclickcheckout.order.domain.mapper.LastApplyMapper
import com.tokopedia.oneclickcheckout.order.view.card.OrderProductCard
import com.tokopedia.oneclickcheckout.order.view.model.*
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
                    product = generateOrderProduct(cart.product)
                    shop = generateOrderShop(cart.shop).apply {
                        cartResponse = cart
                        errors = cart.errors
                    }
                    kero = Kero(response.response.data.keroToken, response.response.data.keroDiscomToken, response.response.data.keroUnixTime)
                }
                return OrderData(response.response.data.occMainOnboarding, orderCart, response.response.data.profileIndex, response.response.data.profileRecommendation,
                        response.response.data.profileResponse, LastApplyMapper.mapPromo(response.response.data.promo))
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

    companion object {
        private const val PARAM_SOURCE = "source"
    }
}