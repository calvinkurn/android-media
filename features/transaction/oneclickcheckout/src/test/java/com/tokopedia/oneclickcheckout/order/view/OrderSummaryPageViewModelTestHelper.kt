package com.tokopedia.oneclickcheckout.order.view

import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.*
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.oneclickcheckout.order.view.model.*

class OrderSummaryPageViewModelTestHelper {

    val firstCourierFirstDuration = ShippingCourierUiModel().apply {
        productData = ProductData().apply {
            shipperName = "kirimin"
            shipperId = 1
            shipperProductId = 1
            insurance = InsuranceData()
            price = PriceData().apply {
                price = 1000
            }
        }
        ratesId = "0"
    }

    val secondCourierFirstDuration = ShippingCourierUiModel().apply {
        productData = ProductData().apply {
            shipperName = "pakirim"
            shipperId = 2
            shipperProductId = 2
            insurance = InsuranceData()
            price = PriceData().apply {
                price = 1500
            }
        }
        ratesId = "0"
    }

    val firstDuration = ShippingDurationUiModel().apply {
        serviceData = ServiceData().apply {
            serviceId = 1
            serviceName = "durasi (1 hari)"
            error = ErrorServiceData()
        }
        shippingCourierViewModelList = listOf(firstCourierFirstDuration, secondCourierFirstDuration)
    }

    val firstCourierSecondDuration = ShippingCourierUiModel().apply {
        productData = ProductData().apply {
            shipperName = "pakirim"
            shipperId = 2
            shipperProductId = 3
            insurance = InsuranceData()
            price = PriceData().apply {
                price = 2000
            }
        }
        ratesId = "0"
    }

    val secondDuration = ShippingDurationUiModel().apply {
        serviceData = ServiceData().apply {
            serviceId = 2
            serviceName = "durasi (2 hari)"
            error = ErrorServiceData()
        }
        shippingCourierViewModelList = listOf(firstCourierSecondDuration)
    }

    val logisticPromo = LogisticPromoUiModel("bbo", "bbo", "bbo", firstCourierSecondDuration.productData.shipperName,
            secondDuration.serviceData.serviceId, firstCourierSecondDuration.productData.shipperId, firstCourierSecondDuration.productData.shipperProductId,
            "", "", "", false, "",
            500, 2000, 1500, false, false, CodDataPromo(), EstimatedTimeArrivalPromo(), "Bebas Ongkir (Rp 0)", "Bebas Ongkir", "Tersedia bbo", false)

    val shippingRecommendationData = ShippingRecommendationData().apply {
        shippingDurationUiModels = listOf(firstDuration, secondDuration)
        logisticPromo = this@OrderSummaryPageViewModelTestHelper.logisticPromo
    }

    val address = OrderProfileAddress(addressId = 1, latitude = "0", longitude = "0")

    val shipment = OrderProfileShipment(serviceId = 1)

    val payment = OrderProfilePayment(gatewayCode = "payment")

    val preference = OrderProfile(address = address, shipment = shipment, payment = payment)

    val orderShipment = OrderShipment(serviceId = firstDuration.serviceData.serviceId,
            serviceName = firstDuration.serviceData.serviceName,
            serviceDuration = firstDuration.serviceData.serviceName,
            shipperId = firstCourierFirstDuration.productData.shipperId,
            shipperProductId = firstCourierFirstDuration.productData.shipperProductId,
            shippingRecommendationData = shippingRecommendationData,
            logisticPromoViewModel = logisticPromo,
            logisticPromoTickerMessage = "Tersedia bbo")

    val product = OrderProduct(productId = Long.MAX_VALUE, orderQuantity = 1)

    val orderData = OrderData(cart = OrderCart(shop = OrderShop(shopId = Long.MAX_VALUE),products = mutableListOf(product)), preference = preference)
}