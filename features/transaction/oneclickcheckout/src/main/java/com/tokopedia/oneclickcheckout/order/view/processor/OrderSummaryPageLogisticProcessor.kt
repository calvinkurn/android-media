package com.tokopedia.oneclickcheckout.order.view.processor

import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.ErrorServiceData
import com.tokopedia.oneclickcheckout.common.dispatchers.ExecutorDispatchers
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderPreference
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderSummaryPageLogisticProcessor @Inject constructor(private val ratesUseCase: GetRatesUseCase,
                                                            private val ratesResponseStateConverter: RatesResponseStateConverter,
                                                            private val userSessionInterface: UserSessionInterface,
                                                            private val orderSummaryAnalytics: OrderSummaryAnalytics,
                                                            private val executorDispatchers: ExecutorDispatchers) {

    private fun generateRatesParam(orderCart: OrderCart, orderPreference: OrderPreference, listShopShipment: List<ShopShipment>): RatesParam {
        return RatesParam.Builder(listShopShipment, generateShippingParam(orderCart, orderPreference)).build().apply {
            occ = "1"
        }
    }

    fun generateShippingParam(orderCart: OrderCart, orderPreference: OrderPreference): ShippingParam {
        return ShippingParam().apply {
            val address = orderPreference.preference.address
            val orderShop = orderCart.shop
            val orderProduct = orderCart.product
            val orderKero = orderCart.kero

            originDistrictId = orderShop.districtId.toString()
            originPostalCode = orderShop.postalCode
            originLatitude = orderShop.latitude
            originLongitude = orderShop.longitude
            destinationDistrictId = address.districtId.toString()
            destinationPostalCode = address.postalCode
            destinationLatitude = address.latitude
            destinationLongitude = address.longitude
            shopId = orderShop.shopId.toString()
            token = orderKero.keroToken
            ut = orderKero.keroUT
            insurance = 1
            isPreorder = orderProduct.isPreorder != 0
            categoryIds = orderProduct.categoryId.toString()
            uniqueId = orderCart.cartString
            addressId = address.addressId
            products = listOf(Product(orderProduct.productId.toLong(), orderProduct.isFreeOngkir))
            weightInKilograms = orderProduct.quantity.orderQuantity * orderProduct.weight / 1000.0
            productInsurance = orderProduct.productFinsurance
            orderValue = orderProduct.quantity.orderQuantity * orderProduct.getPrice()
        }
    }

    private fun getRatesDataFromLogisticPromo(serviceId: Int, list: List<ShippingDurationUiModel>): ShippingDurationUiModel? {
        return list.firstOrNull { it.serviceData.serviceId == serviceId }
    }

    private fun getCourierDataBySpId(spId: Int, shippingCourierViewModels: List<ShippingCourierUiModel>): ShippingCourierUiModel? {
        return shippingCourierViewModels.firstOrNull { it.productData.shipperProductId == spId }
    }

    private fun mapShippingRecommendationData(shippingRecommendationData: ShippingRecommendationData, orderShipment: OrderShipment, listShopShipment: List<ShopShipment>): ShippingRecommendationData {
        val data = ratesResponseStateConverter.fillState(shippingRecommendationData, listShopShipment, orderShipment.shipperProductId.toZeroIfNull(), orderShipment.serviceId.toZeroIfNull())
        if (data.shippingDurationViewModels != null) {
            val logisticPromo = data.logisticPromo
            if (logisticPromo != null) {
                // validate army courier
                val serviceData: ShippingDurationUiModel? = getRatesDataFromLogisticPromo(logisticPromo.serviceId, data.shippingDurationViewModels)
                if (serviceData == null) {
                    data.logisticPromo = null
                } else if (getCourierDataBySpId(logisticPromo.shipperProductId, serviceData.shippingCourierViewModelList) == null) {
                    data.logisticPromo = null
                }
            }
        }
        return data
    }

    suspend fun getRates(orderCart: OrderCart, orderPreference: OrderPreference, orderShipment: OrderShipment, listShopShipment: List<ShopShipment>): ResultRates {
        OccIdlingResource.increment()
        val result: ResultRates = withContext(executorDispatchers.io) {
            try {
                val shippingRecommendationData = ratesUseCase.execute(generateRatesParam(orderCart, orderPreference, listShopShipment))
                        .map { mapShippingRecommendationData(it, orderShipment, listShopShipment) }
                        .toBlocking().single()
                val curShip = orderPreference.preference.shipment
                var shipping = orderShipment
                val currPromo = if (shipping.isApplyLogisticPromo) shipping.logisticPromoViewModel?.promoCode
                        ?: "" else ""
                var shippingErrorId: String? = null
                var preselectedSpId: String? = null

                if (!shippingRecommendationData.errorId.isNullOrEmpty() && !shippingRecommendationData.errorMessage.isNullOrEmpty()) {
                    return@withContext ResultRates(
                            OrderShipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = shippingRecommendationData.errorMessage, shippingRecommendationData = null),
                            currPromo,
                            null, null, null
                    )
                }
                val shippingDurationUiModels: MutableList<ShippingDurationUiModel> = shippingRecommendationData.shippingDurationViewModels
                if (shippingDurationUiModels.isEmpty()) {
                    return@withContext ResultRates(
                            OrderShipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE, shippingRecommendationData = null),
                            currPromo,
                            null, null, null
                    )
                }
                if (shipping.serviceId != null && shipping.shipperProductId != null) {
                    shippingDurationUiModels.forEach {
                        it.isSelected = it.serviceData.serviceId == shipping.serviceId
                    }
                    val selectedShippingDurationUiModel = shippingDurationUiModels.firstOrNull { it.isSelected }
                    if (selectedShippingDurationUiModel == null) {
                        orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DURATION_UNAVAILABLE)
                        return@withContext ResultRates(
                                OrderShipment(serviceName = shipping.serviceName, serviceDuration = shipping.serviceDuration, serviceErrorMessage = OrderSummaryPageViewModel.NO_DURATION_AVAILABLE, shippingRecommendationData = shippingRecommendationData),
                                currPromo,
                                null, null, null
                        )
                    }
                    val durationError: ErrorServiceData? = selectedShippingDurationUiModel.serviceData.error
                    if (durationError?.errorId?.isNotBlank() == true && durationError.errorMessage?.isNotBlank() == true) {
                        shippingErrorId = durationError.errorId
                        shipping = OrderShipment(
                                serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                                serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                                serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                                needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
                                serviceErrorMessage = durationError.errorMessage,
                                shippingRecommendationData = shippingRecommendationData)
                    } else {
                        val shippingCourierViewModelList: MutableList<ShippingCourierUiModel> = selectedShippingDurationUiModel.shippingCourierViewModelList
                        shippingCourierViewModelList.forEach {
                            it.isSelected = it.productData.shipperProductId == shipping.shipperProductId
                        }
                        val selectedShippingCourierUiModel = shippingCourierViewModelList.firstOrNull { it.isSelected }
                                ?: shippingCourierViewModelList.first()
                        var flagNeedToSetPinpoint = false
                        var errorMessage: String? = null
                        val courierError: ErrorProductData? = selectedShippingCourierUiModel.productData.error
                        if (courierError?.errorMessage?.isNotBlank() == true && courierError.errorId != null) {
                            shippingErrorId = courierError.errorId
                            errorMessage = courierError.errorMessage
                            if (courierError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                                flagNeedToSetPinpoint = true
                            }
                        }
                        shipping = shipping.copy(shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                                shipperId = selectedShippingCourierUiModel.productData.shipperId,
                                ratesId = selectedShippingCourierUiModel.ratesId,
                                ut = selectedShippingCourierUiModel.productData.unixTime,
                                checksum = selectedShippingCourierUiModel.productData.checkSum,
                                shipperName = selectedShippingCourierUiModel.productData.shipperName,
                                needPinpoint = flagNeedToSetPinpoint,
                                serviceErrorMessage = if (flagNeedToSetPinpoint) OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE else errorMessage,
                                insuranceData = selectedShippingCourierUiModel.productData.insurance,
                                serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                                serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                                serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                                shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                                isApplyLogisticPromo = false,
                                logisticPromoViewModel = null,
                                logisticPromoShipping = null,
                                shippingRecommendationData = shippingRecommendationData)
                    }
                } else {
                    shippingDurationUiModels.forEach {
                        it.isSelected = it.serviceData.serviceId == curShip.serviceId
                    }
                    val selectedShippingDurationUiModel: ShippingDurationUiModel? = shippingDurationUiModels.firstOrNull { it.isSelected }
                    if (selectedShippingDurationUiModel == null) {
                        orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DURATION_UNAVAILABLE)
                        return@withContext ResultRates(
                                OrderShipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = OrderSummaryPageViewModel.NO_DURATION_AVAILABLE, shippingRecommendationData = shippingRecommendationData),
                                currPromo,
                                null, null, null
                        )
                    }
                    val durationError: ErrorServiceData? = selectedShippingDurationUiModel.serviceData.error
                    if (durationError?.errorId?.isNotBlank() == true && durationError.errorMessage?.isNotBlank() == true) {
                        shippingErrorId = durationError.errorId
                        shipping = OrderShipment(
                                serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                                serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                                serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                                needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
                                serviceErrorMessage = durationError.errorMessage,
                                shippingRecommendationData = shippingRecommendationData)
                    } else {
                        val shippingCourierViewModelList: MutableList<ShippingCourierUiModel> = selectedShippingDurationUiModel.shippingCourierViewModelList
                        val selectedShippingCourierUiModel = shippingCourierViewModelList.firstOrNull { it.isSelected }
                                ?: shippingCourierViewModelList.first()
                        selectedShippingCourierUiModel.isSelected = true
                        var flagNeedToSetPinpoint = false
                        var errorMessage: String? = null
                        val courierError: ErrorProductData? = selectedShippingCourierUiModel.productData.error
                        if (courierError?.errorMessage?.isNotBlank() == true && courierError.errorId != null) {
                            shippingErrorId = courierError.errorId
                            errorMessage = courierError.errorMessage
                            if (courierError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                                flagNeedToSetPinpoint = true
                            }
                        }
                        shipping = OrderShipment(shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                                shipperId = selectedShippingCourierUiModel.productData.shipperId,
                                ratesId = selectedShippingCourierUiModel.ratesId,
                                ut = selectedShippingCourierUiModel.productData.unixTime,
                                checksum = selectedShippingCourierUiModel.productData.checkSum,
                                shipperName = selectedShippingCourierUiModel.productData.shipperName,
                                needPinpoint = flagNeedToSetPinpoint,
                                serviceErrorMessage = if (flagNeedToSetPinpoint) OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE else errorMessage,
                                insuranceData = selectedShippingCourierUiModel.productData.insurance,
                                serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                                serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                                serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                                shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                                shippingRecommendationData = shippingRecommendationData)

                        if (shipping.serviceErrorMessage.isNullOrEmpty()) {
                            preselectedSpId = selectedShippingCourierUiModel.productData.shipperProductId.toString()
                        }
                    }
                }

                val logisticPromo: LogisticPromoUiModel? = shippingRecommendationData.logisticPromo
                if (logisticPromo != null && !logisticPromo.disabled) {
                    shipping = shipping.copy(logisticPromoViewModel = logisticPromo)
                    if (currPromo.isNotEmpty()) {
                        return@withContext ResultRates(
                                shipping,
                                if (logisticPromo.promoCode != currPromo) currPromo else "",
                                logisticPromo,
                                null,
                                null
                        )
                    }
                    shipping = shipping.copy(logisticPromoTickerMessage = if (shipping.serviceErrorMessage.isNullOrEmpty()) "Tersedia ${logisticPromo.title}" else null,
                            logisticPromoShipping = null, isApplyLogisticPromo = false)
                } else {
                    shipping = shipping.copy(logisticPromoTickerMessage = null, logisticPromoViewModel = null, logisticPromoShipping = null, isApplyLogisticPromo = false)
                }
                return@withContext ResultRates(
                        shipping,
                        currPromo,
                        null,
                        shippingErrorId,
                        preselectedSpId
                )
            } catch (t: Throwable) {
                return@withContext ResultRates(
                        OrderShipment(
                                serviceName = orderPreference.preference.shipment.serviceName,
                                serviceDuration = orderPreference.preference.shipment.serviceDuration,
                                serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE,
                                shippingRecommendationData = null
                        ),
                        "",
                        null,
                        null,
                        null
                )
            }
        }
        OccIdlingResource.decrement()
        return result
    }
}

class ResultRates(
        val orderShipment: OrderShipment,
        val clearOldPromoCode: String,
        val autoApplyPromo: LogisticPromoUiModel?,
        val shippingErrorId: String?,
        val preselectedSpId: String?
)