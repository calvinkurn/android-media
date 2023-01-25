package com.tokopedia.oneclickcheckout.order.view.processor

import com.google.gson.JsonParser
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.mapper.ChooseAddressMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorProductData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ErrorServiceData
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.EstimatedTimeArrival
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceTextData
import com.tokopedia.logisticCommon.domain.param.EditAddressParam
import com.tokopedia.logisticCommon.domain.usecase.EditAddressUseCase
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingParam
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticcart.shipping.usecase.GetRatesUseCase
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.network.utils.TKPDMapParam
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.OccGlobalEvent
import com.tokopedia.oneclickcheckout.order.analytics.OrderSummaryAnalytics
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageViewModel.Companion.SAVE_PINPOINT_SUCCESS_MESSAGE
import com.tokopedia.oneclickcheckout.order.view.model.OrderCart
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.oneclickcheckout.order.view.model.OrderInsurance
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfile
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileAddress
import com.tokopedia.oneclickcheckout.order.view.model.OrderProfileShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderShipment
import com.tokopedia.oneclickcheckout.order.view.model.OrderShop
import com.tokopedia.purchase_platform.common.utils.isBlankOrZero
import com.tokopedia.usecase.RequestParams
import dagger.Lazy
import kotlinx.coroutines.withContext
import org.json.JSONException
import javax.inject.Inject

