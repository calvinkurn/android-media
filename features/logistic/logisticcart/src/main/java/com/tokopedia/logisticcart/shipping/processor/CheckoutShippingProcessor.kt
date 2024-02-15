package com.tokopedia.logisticcart.shipping.processor

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.request.EditPinpointParam
import com.tokopedia.logisticCommon.data.request.UpdatePinpointParam
import com.tokopedia.logisticCommon.domain.usecase.UpdatePinpointUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.entity.request.ScheduleDeliveryParam
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetRatesWithScheduleDeliveryCoroutineUseCase
import com.tokopedia.logisticcart.scheduledelivery.domain.usecase.GetScheduleDeliveryCoroutineUseCase
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.RatesResponseStateConverter
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.usecase.GetRatesApiCoroutineUseCase
import com.tokopedia.logisticcart.shipping.usecase.GetRatesCoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class CheckoutShippingProcessor @Inject constructor(
    private val updatePinpointUseCase: UpdatePinpointUseCase,
    private val ratesUseCase: GetRatesCoroutineUseCase,
    private val ratesApiUseCase: GetRatesApiCoroutineUseCase,
    private val ratesWithScheduleUseCase: GetRatesWithScheduleDeliveryCoroutineUseCase,
    private val ratesResponseStateConverter: RatesResponseStateConverter,
    private val shippingCourierConverter: ShippingCourierConverter,
    private val scheduleDeliveryUseCase: GetScheduleDeliveryCoroutineUseCase,
    private val dispatchers: CoroutineDispatchers
) {

    var isBoUnstackEnabled = false

    suspend fun editAddressPinpoint(
        latitude: String,
        longitude: String,
        recipientAddressModel: RecipientAddressModel
    ): EditAddressResult {
        return withContext(dispatchers.io) {
            return@withContext try {
                val requestParams =
                    generateUpdatePinpointParam(latitude, longitude, recipientAddressModel)
                val response = updatePinpointUseCase(requestParams)
                val statusSuccess = response.keroEditAddress.data.isSuccess == 1
                if (statusSuccess) {
                    recipientAddressModel.latitude = latitude
                    recipientAddressModel.longitude = longitude
                    EditAddressResult(isSuccess = true)
                } else {
                    EditAddressResult(
                        isSuccess = false,
                        errorMessage = "Terjadi kesalahan. Ulangi beberapa saat lagi"
                    )
                }
            } catch (t: Throwable) {
                Timber.d(t)
                EditAddressResult(isSuccess = false, throwable = t)
            }
        }
    }

    private fun generateUpdatePinpointParam(
        addressLatitude: String,
        addressLongitude: String,
        recipientAddressModel: RecipientAddressModel
    ): UpdatePinpointParam {
        val params = EditPinpointParam(
            addressId = recipientAddressModel.id.toLongOrZero(),
            addressName = recipientAddressModel.addressName,
            address1 = recipientAddressModel.street,
            postalCode = recipientAddressModel.postalCode,
            district = recipientAddressModel.destinationDistrictId,
            city = recipientAddressModel.cityId,
            province = recipientAddressModel.provinceId,
            address2 = "$addressLatitude, $addressLongitude",
            receiverName = recipientAddressModel.recipientName,
            phone = recipientAddressModel.recipientPhoneNumber
        )

        return UpdatePinpointParam(input = params)
    }

    suspend fun getRatesGeneral(
        ratesProcessorParam: LogisticProcessorGetRatesParam
    ): RatesResult? {
        return if (ratesProcessorParam.isTradeInDropOff) {
            getRatesTradeInDropOff(
                ratesProcessorParam
            )
        } else {
            getRates(
                ratesProcessorParam
            )
        }
    }

    suspend fun getRatesWithScheduleDelivery(
        ratesProcessorParam: LogisticProcessorGetRatesParam,
        schellyProcessorParam: LogisticProcessorGetSchellyParam
    ): RatesResult? {
        return withContext(dispatchers.io) {
            try {
                var shippingRecommendationData =
                    ratesWithScheduleUseCase(ratesProcessorParam.ratesParam to schellyProcessorParam.schellyParam)
                shippingRecommendationData = ratesResponseStateConverter.fillState(
                    shippingRecommendationData,
                    ratesProcessorParam.ratesParam.shopShipments,
                    ratesProcessorParam.selectedSpId,
                    0
                )
                var errorReason = "rates invalid data"
                if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty() && shippingRecommendationData.scheduleDeliveryData != null) {
                    if (isBoUnstackEnabled && ratesProcessorParam.boPromoCode.isNotEmpty()) {
                        val logisticPromo =
                            shippingRecommendationData.listLogisticPromo.firstOrNull { it.promoCode == ratesProcessorParam.boPromoCode && !it.disabled }
                        if (logisticPromo != null) {
                            for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                                if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                    for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                        shippingCourierUiModel.isSelected = false
                                    }
                                    for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                        if (shippingCourierUiModel.productData.shipperProductId == logisticPromo.shipperProductId && shippingCourierUiModel.productData.shipperId == logisticPromo.shipperId) {
                                            if (shippingCourierUiModel.productData.error.errorMessage.isNotEmpty()) {
                                                throw MessageErrorException(
                                                    shippingCourierUiModel.productData.error.errorMessage
                                                )
                                            } else {
                                                val courierItemData =
                                                    generateCourierItemDataWithScheduleDelivery(
                                                        false,
                                                        ratesProcessorParam.selectedServiceId,
                                                        ratesProcessorParam.selectedSpId,
                                                        shippingCourierUiModel,
                                                        shippingRecommendationData,
                                                        logisticPromo,
                                                        schellyProcessorParam.validationMetadata,
                                                        ratesProcessorParam.isDisableChangeCourier
                                                    )
                                                return@withContext RatesResult(
                                                    courierItemData,
                                                    shippingDurationUiModel.shippingCourierViewModelList
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            errorReason = "promo not matched"
                        }
                    } else {
                        for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                            if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    shippingCourierUiModel.isSelected = false
                                }
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    if (shippingCourierUiModel.productData.shipperProductId == ratesProcessorParam.selectedSpId && !shippingCourierUiModel.serviceData.isUiRatesHidden) {
                                        if (shippingCourierUiModel.productData.error.errorMessage.isNotEmpty()) {
                                            throw MessageErrorException(
                                                shippingCourierUiModel.productData.error.errorMessage
                                            )
                                        } else {
                                            val courierItemData =
                                                generateCourierItemDataWithScheduleDelivery(
                                                    false,
                                                    ratesProcessorParam.selectedServiceId,
                                                    ratesProcessorParam.selectedSpId,
                                                    shippingCourierUiModel,
                                                    shippingRecommendationData,
                                                    null,
                                                    schellyProcessorParam.validationMetadata,
                                                    ratesProcessorParam.isDisableChangeCourier
                                                )
                                            if (shippingCourierUiModel.productData.isUiRatesHidden && shippingCourierUiModel.serviceData.selectedShipperProductId == 0 && courierItemData.selectedShipper.logPromoCode.isNullOrEmpty()) {
                                                // courier should only be used with BO, but no BO code found
                                                throw MessageErrorException("rates ui hidden but no promo")
                                            }
                                            val shouldValidatePromo =
                                                courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                                            if (!shouldValidatePromo) {
                                                shippingCourierUiModel.isSelected = true
                                            }
                                            return@withContext RatesResult(
                                                courierItemData,
                                                shippingDurationUiModel.shippingCourierViewModelList
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // corner case auto selection if BE default duration failed
                        if (ratesProcessorParam.isAutoCourierSelection || ratesProcessorParam.isDisableChangeCourier) {
                            val shippingDuration =
                                shippingRecommendationData.shippingDurationUiModels.firstOrNull { it.serviceData.error.errorId.isEmpty() && it.serviceData.error.errorMessage.isEmpty() }
                            if (shippingDuration != null) {
                                val shippingCourier =
                                    shippingDuration.shippingCourierViewModelList.firstOrNull {
                                        it.productData.error.errorMessage.isEmpty()
                                    }
                                if (shippingCourier != null) {
                                    val courierItemData =
                                        generateCourierItemDataWithScheduleDelivery(
                                            false,
                                            ratesProcessorParam.selectedServiceId,
                                            ratesProcessorParam.selectedSpId,
                                            shippingCourier,
                                            shippingRecommendationData,
                                            null,
                                            schellyProcessorParam.validationMetadata,
                                            ratesProcessorParam.isDisableChangeCourier
                                        )
                                    val shouldValidatePromo =
                                        courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                                    if (!shouldValidatePromo) {
                                        shippingCourier.isSelected = true
                                    }
                                    return@withContext RatesResult(
                                        courierItemData,
                                        shippingDuration.shippingCourierViewModelList
                                    )
                                }
                            }
                        }
                    }
                } else {
                    errorReason = "rates empty data"
                }
                throw MessageErrorException(errorReason)
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext RatesResult(
                    null,
                    emptyList(),
                    ratesError = t
                )
            }
        }
    }

    suspend fun getScheduleDelivery(
        schellyProcessorParam: LogisticProcessorGetSchellyParam
    ): RatesResult? {
        return withContext(dispatchers.io) {
            try {
                val schellyResponse = scheduleDeliveryUseCase(schellyProcessorParam.schellyParam)
                val schellyData =
                    schellyResponse.ongkirGetScheduledDeliveryRates.scheduleDeliveryData
                val courierItemData =
                    shippingCourierConverter.schellyToCourierItemData(
                        schellyData,
                        schellyProcessorParam.validationMetadata
                    )
                val schellyHasSchedule =
                    courierItemData.scheduleDeliveryUiModel?.isSelected == true && courierItemData.scheduleDeliveryUiModel?.deliveryServices?.isNotEmpty() == true
                val schellyUnavailable = courierItemData.scheduleDeliveryUiModel?.available == false
                if (schellyHasSchedule || schellyUnavailable) {
                    return@withContext RatesResult(
                        courierItemData,
                        emptyList()
                    )
                } else {
                    val errorReason = "schelly is not selected"
                    throw MessageErrorException(errorReason)
                }
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext RatesResult(
                    null,
                    emptyList(),
                    ratesError = t
                )
            }
        }
    }

    suspend fun getRatesWithBoCode(
        ratesProcessorParam: LogisticProcessorGetRatesParam
    ): RatesResult? {
        return withContext(dispatchers.io) {
            try {
                var shippingRecommendationData = if (ratesProcessorParam.isTradeInDropOff) {
                    ratesApiUseCase(ratesProcessorParam.ratesParam)
                } else {
                    ratesUseCase(ratesProcessorParam.ratesParam)
                }
                shippingRecommendationData = ratesResponseStateConverter.fillState(
                    shippingRecommendationData,
                    ratesProcessorParam.ratesParam.shopShipments,
                    ratesProcessorParam.selectedSpId,
                    ratesProcessorParam.selectedServiceId
                )
                var errorReason = "rates invalid data"
                if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty() && shippingRecommendationData.listLogisticPromo.isNotEmpty()) {
                    val logisticPromo =
                        shippingRecommendationData.listLogisticPromo.firstOrNull { it.promoCode == ratesProcessorParam.boPromoCode && !it.disabled }
                    if (logisticPromo != null) {
                        for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                            if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    shippingCourierUiModel.isSelected = false
                                }
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    if (ratesProcessorParam.isTradeInDropOff || shippingCourierUiModel.productData.shipperProductId == ratesProcessorParam.selectedSpId && shippingCourierUiModel.productData.shipperId == ratesProcessorParam.selectedServiceId) {
                                        if (shippingCourierUiModel.productData.error.errorMessage.isNotEmpty()) {
                                            throw MessageErrorException(
                                                shippingCourierUiModel.productData.error.errorMessage
                                            )
                                        } else {
                                            shippingCourierUiModel.isSelected = true
                                            val courierItemData =
                                                generateCourierItemDataWithLogisticPromo(
                                                    shippingCourierUiModel,
                                                    shippingRecommendationData,
                                                    logisticPromo
                                                )
                                            return@withContext RatesResult(
                                                courierItemData,
                                                shippingDurationUiModel.shippingCourierViewModelList
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        errorReason = "promo not found"
                    }
                } else {
                    errorReason = "rates empty data"
                }
                throw MessageErrorException(errorReason)
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext RatesResult(
                    null,
                    emptyList(),
                    ratesError = t
                )
            }
        }
    }

    private suspend fun getRates(
        param: LogisticProcessorGetRatesParam
    ): RatesResult? {
        return withContext(dispatchers.io) {
            try {
                var shippingRecommendationData = ratesUseCase(param.ratesParam)
                shippingRecommendationData = ratesResponseStateConverter.fillState(
                    shippingRecommendationData,
                    param.ratesParam.shopShipments,
                    param.selectedSpId,
                    param.selectedServiceId
                )
                var errorReason = "rates invalid data"
                if (param.shouldResetCourier) {
                    // todo
                    error("racing condition against epharmacy validation")
                }
                if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                    if (isBoUnstackEnabled && param.boPromoCode.isNotEmpty()) {
                        val logisticPromo =
                            shippingRecommendationData.listLogisticPromo.firstOrNull { it.promoCode == param.boPromoCode && !it.disabled }
                        if (logisticPromo != null) {
                            for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                                if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                    for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                        shippingCourierUiModel.isSelected = false
                                    }
                                    for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                        if (shippingCourierUiModel.productData.shipperProductId == logisticPromo.shipperProductId && shippingCourierUiModel.productData.shipperId == logisticPromo.shipperId) {
                                            if (shippingCourierUiModel.productData.error.errorMessage.isNotEmpty()) {
                                                throw MessageErrorException(
                                                    shippingCourierUiModel.productData.error.errorMessage
                                                )
                                            } else {
                                                val courierItemData =
                                                    generateCourierItemData(
                                                        false,
                                                        param.selectedSpId,
                                                        shippingCourierUiModel,
                                                        shippingRecommendationData,
                                                        logisticPromo,
                                                        param.validationMetadata,
                                                        param.isDisableChangeCourier
                                                    )
                                                return@withContext RatesResult(
                                                    courierItemData,
                                                    shippingDurationUiModel.shippingCourierViewModelList
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            errorReason = "promo not matched"
                        }
                    } else {
                        for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                            if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    shippingCourierUiModel.isSelected = false
                                }
                                val newSelectedSpId = getSelectedSpId(
                                    param.selectedSpId,
                                    shippingDurationUiModel,
                                    param.currentServiceId
                                )
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    if (shippingCourierUiModel.productData.shipperProductId == newSelectedSpId && !shippingCourierUiModel.serviceData.isUiRatesHidden) {
                                        if (shippingCourierUiModel.productData.error.errorMessage.isNotEmpty()) {
                                            throw MessageErrorException(
                                                shippingCourierUiModel.productData.error.errorMessage
                                            )
                                        } else {
                                            val courierItemData = generateCourierItemData(
                                                false,
                                                newSelectedSpId,
                                                shippingCourierUiModel,
                                                shippingRecommendationData,
                                                null,
                                                param.validationMetadata,
                                                param.isDisableChangeCourier
                                            )
                                            if (shippingCourierUiModel.productData.isUiRatesHidden && shippingCourierUiModel.serviceData.selectedShipperProductId == 0 && courierItemData.selectedShipper.logPromoCode.isNullOrEmpty()) {
                                                // courier should only be used with BO, but no BO code found
                                                throw MessageErrorException("rates ui hidden but no promo")
                                            }
                                            val shouldValidatePromo =
                                                courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                                            if (!shouldValidatePromo) {
                                                shippingCourierUiModel.isSelected = true
                                            }
                                            return@withContext RatesResult(
                                                courierItemData,
                                                shippingDurationUiModel.shippingCourierViewModelList
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // corner case auto selection if BE default duration failed
                        if (param.isAutoCourierSelection) {
                            val shippingDuration =
                                shippingRecommendationData.shippingDurationUiModels.firstOrNull { it.serviceData.error.errorId.isEmpty() && it.serviceData.error.errorMessage.isEmpty() }
                            if (shippingDuration != null) {
                                val shippingCourier =
                                    shippingDuration.shippingCourierViewModelList.firstOrNull {
                                        it.productData.error.errorMessage.isEmpty()
                                    }
                                if (shippingCourier != null) {
                                    val courierItemData = generateCourierItemData(
                                        false,
                                        param.selectedSpId,
                                        shippingCourier,
                                        shippingRecommendationData,
                                        null,
                                        param.validationMetadata,
                                        param.isDisableChangeCourier
                                    )
                                    val shouldValidatePromo =
                                        courierItemData.selectedShipper.logPromoCode != null && courierItemData.selectedShipper.logPromoCode!!.isNotEmpty()
                                    if (!shouldValidatePromo) {
                                        shippingCourier.isSelected = true
                                    }
                                    return@withContext RatesResult(
                                        courierItemData,
                                        shippingDuration.shippingCourierViewModelList
                                    )
                                }
                            }
                        }
                    }
                } else {
                    errorReason = "rates empty data"
                }
                throw MessageErrorException(errorReason)
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext RatesResult(
                    null,
                    emptyList(),
                    ratesError = t
                )
            }
        }
    }

    private suspend fun getRatesTradeInDropOff(
        ratesProcessorParam: LogisticProcessorGetRatesParam
    ): RatesResult? {
        return withContext(dispatchers.io) {
            try {
                var shippingRecommendationData = ratesApiUseCase(ratesProcessorParam.ratesParam)
                shippingRecommendationData = ratesResponseStateConverter.fillState(
                    shippingRecommendationData,
                    ratesProcessorParam.ratesParam.shopShipments,
                    ratesProcessorParam.selectedSpId,
                    ratesProcessorParam.selectedServiceId
                )
                var errorReason = "rates invalid data"
                if (shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                    for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                        if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                            for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                shippingCourierUiModel.isSelected = false
                            }
                            val shippingCourierUiModel =
                                shippingDurationUiModel.shippingCourierViewModelList.first()
                            if (shippingCourierUiModel.productData.error.errorMessage.isNotEmpty()) {
                                throw MessageErrorException(
                                    shippingCourierUiModel.productData.error.errorMessage
                                )
                            } else {
                                val courierItemData = generateCourierItemData(
                                    false,
                                    ratesProcessorParam.selectedSpId,
                                    shippingCourierUiModel,
                                    shippingRecommendationData,
                                    null,
                                    ratesProcessorParam.validationMetadata,
                                    ratesProcessorParam.isDisableChangeCourier
                                )
                                if (shippingCourierUiModel.productData.isUiRatesHidden && shippingCourierUiModel.serviceData.selectedShipperProductId == 0 && courierItemData.logPromoCode.isNullOrEmpty()) {
                                    // courier should only be used with BO, but no BO code found
                                    throw MessageErrorException("rates ui hidden but no promo")
                                }
                                shippingCourierUiModel.isSelected = true
                                return@withContext RatesResult(
                                    courierItemData,
                                    shippingDurationUiModel.shippingCourierViewModelList
                                )
                            }
                        }
                    }
                } else {
                    errorReason = "rates empty data"
                }
                throw MessageErrorException(
                    errorReason
                )
            } catch (t: Throwable) {
                Timber.d(t)
                return@withContext RatesResult(
                    null,
                    emptyList(),
                    t
                )
            }
        }
    }

    private fun generateCourierItemData(
        isForceReloadRates: Boolean,
        spId: Int,
        shippingCourierUiModel: ShippingCourierUiModel,
        shippingRecommendationData: ShippingRecommendationData,
        logisticPromo: LogisticPromoUiModel? = null,
        validationMetadata: String,
        isDisableChangeCourier: Boolean
    ): CourierItemData {
        var courierItemData =
            shippingCourierConverter.convertToCourierItemDataNew(
                shippingCourierUiModel,
                shippingRecommendationData,
                validationMetadata
            )

        // Auto apply Promo Stacking Logistic
        var logisticPromoChosen = logisticPromo
        if (isDisableChangeCourier) {
            // set error log
            shippingRecommendationData.listLogisticPromo.firstOrNull()?.let {
                courierItemData.logPromoMsg = it.disableText
                courierItemData.logPromoDesc = it.description
            }
            // must get promo for tokonow
            logisticPromoChosen = shippingRecommendationData.listLogisticPromo.firstOrNull {
                it.promoCode.isNotEmpty() && !it.disabled
            }
        } else if (isForceReloadRates) {
            logisticPromoChosen = shippingRecommendationData.listLogisticPromo.firstOrNull {
                !it.disabled && it.isApplied
            }
        } else if (!isBoUnstackEnabled) {
            logisticPromoChosen = shippingRecommendationData.listLogisticPromo.firstOrNull {
                !it.disabled && /*it.shipperId == shipperId &&*/ it.shipperProductId == spId && it.promoCode.isNotEmpty()
            }
        }
        if (logisticPromoChosen?.shipperProductId != null && logisticPromoChosen.shipperProductId != courierItemData.shipperProductId) {
            val courierUiModel = shippingRecommendationData.shippingDurationUiModels.first {
                it.serviceData.serviceId == logisticPromoChosen.serviceId
            }.shippingCourierViewModelList.first {
                it.productData.shipperProductId == logisticPromoChosen.shipperProductId
            }
            courierItemData = shippingCourierConverter.convertToCourierItemDataNew(
                courierUiModel,
                shippingRecommendationData,
                validationMetadata
            )
        }
        logisticPromoChosen?.let {
            courierItemData.logPromoCode = it.promoCode
            courierItemData.discountedRate = it.discountedRate
            courierItemData.shippingRate = it.shippingRate
            courierItemData.benefitAmount = it.benefitAmount
            courierItemData.promoTitle = it.title
            courierItemData.isHideShipperName = it.hideShipperName
            courierItemData.shipperName = it.shipperName
            courierItemData.etaText = it.etaData.textEta
            courierItemData.etaErrorCode = it.etaData.errorCode
            courierItemData.freeShippingChosenCourierTitle = it.freeShippingChosenCourierTitle
            courierItemData.freeShippingChosenImage = it.imageChosenFreeShipping
            courierItemData.freeShippingMetadata = it.freeShippingMetadata
            courierItemData.benefitClass = it.benefitClass
            courierItemData.shippingSubsidy = it.shippingSubsidy
            courierItemData.boCampaignId = it.boCampaignId
            courierItemData.boOrderMessage = it.orderMessage
        }
        return courierItemData
    }

    private fun getSelectedSpId(
        spId: Int,
        shippingDurationUiModel: ShippingDurationUiModel,
        currentServiceId: Int?
    ): Int {
        return if (currentServiceId != null &&
            currentServiceId > 0 &&
            shippingDurationUiModel.serviceData.serviceId == currentServiceId &&
            shippingDurationUiModel.serviceData.selectedShipperProductId > 0
        ) {
            shippingDurationUiModel.serviceData.selectedShipperProductId
        } else {
            return spId
        }
    }

    private fun generateCourierItemDataWithScheduleDelivery(
        isForceReloadRates: Boolean,
        shipperId: Int,
        spId: Int,
        shippingCourierUiModel: ShippingCourierUiModel,
        shippingRecommendationData: ShippingRecommendationData,
        logisticPromo: LogisticPromoUiModel? = null,
        validationMetadata: String,
        isDisableChangeCourier: Boolean
    ): CourierItemData {
        var courierItemData =
            shippingCourierConverter.convertToCourierItemDataNew(
                shippingCourierUiModel,
                shippingRecommendationData,
                validationMetadata
            )

        // Auto apply Promo Stacking Logistic
        var logisticPromoChosen = logisticPromo
        if (isDisableChangeCourier) {
            // set error log
            shippingRecommendationData.listLogisticPromo.firstOrNull()?.let {
                courierItemData.logPromoMsg = it.disableText
                courierItemData.logPromoDesc = it.description
            }
            // must get promo for tokonow
            logisticPromoChosen = shippingRecommendationData.listLogisticPromo.firstOrNull {
                it.promoCode.isNotEmpty() && !it.disabled
            }
        } else if (isForceReloadRates) {
            logisticPromoChosen = shippingRecommendationData.listLogisticPromo.firstOrNull {
                !it.disabled && it.isApplied
            }
        } else if (!isBoUnstackEnabled) {
            logisticPromoChosen = shippingRecommendationData.listLogisticPromo.firstOrNull {
                !it.disabled && it.shipperId == shipperId && it.shipperProductId == spId && it.promoCode.isNotEmpty()
            }
        }
        if (logisticPromoChosen?.shipperProductId != null && logisticPromoChosen.shipperProductId != courierItemData.shipperProductId) {
            val courierUiModel = shippingRecommendationData.shippingDurationUiModels.first {
                it.serviceData.serviceId == logisticPromoChosen.serviceId
            }.shippingCourierViewModelList.first {
                it.productData.shipperProductId == logisticPromoChosen.shipperProductId
            }
            courierItemData = shippingCourierConverter.convertToCourierItemDataNew(
                courierUiModel,
                shippingRecommendationData,
                validationMetadata
            )
        }

        logisticPromoChosen?.let {
            courierItemData.logPromoCode = it.promoCode
            courierItemData.discountedRate = it.discountedRate
            courierItemData.shippingRate = it.shippingRate
            courierItemData.benefitAmount = it.benefitAmount
            courierItemData.promoTitle = it.title
            courierItemData.isHideShipperName = it.hideShipperName
            courierItemData.shipperName = it.shipperName
            courierItemData.etaText = it.etaData.textEta
            courierItemData.etaErrorCode = it.etaData.errorCode
            courierItemData.freeShippingChosenCourierTitle = it.freeShippingChosenCourierTitle
            courierItemData.freeShippingChosenImage = it.imageChosenFreeShipping
            courierItemData.freeShippingMetadata = it.freeShippingMetadata
            courierItemData.benefitClass = it.benefitClass
            courierItemData.shippingSubsidy = it.shippingSubsidy
            courierItemData.boCampaignId = it.boCampaignId
            courierItemData.boOrderMessage = it.orderMessage
        }
        return courierItemData
    }

    private fun generateCourierItemDataWithLogisticPromo(
        shippingCourierUiModel: ShippingCourierUiModel,
        shippingRecommendationData: ShippingRecommendationData,
        logisticPromoUiModel: LogisticPromoUiModel
    ): CourierItemData {
        val courierItemData =
            shippingCourierConverter.convertToCourierItemDataNew(
                shippingCourierUiModel,
                shippingRecommendationData
            )

        courierItemData.apply {
            logPromoMsg = logisticPromoUiModel.disableText
            logPromoDesc = logisticPromoUiModel.description
            logPromoCode = logisticPromoUiModel.promoCode
            discountedRate = logisticPromoUiModel.discountedRate
            shippingRate = logisticPromoUiModel.shippingRate
            benefitAmount = logisticPromoUiModel.benefitAmount
            promoTitle = logisticPromoUiModel.title
            isHideShipperName = logisticPromoUiModel.hideShipperName
            shipperName = logisticPromoUiModel.shipperName
            etaText = logisticPromoUiModel.etaData.textEta
            etaErrorCode = logisticPromoUiModel.etaData.errorCode
            freeShippingChosenCourierTitle = logisticPromoUiModel.freeShippingChosenCourierTitle
            freeShippingChosenImage = logisticPromoUiModel.imageChosenFreeShipping
            freeShippingMetadata = logisticPromoUiModel.freeShippingMetadata
            benefitClass = logisticPromoUiModel.benefitClass
            shippingSubsidy = logisticPromoUiModel.shippingSubsidy
            boCampaignId = logisticPromoUiModel.boCampaignId
            boOrderMessage = logisticPromoUiModel.orderMessage
        }
        return courierItemData
    }
}

data class EditAddressResult(
    val isSuccess: Boolean,
    val errorMessage: String = "",
    val throwable: Throwable? = null
)

data class RatesResult(
    val courier: CourierItemData?,
    val couriers: List<ShippingCourierUiModel>,
    val ratesError: Throwable? = null
)

data class LogisticProcessorGetRatesParam(
    val ratesParam: RatesParam,
    val selectedServiceId: Int,
    val selectedSpId: Int,
    val boPromoCode: String,
    val shouldResetCourier: Boolean,
    val validationMetadata: String,
    val isDisableChangeCourier: Boolean,
    val currentServiceId: Int?,
    val isAutoCourierSelection: Boolean,
    val isTradeInDropOff: Boolean
)

data class LogisticProcessorGetSchellyParam(
    val schellyParam: ScheduleDeliveryParam,
    val validationMetadata: String
)
