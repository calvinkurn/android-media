package com.tokopedia.oneclickcheckout.order.view.processor

import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.authentication.AuthHelper
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorServiceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.EstimatedTimeArrival
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceTextData
import com.tokopedia.logisticCommon.domain.param.EditAddressParam
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.*
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import com.tokopedia.oneclickcheckout.order.view.model.*
import com.tokopedia.usecase.RequestParams
import dagger.Lazy
import kotlinx.coroutines.withContext
import org.json.JSONException
import javax.inject.Inject

class OrderSummaryPageLogisticProcessor @Inject constructor(private val ratesUseCase: GetRatesUseCase,
                                                            private val ratesResponseStateConverter: RatesResponseStateConverter,
                                                            private val editAddressUseCase: Lazy<EditAddressUseCase>,
                                                            private val orderSummaryAnalytics: OrderSummaryAnalytics,
                                                            private val executorDispatchers: CoroutineDispatchers) {

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

            originDistrictId = orderShop.districtId
            originPostalCode = orderShop.postalCode
            originLatitude = orderShop.latitude
            originLongitude = orderShop.longitude
            destinationDistrictId = address.districtId.toString()
            destinationPostalCode = address.postalCode
            destinationLatitude = address.latitude
            destinationLongitude = address.longitude
            shopId = orderShop.shopId.toString()
            shopTier = orderShop.shopTier
            token = orderKero.keroToken
            ut = orderKero.keroUT
            insurance = 1
            isPreorder = orderProduct.isPreOrder != 0
            categoryIds = orderProduct.categoryId
            uniqueId = orderCart.cartString
            addressId = address.addressId.toString()
            products = listOf(Product(orderProduct.productId, orderProduct.isFreeOngkir, orderProduct.isFreeOngkirExtra))
            weightInKilograms = orderProduct.quantity.orderQuantity * orderProduct.weight / 1000.0
            weightActualInKilograms = orderProduct.quantity.orderQuantity * orderProduct.weightActual / 1000.0
            productInsurance = orderProduct.productFinsurance
            orderValue = orderProduct.quantity.orderQuantity * orderProduct.getPrice()
            isFulfillment = orderShop.isFulfillment
            preOrderDuration = orderProduct.preOrderDuration
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
                val profileShipment = orderPreference.preference.shipment
                var shipping = orderShipment
                val currPromo = if (shipping.isApplyLogisticPromo) shipping.logisticPromoViewModel?.promoCode
                        ?: "" else ""
                val shippingErrorId: String?
                var preselectedSpId: String? = null

                if (!shippingRecommendationData.errorId.isNullOrEmpty() && !shippingRecommendationData.errorMessage.isNullOrEmpty()) {
                    return@withContext ResultRates(
                            OrderShipment(serviceName = profileShipment.serviceName, serviceDuration = profileShipment.serviceDuration, serviceErrorMessage = shippingRecommendationData.errorMessage, shippingRecommendationData = null),
                            currPromo,
                            null, null, null
                    )
                }
                val shippingDurationUiModels: MutableList<ShippingDurationUiModel> = shippingRecommendationData.shippingDurationViewModels
                if (shippingDurationUiModels.isEmpty()) {
                    return@withContext ResultRates(
                            OrderShipment(serviceName = profileShipment.serviceName, serviceDuration = profileShipment.serviceDuration, serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE, shippingRecommendationData = null),
                            currPromo,
                            null, null, null
                    )
                }
                val isReload = shipping.serviceId != null && shipping.shipperProductId != null
                if (isReload) {
                    val (orderShipment1, errorId1) = onRenewShipping(shippingDurationUiModels, shipping, shippingRecommendationData)
                    shipping = orderShipment1
                    shippingErrorId = errorId1
                } else {
                    val (orderShipment3, errorId3, preselectedId3) = onRevampNewShipping(shippingDurationUiModels, profileShipment, shippingRecommendationData)
                    shipping = orderShipment3
                    shippingErrorId = errorId3
                    preselectedSpId = preselectedId3
                }

                val logisticPromo: LogisticPromoUiModel? = shippingRecommendationData.logisticPromo
                if (logisticPromo != null && !logisticPromo.disabled) {
                    shipping = shipping.copy(logisticPromoViewModel = logisticPromo)
                    if (currPromo.isNotEmpty() || (!isReload && profileShipment.isFreeShippingSelected)) {
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
                                serviceErrorMessage = OrderSummaryPageViewModel.FAIL_GET_RATES_ERROR_MESSAGE,
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

    private fun onRenewShipping(shippingDurationUiModels: List<ShippingDurationUiModel>, shipping: OrderShipment, shippingRecommendationData: ShippingRecommendationData): Pair<OrderShipment, String?> {
        shippingDurationUiModels.forEach {
            it.isSelected = it.serviceData.serviceId == shipping.serviceId
        }
        val selectedShippingDurationUiModel = shippingDurationUiModels.firstOrNull { it.isSelected }
        if (selectedShippingDurationUiModel == null) {
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DURATION_UNAVAILABLE)
            return Pair(
                    OrderShipment(
                            serviceName = shipping.serviceName,
                            serviceDuration = shipping.serviceDuration,
                            serviceErrorMessage = OrderSummaryPageViewModel.NO_DURATION_AVAILABLE,
                            shippingRecommendationData = shippingRecommendationData),
                    null)
        }
        val durationError: ErrorServiceData? = selectedShippingDurationUiModel.serviceData.error
        if (durationError?.errorId?.isNotBlank() == true && durationError.errorMessage?.isNotBlank() == true) {
            return Pair(
                    OrderShipment(
                            serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                            serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                            serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                            needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
                            serviceErrorMessage = durationError.errorMessage,
                            shippingRecommendationData = shippingRecommendationData),
                    durationError.errorId)
        }
        val shippingCourierViewModelList: MutableList<ShippingCourierUiModel> = selectedShippingDurationUiModel.shippingCourierViewModelList
        shippingCourierViewModelList.forEach {
            it.isSelected = it.productData.shipperProductId == shipping.shipperProductId
        }
        val selectedShippingCourierUiModel = shippingCourierViewModelList.firstOrNull { it.isSelected }
                ?: shippingCourierViewModelList.first()
        var flagNeedToSetPinpoint = false
        var errorMessage: String? = null
        var shippingErrorId: String? = null
        val courierError: ErrorProductData? = selectedShippingCourierUiModel.productData.error
        if (courierError?.errorMessage?.isNotBlank() == true && courierError.errorId != null) {
            shippingErrorId = courierError.errorId
            errorMessage = courierError.errorMessage
            if (courierError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                flagNeedToSetPinpoint = true
            }
        }
        return Pair(
                shipping.copy(shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                        shipperId = selectedShippingCourierUiModel.productData.shipperId,
                        ratesId = selectedShippingCourierUiModel.ratesId,
                        ut = selectedShippingCourierUiModel.productData.unixTime,
                        checksum = selectedShippingCourierUiModel.productData.checkSum,
                        shipperName = selectedShippingCourierUiModel.productData.shipperName,
                        needPinpoint = flagNeedToSetPinpoint,
                        serviceErrorMessage = if (flagNeedToSetPinpoint) OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE else errorMessage,
                        insuranceData = selectedShippingCourierUiModel.productData.insurance,
                        serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                        serviceEta = getShippingServiceETA(selectedShippingDurationUiModel.serviceData.texts),
                        shippingEta = getShippingCourierETA(selectedShippingCourierUiModel.productData.estimatedTimeArrival),
                        serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                        serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                        shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                        isApplyLogisticPromo = false,
                        logisticPromoViewModel = null,
                        logisticPromoShipping = null,
                        shippingRecommendationData = shippingRecommendationData),
                shippingErrorId)
    }

    private fun onRevampNewShipping(shippingDurationUiModels: List<ShippingDurationUiModel>, profileShipment: OrderProfileShipment, shippingRecommendationData: ShippingRecommendationData): Triple<OrderShipment, String?, String?> {
        shippingDurationUiModels.forEach {
            it.isSelected = it.serviceData.serviceId == profileShipment.serviceId
        }
        val selectedShippingDurationUiModel: ShippingDurationUiModel = shippingDurationUiModels.firstOrNull { it.isSelected }
                ?: return onRevampNewShippingFromRecommendation(shippingDurationUiModels, profileShipment, shippingRecommendationData)
        val durationError: ErrorServiceData? = selectedShippingDurationUiModel.serviceData.error
        if (durationError?.errorId?.isNotBlank() == true && durationError.errorMessage?.isNotBlank() == true) {
            return onRevampNewShippingFromRecommendation(shippingDurationUiModels, profileShipment, shippingRecommendationData)
        }
        val selectedShippingCourierUiModel = getSelectedCourierFromProfileSpId(profileShipment.spId, selectedShippingDurationUiModel.shippingCourierViewModelList)
        val flagNeedToSetPinpoint = false
        val errorMessage: String? = null
        val shippingErrorId: String? = null
        var preselectedSpId: String? = null
        val courierError: ErrorProductData? = selectedShippingCourierUiModel.productData.error
        if (courierError?.errorMessage?.isNotBlank() == true && courierError.errorId != null) {
            return onRevampNewShippingFromRecommendation(shippingDurationUiModels, profileShipment, shippingRecommendationData)
        } else if (profileShipment.spId <= 0) {
            preselectedSpId = selectedShippingCourierUiModel.productData.shipperProductId.toString()
        }
        selectedShippingDurationUiModel.shippingCourierViewModelList.forEach {
            it.isSelected = it.productData.shipperProductId == selectedShippingCourierUiModel.productData.shipperProductId
        }
        return Triple(
                OrderShipment(shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                        shipperId = selectedShippingCourierUiModel.productData.shipperId,
                        ratesId = selectedShippingCourierUiModel.ratesId,
                        ut = selectedShippingCourierUiModel.productData.unixTime,
                        checksum = selectedShippingCourierUiModel.productData.checkSum,
                        shipperName = selectedShippingCourierUiModel.productData.shipperName,
                        needPinpoint = flagNeedToSetPinpoint,
                        serviceErrorMessage = if (flagNeedToSetPinpoint) OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE else errorMessage,
                        insuranceData = selectedShippingCourierUiModel.productData.insurance,
                        serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                        serviceEta = getShippingServiceETA(selectedShippingDurationUiModel.serviceData.texts),
                        shippingEta = getShippingCourierETA(selectedShippingCourierUiModel.productData.estimatedTimeArrival),
                        serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                        serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                        shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                        shippingRecommendationData = shippingRecommendationData),
                shippingErrorId,
                preselectedSpId)
    }

    private fun onRevampNewShippingFromRecommendation(shippingDurationUiModels: List<ShippingDurationUiModel>, profileShipment: OrderProfileShipment, shippingRecommendationData: ShippingRecommendationData): Triple<OrderShipment, String?, String?> {
        var selectedShippingDurationUiModel: ShippingDurationUiModel? = null
        var selectedShippingCourierUiModel: ShippingCourierUiModel? = null
        for (shippingDurationUiModel in shippingDurationUiModels) {
            val shippingCourierViewModelList = shippingDurationUiModel.shippingCourierViewModelList
            shippingDurationUiModel.isSelected = shippingDurationUiModel.serviceData.serviceId == profileShipment.recommendationServiceId
            if (shippingDurationUiModel.isSelected) {
                for (shippingCourierUiModel in shippingCourierViewModelList) {
                    shippingCourierUiModel.isSelected = false
                    if (shippingCourierUiModel.productData.shipperProductId == profileShipment.recommendationSpId) {
                        shippingCourierUiModel.isSelected = true
                        selectedShippingCourierUiModel = shippingCourierUiModel
                        selectedShippingDurationUiModel = shippingDurationUiModel
                    }
                }
            }
        }
        if (selectedShippingDurationUiModel == null || selectedShippingCourierUiModel == null) {
            // Recommendation Courier not available
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DURATION_UNAVAILABLE)
            return Triple(
                    OrderShipment(serviceName = profileShipment.serviceName, serviceDuration = profileShipment.serviceDuration, serviceErrorMessage = OrderSummaryPageViewModel.NO_DURATION_AVAILABLE, shippingRecommendationData = shippingRecommendationData),
                    null,
                    null)
        }
        val durationError: ErrorServiceData? = selectedShippingDurationUiModel.serviceData.error
        if (durationError?.errorId?.isNotBlank() == true && durationError.errorMessage?.isNotBlank() == true) {
            return Triple(
                    OrderShipment(
                            serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                            serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                            serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                            needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
                            serviceErrorMessage = durationError.errorMessage,
                            shippingRecommendationData = shippingRecommendationData),
                    durationError.errorId,
                    null)
        }
        var flagNeedToSetPinpoint = false
        var errorMessage: String? = null
        var shippingErrorId: String? = null
        var preselectedSpId: String? = null
        val courierError: ErrorProductData? = selectedShippingCourierUiModel.productData.error
        if (courierError?.errorMessage?.isNotBlank() == true && courierError.errorId != null) {
            shippingErrorId = courierError.errorId
            errorMessage = courierError.errorMessage
            if (courierError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED) {
                flagNeedToSetPinpoint = true
            }
        } else {
            preselectedSpId = selectedShippingCourierUiModel.productData.shipperProductId.toString()
        }
        return Triple(
                OrderShipment(shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
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
                        serviceEta = getShippingServiceETA(selectedShippingDurationUiModel.serviceData.texts),
                        shippingEta = getShippingCourierETA(selectedShippingCourierUiModel.productData.estimatedTimeArrival),
                        serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                        shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                        shippingRecommendationData = shippingRecommendationData),
                shippingErrorId,
                preselectedSpId)
    }

    private fun getSelectedCourierFromProfileSpId(spId: Int, shippingCourierViewModelList: List<ShippingCourierUiModel>): ShippingCourierUiModel {
        return if (spId > 0) {
            shippingCourierViewModelList.firstOrNull { it.productData.shipperProductId == spId }
        } else {
            shippingCourierViewModelList.firstOrNull { it.isSelected }
        } ?: shippingCourierViewModelList.first()
    }

    private fun getShippingCourierETA(eta: EstimatedTimeArrival?): String? {
        return if (eta != null && eta.errorCode == 0) eta.textEta else null
    }

    private fun getShippingServiceETA(eta: ServiceTextData?): String? {
        return if (eta != null && eta.errorCode == 0) eta.textEtaSummarize else null
    }

    suspend fun savePinpoint(address: OrderProfileAddress, longitude: String, latitude: String, userId: String, deviceId: String): OccGlobalEvent {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val params = AuthHelper.generateParamsNetwork(userId, deviceId, TKPDMapParam())
                params[EditAddressParam.ADDRESS_ID] = address.addressId.toString()
                params[EditAddressParam.ADDRESS_NAME] = address.addressName
                params[EditAddressParam.ADDRESS_STREET] = address.addressStreet
                params[EditAddressParam.POSTAL_CODE] = address.postalCode
                params[EditAddressParam.DISTRICT_ID] = address.districtId.toString()
                params[EditAddressParam.CITY_ID] = address.cityId.toString()
                params[EditAddressParam.PROVINCE_ID] = address.provinceId.toString()
                params[EditAddressParam.LATITUDE] = latitude
                params[EditAddressParam.LONGITUDE] = longitude
                params[EditAddressParam.RECEIVER_NAME] = address.receiverName
                params[EditAddressParam.RECEIVER_PHONE] = address.phone

                val stringResponse = editAddressUseCase.get().createObservable(RequestParams.create().apply {
                    putAllString(params)
                }).toBlocking().single()
                var messageError: String? = null
                var statusSuccess: Boolean
                try {
                    val response = JsonParser().parse(stringResponse).asJsonObject
                    val statusCode = response.getAsJsonObject(EditAddressUseCase.RESPONSE_DATA)
                            .get(EditAddressUseCase.RESPONSE_IS_SUCCESS).asInt
                    statusSuccess = statusCode == 1
                    if (!statusSuccess) {
                        messageError = response.getAsJsonArray("message_error").get(0).asString
                    }
                } catch (e: JSONException) {
                    statusSuccess = false
                }

                if (statusSuccess) {
                    return@withContext OccGlobalEvent.TriggerRefresh()
                }

                if (messageError.isNullOrBlank()) {
                    messageError = DEFAULT_ERROR_MESSAGE
                }
                return@withContext OccGlobalEvent.Error(errorMessage = messageError)
            } catch (t: Throwable) {
                return@withContext OccGlobalEvent.Error(t.cause ?: t)
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    fun chooseCourier(chosenShippingCourierViewModel: ShippingCourierUiModel, shipping: OrderShipment): OrderShipment? {
        val shippingRecommendationData = shipping.shippingRecommendationData
        if (shippingRecommendationData != null) {
            val shippingDurationViewModels = shippingRecommendationData.shippingDurationViewModels
            shippingRecommendationData.logisticPromo = shippingRecommendationData.logisticPromo?.copy(isApplied = false)
            for (shippingDurationViewModel in shippingDurationViewModels) {
                if (shippingDurationViewModel.serviceData.serviceId == shipping.serviceId) {
                    shippingDurationViewModel.isSelected = true
                    val shippingCourierViewModelList = shippingDurationViewModel.shippingCourierViewModelList
                    var selectedShippingCourierUiModel: ShippingCourierUiModel? = null
                    for (shippingCourierUiModel in shippingCourierViewModelList) {
                        if (shippingCourierUiModel.productData.shipperProductId == chosenShippingCourierViewModel.productData.shipperProductId) {
                            selectedShippingCourierUiModel = shippingCourierUiModel
                        } else {
                            shippingCourierUiModel.isSelected = false
                        }
                    }
                    if (selectedShippingCourierUiModel != null) {
                        selectedShippingCourierUiModel.isSelected = true
                        return shipping.copy(
                                shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                                ratesId = selectedShippingCourierUiModel.ratesId,
                                ut = selectedShippingCourierUiModel.productData.unixTime,
                                checksum = selectedShippingCourierUiModel.productData.checkSum,
                                shipperId = selectedShippingCourierUiModel.productData.shipperId,
                                shipperName = selectedShippingCourierUiModel.productData.shipperName,
                                insuranceData = selectedShippingCourierUiModel.productData.insurance,
                                shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                                shippingEta = getShippingCourierETA(selectedShippingCourierUiModel.productData.estimatedTimeArrival),
                                shippingRecommendationData = shippingRecommendationData,
                                logisticPromoShipping = null,
                                isApplyLogisticPromo = false)
                    }
                }
            }
        }
        return null
    }

    fun chooseDuration(selectedServiceId: Int, selectedShippingCourierUiModel: ShippingCourierUiModel, flagNeedToSetPinpoint: Boolean, shipping: OrderShipment): OrderShipment? {
        val shippingRecommendationData = shipping.shippingRecommendationData
        if (shippingRecommendationData != null) {
            val shippingDurationViewModels = shippingRecommendationData.shippingDurationViewModels
            var selectedShippingDurationViewModel = shippingDurationViewModels[0]
            for (shippingDurationViewModel in shippingDurationViewModels) {
                if (shippingDurationViewModel.serviceData.serviceId == selectedServiceId) {
                    shippingDurationViewModel.isSelected = true
                    selectedShippingDurationViewModel = shippingDurationViewModel
                } else {
                    shippingDurationViewModel.isSelected = false
                }
            }
            shippingRecommendationData.logisticPromo = shippingRecommendationData.logisticPromo?.copy(isApplied = false)
            var newShipping = shipping.copy(
                    needPinpoint = flagNeedToSetPinpoint,
                    serviceErrorMessage = if (flagNeedToSetPinpoint) OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE else selectedShippingCourierUiModel.productData.error?.errorMessage,
                    isServicePickerEnable = !flagNeedToSetPinpoint,
                    serviceId = selectedShippingDurationViewModel.serviceData.serviceId,
                    serviceDuration = selectedShippingDurationViewModel.serviceData.serviceName,
                    serviceName = selectedShippingDurationViewModel.serviceData.serviceName,
                    shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                    ratesId = selectedShippingCourierUiModel.ratesId,
                    ut = selectedShippingCourierUiModel.productData.unixTime,
                    checksum = selectedShippingCourierUiModel.productData.checkSum,
                    shipperId = selectedShippingCourierUiModel.productData.shipperId,
                    shipperName = selectedShippingCourierUiModel.productData.shipperName,
                    insuranceData = selectedShippingCourierUiModel.productData.insurance,
                    shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                    serviceEta = getShippingServiceETA(selectedShippingDurationViewModel.serviceData.texts),
                    shippingEta = getShippingCourierETA(selectedShippingCourierUiModel.productData.estimatedTimeArrival),
                    shippingRecommendationData = shippingRecommendationData,
                    logisticPromoTickerMessage = null,
                    logisticPromoViewModel = null,
                    logisticPromoShipping = null,
                    isApplyLogisticPromo = false)

            if (newShipping.serviceErrorMessage.isNullOrEmpty()) {
                val logisticPromo: LogisticPromoUiModel? = shippingRecommendationData.logisticPromo
                if (logisticPromo != null && !logisticPromo.disabled) {
                    newShipping = newShipping.copy(logisticPromoTickerMessage = "Tersedia ${logisticPromo.title}", logisticPromoViewModel = logisticPromo, logisticPromoShipping = null)
                }
            }
            return newShipping
        }
        return null
    }

    fun onApplyBbo(shipping: OrderShipment, logisticPromoUiModel: LogisticPromoUiModel): Pair<OrderShipment?, OccGlobalEvent> {
        val shippingRecommendationData = shipping.shippingRecommendationData
        if (shippingRecommendationData != null) {
            var logisticPromoShipping: ShippingCourierUiModel? = null
            for (shippingDurationViewModel in shippingRecommendationData.shippingDurationViewModels) {
                if (shippingDurationViewModel.isSelected) {
                    for (shippingCourierUiModel in shippingDurationViewModel.shippingCourierViewModelList) {
                        shippingCourierUiModel.isSelected = false
                    }
                }
                if (shippingDurationViewModel.serviceData.serviceId == logisticPromoUiModel.serviceId) {
                    logisticPromoShipping = shippingDurationViewModel.shippingCourierViewModelList.firstOrNull { it.productData.shipperProductId == logisticPromoUiModel.shipperProductId }
                }
                shippingDurationViewModel.isSelected = false
            }
            if (logisticPromoShipping != null) {
                shippingRecommendationData.logisticPromo = shippingRecommendationData.logisticPromo.copy(isApplied = true)
                val needPinpoint = logisticPromoShipping.productData?.error?.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED
                return Pair(
                        shipping.copy(shippingRecommendationData = shippingRecommendationData,
                                isServicePickerEnable = true,
                                insuranceData = logisticPromoShipping.productData?.insurance,
                                serviceErrorMessage = if (needPinpoint) OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE else logisticPromoShipping.productData?.error?.errorMessage,
                                needPinpoint = needPinpoint,
                                logisticPromoTickerMessage = null,
                                isApplyLogisticPromo = true,
                                logisticPromoShipping = logisticPromoShipping),
                        OccGlobalEvent.Normal)
            }
        }
        return Pair(null, OccGlobalEvent.Error(errorMessage = OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE))
    }

    fun getRecommendedShipmentFromServiceId(orderShipment: OrderShipment, serviceId: Int): Pair<Int, Int>? {
        val shippingRecommendationData = orderShipment.shippingRecommendationData
        if (shippingRecommendationData != null) {
            val newDuration = shippingRecommendationData.shippingDurationViewModels.firstOrNull { it.serviceData.serviceId == serviceId }
            if (newDuration != null && newDuration.shippingCourierViewModelList.isNotEmpty()) {
                val newCourier = newDuration.shippingCourierViewModelList.firstOrNull { it.isSelected || it.productData.isRecommend }
                        ?: newDuration.shippingCourierViewModelList.first()
                return newCourier.productData.shipperId to newCourier.productData.shipperProductId
            }
        }
        return null
    }

    internal fun resetBbo(orderShipment: OrderShipment): OrderShipment {
        val logisticPromoViewModel = orderShipment.logisticPromoViewModel
        val logisticPromoShipping = orderShipment.logisticPromoShipping
        val shippingRecommendationData = orderShipment.shippingRecommendationData
        if (shippingRecommendationData != null && logisticPromoViewModel != null && orderShipment.isApplyLogisticPromo && logisticPromoShipping != null) {
            shippingRecommendationData.logisticPromo = shippingRecommendationData.logisticPromo.copy(isApplied = false)
            val shippingDuration = shippingRecommendationData.shippingDurationViewModels.first { it.serviceData.serviceId == logisticPromoShipping.serviceData.serviceId }
            shippingDuration.isSelected = true
            shippingDuration.shippingCourierViewModelList.first { it.productData.shipperProductId == logisticPromoShipping.productData.shipperProductId }.isSelected = true
            return orderShipment.copy(shippingRecommendationData = shippingRecommendationData,
                    isApplyLogisticPromo = false,
                    logisticPromoShipping = null,
                    logisticPromoTickerMessage = "Tersedia ${logisticPromoViewModel.title}")
        }
        return orderShipment
    }
}

class ResultRates(
        val orderShipment: OrderShipment,
        val clearOldPromoCode: String,
        val autoApplyPromo: LogisticPromoUiModel?,
        val shippingErrorId: String?,
        val preselectedSpId: String?
)