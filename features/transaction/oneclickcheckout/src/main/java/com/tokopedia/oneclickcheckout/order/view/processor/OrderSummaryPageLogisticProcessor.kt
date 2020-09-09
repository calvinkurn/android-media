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
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileShipment
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

    private fun setupShippingError(selectedShippingDurationViewModel: ShippingDurationUiModel?, shippingRecommendationData: ShippingRecommendationData, shipping: OrderShipment, curShip: OrderProfileShipment): OrderShipment {
        if (selectedShippingDurationViewModel == null && shippingRecommendationData.shippingDurationViewModels.isNotEmpty()) {
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DURATION_UNAVAILABLE)
            return OrderShipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = OrderSummaryPageViewModel.NO_DURATION_AVAILABLE, shippingRecommendationData = shippingRecommendationData)
        } else if (shippingRecommendationData.shippingDurationViewModels.isEmpty()) {
            return OrderShipment(serviceName = curShip.serviceName, serviceDuration = curShip.serviceDuration, serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE, shippingRecommendationData = null)
        }
        return shipping
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
                            null
                    )
                }
                if (shipping.serviceId != null && shipping.shipperProductId != null) {
                    val shippingDurationViewModels = shippingRecommendationData.shippingDurationViewModels
                    var selectedShippingDurationViewModel: ShippingDurationUiModel? = null
                    for (shippingDurationViewModel in shippingDurationViewModels) {
                        if (shippingDurationViewModel.serviceData.serviceId == shipping.serviceId) {
                            shippingDurationViewModel.isSelected = true
                            selectedShippingDurationViewModel = shippingDurationViewModel
                            val durationError = shippingDurationViewModel.serviceData.error
                            if (durationError.errorId != null && durationError.errorId.isNotBlank() && durationError.errorMessage.isNotBlank()) {
                                shippingErrorId = durationError.errorId
                                shipping = OrderShipment(
                                        serviceId = shippingDurationViewModel.serviceData.serviceId,
                                        serviceDuration = shippingDurationViewModel.serviceData.serviceName,
                                        serviceName = shippingDurationViewModel.serviceData.serviceName,
                                        needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
                                        serviceErrorMessage = durationError.errorMessage,
                                        shippingRecommendationData = shippingRecommendationData)
                            } else {
                                val shippingCourierViewModelList = shippingDurationViewModel.shippingCourierViewModelList
                                var selectedShippingCourierUiModel = shippingCourierViewModelList.first()
                                for (shippingCourierUiModel in shippingCourierViewModelList) {
                                    if (shippingCourierUiModel.productData.shipperProductId == shipping.shipperProductId) {
                                        shippingCourierUiModel.isSelected = true
                                        selectedShippingCourierUiModel = shippingCourierUiModel
                                    } else {
                                        shippingCourierUiModel.isSelected = false
                                    }
                                }
                                var flagNeedToSetPinpoint = false
                                var errorMessage: String? = null
                                if (selectedShippingCourierUiModel.productData.error != null && selectedShippingCourierUiModel.productData.error.errorMessage != null && selectedShippingCourierUiModel.productData.error.errorId != null) {
                                    shippingErrorId = selectedShippingCourierUiModel.productData.error.errorId
                                    errorMessage = selectedShippingCourierUiModel.productData.error.errorMessage
                                    if (selectedShippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
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
                                        serviceId = shippingDurationViewModel.serviceData.serviceId,
                                        serviceDuration = shippingDurationViewModel.serviceData.serviceName,
                                        serviceName = shippingDurationViewModel.serviceData.serviceName,
                                        shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                                        isApplyLogisticPromo = false,
                                        logisticPromoViewModel = null,
                                        logisticPromoShipping = null,
                                        shippingRecommendationData = shippingRecommendationData)
                            }
                        } else {
                            shippingDurationViewModel.isSelected = false
                        }
                    }
                    shipping = setupShippingError(selectedShippingDurationViewModel, shippingRecommendationData, shipping, curShip)
                } else {
                    val shippingDurationViewModels = shippingRecommendationData.shippingDurationViewModels
                    var selectedShippingDurationViewModel: ShippingDurationUiModel? = null
                    for (shippingDurationViewModel in shippingDurationViewModels) {
                        if (shippingDurationViewModel.serviceData.serviceId == curShip.serviceId) {
                            shippingDurationViewModel.isSelected = true
                            selectedShippingDurationViewModel = shippingDurationViewModel
                            val durationError: ErrorServiceData? = shippingDurationViewModel.serviceData.error
                            if (durationError?.errorId != null && durationError.errorId.isNotBlank() && durationError.errorMessage.isNotBlank()) {
                                shippingErrorId = durationError.errorId
                                shipping = OrderShipment(
                                        serviceId = shippingDurationViewModel.serviceData.serviceId,
                                        serviceDuration = shippingDurationViewModel.serviceData.serviceName,
                                        serviceName = shippingDurationViewModel.serviceData.serviceName,
                                        needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
                                        serviceErrorMessage = durationError.errorMessage,
                                        shippingRecommendationData = shippingRecommendationData)
                            } else {
                                val shippingCourierViewModelList = shippingDurationViewModel.shippingCourierViewModelList
                                var selectedShippingCourierUiModel = shippingCourierViewModelList.first()
                                for (shippingCourierUiModel in shippingCourierViewModelList) {
                                    if (shippingCourierUiModel.isSelected) {
                                        selectedShippingCourierUiModel = shippingCourierUiModel
                                    }
                                }
                                selectedShippingCourierUiModel.isSelected = true
                                var flagNeedToSetPinpoint = false
                                var errorMessage: String? = null
                                if (selectedShippingCourierUiModel.productData.error != null && selectedShippingCourierUiModel.productData.error.errorMessage != null && selectedShippingCourierUiModel.productData.error.errorId != null) {
                                    shippingErrorId = selectedShippingCourierUiModel.productData.error.errorId
                                    errorMessage = selectedShippingCourierUiModel.productData.error.errorMessage
                                    if (selectedShippingCourierUiModel.productData.error.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
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
                                        serviceId = shippingDurationViewModel.serviceData.serviceId,
                                        serviceDuration = shippingDurationViewModel.serviceData.serviceName,
                                        serviceName = shippingDurationViewModel.serviceData.serviceName,
                                        shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                                        shippingRecommendationData = shippingRecommendationData)

                                if (shipping.serviceErrorMessage.isNullOrEmpty()) {
                                    preselectedSpId = selectedShippingCourierUiModel.productData.shipperProductId.toString()
                                }
                            }
                        } else {
                            shippingDurationViewModel.isSelected = false
                        }
                    }
                    shipping = setupShippingError(selectedShippingDurationViewModel, shippingRecommendationData, shipping, curShip)
                }

                val logisticPromo: LogisticPromoUiModel? = shippingRecommendationData.logisticPromo
                if (logisticPromo != null && !logisticPromo.disabled) {
                    shipping = shipping.copy(logisticPromoViewModel = logisticPromo)
                    if (currPromo.isNotEmpty()) {
                        return@withContext ResultRates(
                                shipping,
                                if (logisticPromo.promoCode != currPromo) currPromo else null,
                                logisticPromo
                        )
                    }
                    shipping = shipping.copy(logisticPromoTickerMessage = if (shipping.serviceErrorMessage.isNullOrEmpty()) "Tersedia ${logisticPromo.title}" else null,
                            logisticPromoShipping = null, isApplyLogisticPromo = false)
                } else {
                    shipping = shipping.copy(logisticPromoTickerMessage = null, logisticPromoViewModel = null, logisticPromoShipping = null, isApplyLogisticPromo = false)
                }
                if (!shipping.serviceErrorMessage.isNullOrEmpty()) {
                    sendViewShippingErrorMessage(shippingErrorId)
                }
                sendPreselectedCourierOption(preselectedSpId)
                return@withContext ResultRates(
                        shipping,
                        if (currPromo.isNotEmpty()) currPromo else null,
                        null
                )
            } catch (t: Throwable) {
                return@withContext ResultRates(
                        OrderShipment(
                                serviceName = orderPreference.preference.shipment.serviceName,
                                serviceDuration = orderPreference.preference.shipment.serviceDuration,
                                serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE,
                                shippingRecommendationData = null
                        ),
                        null,
                        null
                )
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    private fun isDurationError(shippingDurationUiModel: ShippingDurationUiModel, shippingRecommendationData: ShippingRecommendationData): Triple<Boolean, String?, OrderShipment?> {
        val durationError: ErrorServiceData? = shippingDurationUiModel.serviceData.error
        if (durationError?.errorId != null && durationError.errorId.isNotBlank() && durationError.errorMessage.isNotBlank()) {
            return Triple(
                    true,
                    durationError.errorId,
                    OrderShipment(
                            serviceId = shippingDurationUiModel.serviceData.serviceId,
                            serviceDuration = shippingDurationUiModel.serviceData.serviceName,
                            serviceName = shippingDurationUiModel.serviceData.serviceName,
                            needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
                            serviceErrorMessage = durationError.errorMessage,
                            shippingRecommendationData = shippingRecommendationData)
            )
        }
        return Triple(false, null, null)
    }

    private fun sendViewShippingErrorMessage(shippingErrorId: String?) {
        if (shippingErrorId == ErrorProductData.ERROR_DISTANCE_LIMIT_EXCEEDED) {
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DISTANCE_EXCEED)
        } else if (shippingErrorId == ErrorProductData.ERROR_WEIGHT_LIMIT_EXCEEDED) {
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_WEIGHT_EXCEED)
        }
    }

    private fun sendPreselectedCourierOption(preselectedSpId: String?) {
        if (preselectedSpId != null) {
            orderSummaryAnalytics.eventViewPreselectedCourierOption(preselectedSpId, userSessionInterface.userId)
        }
    }
}

class ResultRates(
        val orderShipment: OrderShipment,
        val clearOldPromoCode: String?,
        val autoApplyPromo: LogisticPromoUiModel?
)