class OrderSummaryPageLogisticProcessor @Inject constructor(
    private val ratesUseCase: GetRatesUseCase,
    private val ratesResponseStateConverter: RatesResponseStateConverter,
    private val chooseAddressRepository: Lazy<ChooseAddressRepository>,
    private val chooseAddressMapper: Lazy<ChooseAddressMapper>,
    private val editAddressUseCase: Lazy<EditAddressUseCase>,
    private val orderSummaryAnalytics: OrderSummaryAnalytics,
    private val executorDispatchers: CoroutineDispatchers
) {

    private fun generateRatesParam(
        orderCart: OrderCart,
        orderProfile: OrderProfile,
        orderCost: OrderCost,
        listShopShipment: List<ShopShipment>
    ): Pair<RatesParam?, Double> {
        val (shipping, overweight) = generateShippingParam(orderCart, orderProfile, orderCost)
        if (shipping == null) return null to overweight
        return RatesParam.Builder(listShopShipment, shipping).cartData(orderCart.cartData).build()
            .apply {
                occ = "1"
            } to 0.0
    }

    private fun generateShippingParam(
        orderCart: OrderCart, orderProfile: OrderProfile, orderCost: OrderCost
    ): Pair<ShippingParam?, Double> {
        val address = orderProfile.address
        val orderShop = orderCart.shop
        val orderProducts = orderCart.products
        val orderKero = orderCart.kero

        var totalWeight = 0.0
        var totalWeightActual = 0.0
        var productFInsurance = 0
        var preOrder = false
        var productPreOrderDuration = 0
        val productList: ArrayList<Product> = ArrayList()
        val categoryList: ArrayList<String> = ArrayList()
        orderProducts.forEach {
            if (!it.isError) {
                totalWeight += it.orderQuantity * it.weight
                totalWeightActual += if (it.weightActual > 0) {
                    it.orderQuantity * it.weightActual
                } else {
                    it.orderQuantity * it.weight
                }
                if (it.productFinsurance == 1) {
                    productFInsurance = 1
                }
                preOrder = it.isPreOrder != 0
                productPreOrderDuration = it.preOrderDuration
                categoryList.add(it.categoryId)
                productList.add(Product(it.productId.toLongOrZero(), it.isFreeOngkir, it.isFreeOngkirExtra))
            }
        }
        if (orderShop.shouldValidateWeight() && totalWeight > orderShop.maximumWeight) {
            // overweight
            return null to (totalWeight - orderShop.maximumWeight)
        }
        return ShippingParam().apply {
            originDistrictId = orderShop.districtId
            originPostalCode = orderShop.postalCode
            originLatitude = orderShop.latitude
            originLongitude = orderShop.longitude
            destinationDistrictId = address.districtId
            destinationPostalCode = address.postalCode
            destinationLatitude = address.latitude
            destinationLongitude = address.longitude
            shopId = orderShop.shopId
            shopTier = orderShop.shopTier
            token = orderKero.keroToken
            ut = orderKero.keroUT
            insurance = 1
            isPreorder = preOrder
            categoryIds = categoryList.joinToString(",")
            uniqueId = orderCart.cartString
            addressId = address.addressId
            products = productList
            weightInKilograms = totalWeight / OrderShop.WEIGHT_KG_DIVIDER
            weightActualInKilograms = totalWeightActual / OrderShop.WEIGHT_KG_DIVIDER
            productInsurance = productFInsurance
            orderValue = orderCost.totalItemPrice.toLong()
            isFulfillment = orderShop.isFulfillment
            preOrderDuration = productPreOrderDuration
            boMetadata = orderShop.boMetadata
        } to 0.0
    }

    private fun mapShippingRecommendationData(
        shippingRecommendationData: ShippingRecommendationData,
        orderShipment: OrderShipment,
        listShopShipment: List<ShopShipment>,
        shipmentProfile: OrderProfileShipment
    ): ShippingRecommendationData {
        return ratesResponseStateConverter.fillState(
            shippingRecommendationData,
            listShopShipment,
            orderShipment.shipperProductId.toZeroIfNull(),
            orderShipment.serviceId.toZeroIfNull()
        )
    }

    suspend fun getRates(
        orderCart: OrderCart,
        orderProfile: OrderProfile,
        orderShipment: OrderShipment,
        orderCost: OrderCost,
        listShopShipment: List<ShopShipment>
    ): ResultRates {
        OccIdlingResource.increment()
        val result: ResultRates = withContext(executorDispatchers.io) {
            try {
                val (param, overweight) = generateRatesParam(
                    orderCart, orderProfile, orderCost, listShopShipment
                )
                if (param == null) {
                    // overweight
                    return@withContext ResultRates(
                        orderShipment = orderShipment.copy(
                            isLoading = false,
                            serviceErrorMessage = OrderSummaryPageViewModel.FAIL_GET_RATES_ERROR_MESSAGE
                        ), overweight = overweight
                    )
                }
                val shippingRecommendationData = ratesUseCase.execute(param).map {
                    mapShippingRecommendationData(
                        it, orderShipment, listShopShipment, orderProfile.shipment
                    )
                }.toBlocking().single()
                val profileShipment = orderProfile.shipment
                var shipping = orderShipment
                val currPromo =
                    if (shipping.isApplyLogisticPromo) shipping.logisticPromoViewModel?.promoCode
                        ?: "" else ""
                val shippingErrorId: String?
                var preselectedSpId: String? = null

                if (!shippingRecommendationData.errorId.isNullOrEmpty() && !shippingRecommendationData.errorMessage.isNullOrEmpty()) {
                    return@withContext ResultRates(
                        OrderShipment(
                            isLoading = false,
                            serviceName = profileShipment.serviceName,
                            serviceDuration = profileShipment.serviceDuration,
                            serviceErrorMessage = shippingRecommendationData.errorMessage,
                            shippingRecommendationData = null
                        ), currPromo, null, null, null
                    )
                }
                val shippingDurationUiModels: List<ShippingDurationUiModel> =
                    shippingRecommendationData.shippingDurationUiModels
                if (shippingDurationUiModels.isEmpty()) {
                    return@withContext ResultRates(
                        OrderShipment(
                            isLoading = false,
                            serviceName = profileShipment.serviceName,
                            serviceDuration = profileShipment.serviceDuration,
                            serviceErrorMessage = OrderSummaryPageViewModel.NO_COURIER_SUPPORTED_ERROR_MESSAGE,
                            shippingRecommendationData = null
                        ), currPromo, null, null, null
                    )
                }
                val isReload = shipping.serviceId != null && shipping.shipperProductId != null
                if (isReload) {
                    val (orderShipment1, errorId1) = onRenewShipping(
                        shippingDurationUiModels,
                        shipping,
                        shippingRecommendationData
                    )
                    shipping = orderShipment1
                    shippingErrorId = errorId1
                } else {
                    val (orderShipment3, errorId3, preselectedId3) = onRevampNewShipping(
                        shippingDurationUiModels, profileShipment, shippingRecommendationData
                    )
                    shipping = orderShipment3
                    shippingErrorId = errorId3
                    preselectedSpId = preselectedId3
                }

                val logisticPromo: LogisticPromoUiModel? = shippingRecommendationData.logisticPromo
                if (logisticPromo != null && !logisticPromo.disabled) {
                    shipping = shipping.copy(logisticPromoViewModel = logisticPromo)
                    if (currPromo.isNotEmpty() || (!isReload && profileShipment.isFreeShippingSelected) || profileShipment.isDisableChangeCourier) {
                        return@withContext ResultRates(
                            shipping,
                            if (logisticPromo.promoCode != currPromo) currPromo else "",
                            logisticPromo,
                            null,
                            null
                        )
                    }
                    shipping = shipping.copy(
                        logisticPromoTickerMessage = if (shipping.serviceErrorMessage.isNullOrEmpty()) constructBboTickerTitle(
                            logisticPromo
                        ) else null, logisticPromoShipping = null, isApplyLogisticPromo = false
                    )
                } else if (logisticPromo != null && profileShipment.isDisableChangeCourier) {
                    shipping = shipping.copy(
                        logisticPromoTickerMessage = null,
                        logisticPromoViewModel = logisticPromo,
                        logisticPromoShipping = null,
                        isApplyLogisticPromo = false
                    )
                } else {
                    shipping = shipping.copy(
                        logisticPromoTickerMessage = null,
                        logisticPromoViewModel = null,
                        logisticPromoShipping = null,
                        isApplyLogisticPromo = false
                    )
                }
                return@withContext ResultRates(
                    shipping, currPromo, null, shippingErrorId, preselectedSpId
                )
            } catch (t: Throwable) {
                return@withContext ResultRates(
                    OrderShipment(
                        isLoading = false,
                        serviceName = orderProfile.shipment.serviceName,
                        serviceDuration = orderProfile.shipment.serviceDuration,
                        serviceErrorMessage = OrderSummaryPageViewModel.FAIL_GET_RATES_ERROR_MESSAGE,
                        shippingRecommendationData = null
                    ), "", null, null, null
                )
            }
        }
        OccIdlingResource.decrement()
        return result
    }

    private fun onRenewShipping(
        shippingDurationUiModels: List<ShippingDurationUiModel>,
        shipping: OrderShipment,
        shippingRecommendationData: ShippingRecommendationData
    ): Pair<OrderShipment, String?> {
        shippingDurationUiModels.forEach {
            it.isSelected =
                it.serviceData.serviceId == shipping.serviceId && !it.serviceData.isUiRatesHidden
        }
        val selectedShippingDurationUiModel = shippingDurationUiModels.firstOrNull { it.isSelected }
        if (selectedShippingDurationUiModel == null) {
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DURATION_UNAVAILABLE)
            return Pair(
                OrderShipment(
                    isLoading = false,
                    serviceName = shipping.serviceName,
                    serviceDuration = shipping.serviceDuration,
                    isHideChangeCourierCard = shipping.isHideChangeCourierCard,
                    serviceErrorMessage = OrderSummaryPageViewModel.NO_DURATION_AVAILABLE,
                    shippingRecommendationData = shippingRecommendationData
                ), null
            )
        }
        val durationError: ErrorServiceData? = selectedShippingDurationUiModel.serviceData.error
        val hasSelectedSpIdFromRates =
            selectedShippingDurationUiModel.serviceData.selectedShipperProductId > 0
        if (durationError?.errorId?.isNotBlank() == true && durationError.errorMessage?.isNotBlank() == true) {
            return Pair(
                OrderShipment(
                    isLoading = false,
                    serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                    serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                    serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                    isHideChangeCourierCard = hasSelectedSpIdFromRates,
                    needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
                    serviceErrorMessage = durationError.errorMessage,
                    shippingRecommendationData = shippingRecommendationData
                ), durationError.errorId
            )
        }
        val shippingCourierViewModelList: List<ShippingCourierUiModel> =
            selectedShippingDurationUiModel.shippingCourierViewModelList
        val selectedSpId = if (hasSelectedSpIdFromRates) {
            // use spId from rates if given
            selectedShippingDurationUiModel.serviceData.selectedShipperProductId
        } else {
            shipping.shipperProductId
        }
        shippingCourierViewModelList.forEach {
            it.isSelected = it.productData.shipperProductId == selectedSpId
        }
        val selectedShippingCourierUiModel =
            shippingCourierViewModelList.firstOrNull { it.isSelected && (hasSelectedSpIdFromRates || !it.productData.isUiRatesHidden) }
                ?: shippingCourierViewModelList.firstOrNull { it.productData.isRecommend && !it.productData.isUiRatesHidden }
                ?: shippingCourierViewModelList.firstOrNull { !it.productData.isUiRatesHidden && (it.productData.error?.errorMessage?.isEmpty() != false) }
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
            shipping.copy(
                isLoading = false,
                shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                shipperId = selectedShippingCourierUiModel.productData.shipperId,
                ratesId = selectedShippingCourierUiModel.ratesId,
                ut = selectedShippingCourierUiModel.productData.unixTime,
                checksum = selectedShippingCourierUiModel.productData.checkSum,
                shipperName = selectedShippingCourierUiModel.productData.shipperName,
                needPinpoint = flagNeedToSetPinpoint,
                serviceErrorMessage = if (flagNeedToSetPinpoint) OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE else errorMessage,
                insurance = OrderInsurance(selectedShippingCourierUiModel.productData.insurance),
                serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                serviceEta = getShippingServiceETA(selectedShippingDurationUiModel.serviceData.texts),
                whitelabelDescription = selectedShippingDurationUiModel.serviceData.texts.textServiceDesc,
                shippingEta = getShippingCourierETA(selectedShippingCourierUiModel.productData.estimatedTimeArrival),
                serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                isApplyLogisticPromo = false,
                logisticPromoViewModel = null,
                logisticPromoShipping = null,
                shippingRecommendationData = shippingRecommendationData,
                isHideChangeCourierCard = hasSelectedSpIdFromRates
            ),
            shippingErrorId
        )
    }

    private fun onRevampNewShipping(
        shippingDurationUiModels: List<ShippingDurationUiModel>,
        profileShipment: OrderProfileShipment,
        shippingRecommendationData: ShippingRecommendationData
    ): Triple<OrderShipment, String?, String?> {
        shippingDurationUiModels.forEach {
            it.isSelected =
                it.serviceData.serviceId == profileShipment.serviceId.toIntOrZero() && !it.serviceData.isUiRatesHidden
        }
        val selectedShippingDurationUiModel: ShippingDurationUiModel =
            shippingDurationUiModels.firstOrNull { it.isSelected }
                ?: return onRevampNewShippingFromRecommendation(
                    shippingDurationUiModels, profileShipment, shippingRecommendationData
                )
        val durationError: ErrorServiceData? = selectedShippingDurationUiModel.serviceData.error
        if (durationError?.errorId?.isNotBlank() == true && durationError.errorMessage?.isNotBlank() == true) {
            return onRevampNewShippingFromRecommendation(
                shippingDurationUiModels, profileShipment, shippingRecommendationData
            )
        }
        val selectedShippingCourierUiModel = getSelectedCourierFromProfileSpId(
            profileShipment.spId.toIntOrZero(),
            selectedShippingDurationUiModel,
            selectedShippingDurationUiModel.shippingCourierViewModelList
        )
            ?: return onRevampNewShippingFromRecommendation(
                shippingDurationUiModels, profileShipment, shippingRecommendationData
            )
        val flagNeedToSetPinpoint = false
        val errorMessage: String? = null
        val shippingErrorId: String? = null
        var preselectedSpId: String? = null
        val courierError: ErrorProductData? = selectedShippingCourierUiModel.productData.error
        if (courierError?.errorMessage?.isNotBlank() == true && courierError.errorId != null) {
            return onRevampNewShippingFromRecommendation(
                shippingDurationUiModels, profileShipment, shippingRecommendationData
            )
        } else if (profileShipment.spId.isBlankOrZero()) {
            preselectedSpId = selectedShippingCourierUiModel.productData.shipperProductId.toString()
        }
        selectedShippingDurationUiModel.shippingCourierViewModelList.forEach {
            it.isSelected =
                it.productData.shipperProductId == selectedShippingCourierUiModel.productData.shipperProductId
        }
        return Triple(
            OrderShipment(
                isLoading = false,
                shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                shipperId = selectedShippingCourierUiModel.productData.shipperId,
                ratesId = selectedShippingCourierUiModel.ratesId,
                ut = selectedShippingCourierUiModel.productData.unixTime,
                checksum = selectedShippingCourierUiModel.productData.checkSum,
                shipperName = selectedShippingCourierUiModel.productData.shipperName,
                needPinpoint = flagNeedToSetPinpoint,
                serviceErrorMessage = if (flagNeedToSetPinpoint) OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE else errorMessage,
                insurance = OrderInsurance(selectedShippingCourierUiModel.productData.insurance),
                serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                serviceEta = getShippingServiceETA(selectedShippingDurationUiModel.serviceData.texts),
                whitelabelDescription = selectedShippingDurationUiModel.serviceData.texts.textServiceDesc,
                shippingEta = getShippingCourierETA(selectedShippingCourierUiModel.productData.estimatedTimeArrival),
                serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                isHideChangeCourierCard = selectedShippingDurationUiModel.serviceData.selectedShipperProductId > 0,
                shippingRecommendationData = shippingRecommendationData
            ), shippingErrorId, preselectedSpId
        )
    }

    private fun onRevampNewShippingFromRecommendation(
        shippingDurationUiModels: List<ShippingDurationUiModel>,
        profileShipment: OrderProfileShipment,
        shippingRecommendationData: ShippingRecommendationData
    ): Triple<OrderShipment, String?, String?> {
        var selectedShippingDurationUiModel: ShippingDurationUiModel? = null
        var selectedShippingCourierUiModel: ShippingCourierUiModel? = null
        for (shippingDurationUiModel in shippingDurationUiModels) {
            val shippingCourierViewModelList = shippingDurationUiModel.shippingCourierViewModelList
            shippingDurationUiModel.isSelected =
                shippingDurationUiModel.serviceData.serviceId == profileShipment.recommendationServiceId.toIntOrZero() && !shippingDurationUiModel.serviceData.isUiRatesHidden
            if (shippingDurationUiModel.isSelected) {
                val recommendationSpId =
                    if (shippingDurationUiModel.serviceData.selectedShipperProductId > 0) {
                        // use spId from rates if given
                        shippingDurationUiModel.serviceData.selectedShipperProductId
                    } else {
                        profileShipment.recommendationSpId.toIntOrZero()
                    }
                for (shippingCourierUiModel in shippingCourierViewModelList) {
                    shippingCourierUiModel.isSelected = false
                    if (shippingCourierUiModel.productData.shipperProductId == recommendationSpId && (shippingDurationUiModel.serviceData.selectedShipperProductId > 0 || !shippingCourierUiModel.productData.isUiRatesHidden)) {
                        shippingCourierUiModel.isSelected = true
                        selectedShippingCourierUiModel = shippingCourierUiModel
                        selectedShippingDurationUiModel = shippingDurationUiModel
                    }
                }
                if (selectedShippingCourierUiModel == null) {
                    // fallback if recommendation is also ui rates hidden
                    val recommendedShippingCourierUiModel =
                        shippingCourierViewModelList.firstOrNull { it.productData.isRecommend && !it.productData.isUiRatesHidden }
                            ?: shippingCourierViewModelList.firstOrNull { !it.productData.isUiRatesHidden && (it.productData.error?.errorMessage?.isEmpty() != false) }
                    if (recommendedShippingCourierUiModel != null) {
                        recommendedShippingCourierUiModel.isSelected = true
                        selectedShippingCourierUiModel = recommendedShippingCourierUiModel
                        selectedShippingDurationUiModel = shippingDurationUiModel
                    }
                }
            }
        }
        if ((selectedShippingDurationUiModel == null || selectedShippingCourierUiModel == null) && profileShipment.autoCourierSelection) {
            selectedShippingDurationUiModel =
                shippingDurationUiModels.firstOrNull { it.serviceData.error?.errorId.isNullOrEmpty() && it.serviceData.error?.errorMessage.isNullOrEmpty() }
            selectedShippingDurationUiModel?.isSelected = true
            selectedShippingCourierUiModel =
                selectedShippingDurationUiModel?.shippingCourierViewModelList?.firstOrNull { it.productData.error?.errorMessage.isNullOrEmpty() }
            selectedShippingCourierUiModel?.isSelected = true
        }
        if (selectedShippingDurationUiModel == null || selectedShippingCourierUiModel == null) {
            // Recommendation Courier not available
            orderSummaryAnalytics.eventViewErrorMessage(OrderSummaryAnalytics.ERROR_ID_LOGISTIC_DURATION_UNAVAILABLE)
            return Triple(
                OrderShipment(
                    isDisabled = profileShipment.isDisableChangeCourier,
                    isLoading = false,
                    serviceName = profileShipment.serviceName,
                    serviceDuration = profileShipment.serviceDuration,
                    serviceErrorMessage = OrderSummaryPageViewModel.NO_DURATION_AVAILABLE,
                    shippingRecommendationData = shippingRecommendationData
                ), null, null
            )
        }
        val durationError: ErrorServiceData? = selectedShippingDurationUiModel.serviceData.error
        if (durationError?.errorId?.isNotBlank() == true && durationError.errorMessage?.isNotBlank() == true) {
            return Triple(
                OrderShipment(
                    isLoading = false,
                    serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                    serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                    isHideChangeCourierCard = selectedShippingDurationUiModel.serviceData.selectedShipperProductId > 0,
                    serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                    needPinpoint = durationError.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED,
                    serviceErrorMessage = durationError.errorMessage,
                    shippingRecommendationData = shippingRecommendationData
                ), durationError.errorId, null
            )
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
            OrderShipment(
                isLoading = false,
                shipperProductId = selectedShippingCourierUiModel.productData.shipperProductId,
                shipperId = selectedShippingCourierUiModel.productData.shipperId,
                ratesId = selectedShippingCourierUiModel.ratesId,
                ut = selectedShippingCourierUiModel.productData.unixTime,
                checksum = selectedShippingCourierUiModel.productData.checkSum,
                shipperName = selectedShippingCourierUiModel.productData.shipperName,
                needPinpoint = flagNeedToSetPinpoint,
                serviceErrorMessage = if (flagNeedToSetPinpoint) OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE else errorMessage,
                insurance = OrderInsurance(selectedShippingCourierUiModel.productData.insurance),
                serviceId = selectedShippingDurationUiModel.serviceData.serviceId,
                serviceDuration = selectedShippingDurationUiModel.serviceData.serviceName,
                isHideChangeCourierCard = selectedShippingDurationUiModel.serviceData.selectedShipperProductId > 0,
                serviceEta = getShippingServiceETA(selectedShippingDurationUiModel.serviceData.texts),
                whitelabelDescription = selectedShippingDurationUiModel.serviceData.texts.textServiceDesc,
                shippingEta = getShippingCourierETA(selectedShippingCourierUiModel.productData.estimatedTimeArrival),
                serviceName = selectedShippingDurationUiModel.serviceData.serviceName,
                shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                shippingRecommendationData = shippingRecommendationData
            ), shippingErrorId, preselectedSpId
        )
    }

    private fun getSelectedCourierFromProfileSpId(
        spId: Int,
        shippingDurationUiModel: ShippingDurationUiModel,
        shippingCourierViewModelList: List<ShippingCourierUiModel>
    ): ShippingCourierUiModel? {
        var selectedCourier: ShippingCourierUiModel? = null
        if (shippingDurationUiModel.serviceData.selectedShipperProductId > 0) {
            // use spId from rates if given
            selectedCourier =
                shippingCourierViewModelList.firstOrNull { it.productData.shipperProductId == shippingDurationUiModel.serviceData.selectedShipperProductId }
        } else if (spId > 0) {
            selectedCourier =
                shippingCourierViewModelList.firstOrNull { it.productData.shipperProductId == spId && !it.productData.isUiRatesHidden }
        }
        if (selectedCourier == null) {
            // fallback if spId from cart is ui-hidden
            selectedCourier =
                shippingCourierViewModelList.firstOrNull { it.productData.isRecommend && !it.productData.isUiRatesHidden }
                    ?: shippingCourierViewModelList.firstOrNull { !it.productData.isUiRatesHidden }
        }
        return selectedCourier
    }

    private fun getShippingCourierETA(eta: EstimatedTimeArrival?): String? {
        return if (eta != null && eta.errorCode == 0) eta.textEta else null
    }

    private fun getShippingServiceETA(eta: ServiceTextData?): String? {
        return if (eta != null && eta.errorCode == 0) eta.textEtaSummarize else null
    }

    fun generateOrderErrorResultRates(orderProfile: OrderProfile): ResultRates {
        return ResultRates(
            orderShipment = OrderShipment(
                isLoading = false,
                isDisabled = true,
                serviceName = orderProfile.shipment.serviceName,
                serviceDuration = orderProfile.shipment.serviceDuration,
                serviceErrorMessage = OrderSummaryPageViewModel.FAIL_GET_RATES_ERROR_MESSAGE,
                shippingRecommendationData = null
            )
        )
    }

    fun generateNeedPinpointResultRates(orderProfile: OrderProfile): ResultRates {
        return ResultRates(
            orderShipment = OrderShipment(
                isLoading = false,
                isDisabled = false,
                serviceName = orderProfile.shipment.serviceName,
                serviceDuration = orderProfile.shipment.serviceDuration,
                serviceErrorMessage = OrderSummaryPageViewModel.FAIL_GET_RATES_ERROR_MESSAGE,
                needPinpoint = true,
                shippingRecommendationData = null
            )
        )
    }

    suspend fun savePinpoint(
        address: OrderProfileAddress,
        longitude: String,
        latitude: String,
        userId: String,
        deviceId: String
    ): OccGlobalEvent {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val params = AuthHelper.generateParamsNetwork(userId, deviceId, TKPDMapParam())
                params[EditAddressParam.ADDRESS_ID] = address.addressId
                params[EditAddressParam.ADDRESS_NAME] = address.addressName
                params[EditAddressParam.ADDRESS_STREET] = address.addressStreet
                params[EditAddressParam.POSTAL_CODE] = address.postalCode
                params[EditAddressParam.DISTRICT_ID] = address.districtId
                params[EditAddressParam.CITY_ID] = address.cityId
                params[EditAddressParam.PROVINCE_ID] = address.provinceId
                params[EditAddressParam.LATITUDE] = latitude
                params[EditAddressParam.LONGITUDE] = longitude
                params[EditAddressParam.RECEIVER_NAME] = address.receiverName
                params[EditAddressParam.RECEIVER_PHONE] = address.phone

                val stringResponse =
                    editAddressUseCase.get().createObservable(RequestParams.create().apply {
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
                    return@withContext OccGlobalEvent.TriggerRefresh(successMessage = SAVE_PINPOINT_SUCCESS_MESSAGE)
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

    fun chooseCourier(
        chosenShippingCourierViewModel: ShippingCourierUiModel, shipping: OrderShipment
    ): OrderShipment? {
        val shippingRecommendationData = shipping.shippingRecommendationData
        if (shippingRecommendationData != null) {
            val shippingDurationViewModels = shippingRecommendationData.shippingDurationUiModels
            val logisticPromoList = shippingRecommendationData.listLogisticPromo
            shippingRecommendationData.listLogisticPromo =
                logisticPromoList.map { it.copy(isApplied = false) }
            for (shippingDurationViewModel in shippingDurationViewModels) {
                if (shippingDurationViewModel.serviceData.serviceId == shipping.serviceId) {
                    shippingDurationViewModel.isSelected = true
                    val shippingCourierViewModelList =
                        shippingDurationViewModel.shippingCourierViewModelList
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
                            insurance = OrderInsurance(selectedShippingCourierUiModel.productData.insurance),
                            shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                            shippingEta = getShippingCourierETA(selectedShippingCourierUiModel.productData.estimatedTimeArrival),
                            shippingRecommendationData = shippingRecommendationData,
                            logisticPromoShipping = null,
                            isApplyLogisticPromo = false
                        )
                    }
                }
            }
        }
        return null
    }

    fun chooseDuration(
        selectedServiceId: Int,
        selectedShippingCourierUiModel: ShippingCourierUiModel,
        flagNeedToSetPinpoint: Boolean,
        shipping: OrderShipment
    ): OrderShipment? {
        val shippingRecommendationData = shipping.shippingRecommendationData
        if (shippingRecommendationData != null) {
            val shippingDurationViewModels = shippingRecommendationData.shippingDurationUiModels
            var selectedShippingDurationViewModel = shippingDurationViewModels[0]
            for (shippingDurationViewModel in shippingDurationViewModels) {
                if (shippingDurationViewModel.serviceData.serviceId == selectedServiceId) {
                    shippingDurationViewModel.isSelected = true
                    selectedShippingDurationViewModel = shippingDurationViewModel
                } else {
                    shippingDurationViewModel.isSelected = false
                }
            }
            val logisticPromoList = shippingRecommendationData.listLogisticPromo
            shippingRecommendationData.listLogisticPromo =
                logisticPromoList.map { it.copy(isApplied = false) }
            var newShipping = shipping.copy(
                isLoading = false,
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
                insurance = OrderInsurance(selectedShippingCourierUiModel.productData.insurance),
                shippingPrice = selectedShippingCourierUiModel.productData.price.price,
                serviceEta = getShippingServiceETA(selectedShippingDurationViewModel.serviceData.texts),
                whitelabelDescription = selectedShippingDurationViewModel.serviceData.texts.textServiceDesc,
                shippingEta = getShippingCourierETA(selectedShippingCourierUiModel.productData.estimatedTimeArrival),
                shippingRecommendationData = shippingRecommendationData,
                logisticPromoTickerMessage = null,
                logisticPromoViewModel = null,
                logisticPromoShipping = null,
                isApplyLogisticPromo = false,
                isHideChangeCourierCard = selectedShippingDurationViewModel.serviceData.selectedShipperProductId > 0
            )

            if (newShipping.serviceErrorMessage.isNullOrEmpty()) {
                val logisticPromo: LogisticPromoUiModel? = shippingRecommendationData.logisticPromo
                if (logisticPromo != null && !logisticPromo.disabled) {
                    newShipping = newShipping.copy(
                        logisticPromoTickerMessage = constructBboTickerTitle(logisticPromo),
                        logisticPromoViewModel = logisticPromo,
                        logisticPromoShipping = null
                    )
                }
            }
            return newShipping
        }
        return null
    }

    private fun constructBboTickerTitle(logisticPromoUiModel: LogisticPromoUiModel): String {
        if (logisticPromoUiModel.tickerDescriptionPromoAdjusted.isNotEmpty()) {
            return "${logisticPromoUiModel.tickerAvailableFreeShippingCourierTitle}<br />${logisticPromoUiModel.tickerDescriptionPromoAdjusted}"
        }
        return logisticPromoUiModel.tickerAvailableFreeShippingCourierTitle
    }

    fun onApplyBbo(
        shipping: OrderShipment,
        logisticPromoUiModel: LogisticPromoUiModel,
        newGlobalEvent: OccGlobalEvent
    ): Pair<OrderShipment?, OccGlobalEvent> {
        val shippingRecommendationData = shipping.shippingRecommendationData
        if (shippingRecommendationData != null) {
            var logisticPromoShipping: ShippingCourierUiModel? = null
            for (shippingDurationViewModel in shippingRecommendationData.shippingDurationUiModels) {
                if (shippingDurationViewModel.isSelected) {
                    for (shippingCourierUiModel in shippingDurationViewModel.shippingCourierViewModelList) {
                        shippingCourierUiModel.isSelected = false
                    }
                }
                if (shippingDurationViewModel.serviceData.serviceId == logisticPromoUiModel.serviceId) {
                    logisticPromoShipping =
                        shippingDurationViewModel.shippingCourierViewModelList.firstOrNull { it.productData.shipperProductId == logisticPromoUiModel.shipperProductId }
                }
                shippingDurationViewModel.isSelected = false
            }
            if (logisticPromoShipping != null) {
                val logisticPromoList = shippingRecommendationData.listLogisticPromo
                shippingRecommendationData.listLogisticPromo =
                    logisticPromoList.map { it.copy(isApplied = logisticPromoUiModel.promoCode == it.promoCode) }
                val needPinpoint =
                    logisticPromoShipping.productData.error?.errorId == ErrorProductData.ERROR_PINPOINT_NEEDED
                return Pair(
                    shipping.copy(
                        isLoading = false,
                        logisticPromoViewModel = logisticPromoUiModel,
                        shippingRecommendationData = shippingRecommendationData,
                        isServicePickerEnable = true,
                        insurance = OrderInsurance(logisticPromoShipping.productData.insurance),
                        serviceErrorMessage = if (needPinpoint) OrderSummaryPageViewModel.NEED_PINPOINT_ERROR_MESSAGE else logisticPromoShipping.productData.error?.errorMessage,
                        needPinpoint = needPinpoint,
                        logisticPromoTickerMessage = null,
                        isApplyLogisticPromo = true,
                        logisticPromoShipping = logisticPromoShipping
                    ), newGlobalEvent
                )
            }
        }
        return Pair(
            null,
            OccGlobalEvent.Error(errorMessage = OrderSummaryPageViewModel.FAIL_APPLY_BBO_ERROR_MESSAGE)
        )
    }

    internal fun resetBbo(orderShipment: OrderShipment): OrderShipment {
        val logisticPromoViewModel = orderShipment.logisticPromoViewModel
        val logisticPromoShipping = orderShipment.logisticPromoShipping
        val shippingRecommendationData = orderShipment.shippingRecommendationData
        if (shippingRecommendationData != null && logisticPromoViewModel != null && orderShipment.isApplyLogisticPromo && logisticPromoShipping != null) {
            val logisticPromoList = shippingRecommendationData.listLogisticPromo
            shippingRecommendationData.listLogisticPromo =
                logisticPromoList.map { it.copy(isApplied = false) }
            val shippingDuration =
                shippingRecommendationData.shippingDurationUiModels.first { it.serviceData.serviceId == logisticPromoShipping.serviceData.serviceId }
            shippingDuration.isSelected = true
            shippingDuration.shippingCourierViewModelList.first { it.productData.shipperProductId == logisticPromoShipping.productData.shipperProductId }.isSelected =
                true
            return orderShipment.copy(
                isLoading = false,
                shippingRecommendationData = shippingRecommendationData,
                isApplyLogisticPromo = false,
                logisticPromoShipping = null,
                logisticPromoTickerMessage = constructBboTickerTitle(logisticPromoViewModel)
            )
        }
        return orderShipment
    }

    suspend fun setChosenAddress(address: RecipientAddressModel): ChosenAddressModel? {
        OccIdlingResource.increment()
        val result = withContext(executorDispatchers.io) {
            try {
                val stateChosenAddressFromAddress =
                    chooseAddressRepository.get().setStateChosenAddressFromAddress(address)
                chooseAddressMapper.get()
                    .mapSetStateChosenAddress(stateChosenAddressFromAddress.response)
            } catch (t: Throwable) {
                null
            }
        }
        OccIdlingResource.decrement()
        return result
    }
}

class ResultRates(
    val orderShipment: OrderShipment,
    val clearOldPromoCode: String = "",
    val autoApplyPromo: LogisticPromoUiModel? = null,
    val shippingErrorId: String? = null,
    val preselectedSpId: String? = null,
    val overweight: Double? = null
)
