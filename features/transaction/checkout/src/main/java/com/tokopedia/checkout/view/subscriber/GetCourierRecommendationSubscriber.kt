package com.tokopedia.checkout.view.subscriber

import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.network.exception.MessageErrorException
import rx.Subscriber
import rx.subjects.PublishSubject
import timber.log.Timber

class GetCourierRecommendationSubscriber(
    private val view: ShipmentContract.View,
    private val presenter: ShipmentContract.Presenter,
    private val shipperId: Int,
    private val spId: Int,
    private val itemPosition: Int,
    private val shippingCourierConverter: ShippingCourierConverter,
    private val shipmentCartItemModel: ShipmentCartItemModel,
    private val isInitialLoad: Boolean,
    private val isTradeInDropOff: Boolean,
    private val isForceReloadRates: Boolean,
    private val isBoUnstackEnabled: Boolean,
    private val logisticDonePublisher: PublishSubject<Boolean>?
) : Subscriber<ShippingRecommendationData?>() {

    override fun onCompleted() {
        // no op
    }

    override fun onError(e: Throwable) {
        Timber.d(e)
        val boPromoCode = getBoPromoCode()
        if (isInitialLoad) {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, false)
        } else {
            view.updateCourierBottomsheetHasNoData(itemPosition, shipmentCartItemModel)
        }
        view.logOnErrorLoadCourier(e, itemPosition, boPromoCode)
        logisticDonePublisher?.onCompleted()
    }

    override fun onNext(shippingRecommendationData: ShippingRecommendationData?) {
        val boPromoCode = getBoPromoCode()
        var errorReason = "rates invalid data"
        if (isInitialLoad || isForceReloadRates) {
            if (shippingRecommendationData?.shippingDurationUiModels != null && shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                if (!isForceReloadRates && isBoUnstackEnabled && shipmentCartItemModel.boCode.isNotEmpty()) {
                    val logisticPromo =
                        shippingRecommendationData.listLogisticPromo.firstOrNull { it.promoCode == shipmentCartItemModel.boCode && !it.disabled }
                    if (logisticPromo != null) {
                        for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                            if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    shippingCourierUiModel.isSelected = false
                                }
                                for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                    if (shippingCourierUiModel.productData.shipperProductId == logisticPromo.shipperProductId && shippingCourierUiModel.productData.shipperId == logisticPromo.shipperId) {
                                        if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                            view.renderCourierStateFailed(
                                                itemPosition,
                                                isTradeInDropOff,
                                                false
                                            )
                                            view.logOnErrorLoadCourier(
                                                MessageErrorException(
                                                    shippingCourierUiModel.productData.error?.errorMessage
                                                ), itemPosition,
                                                boPromoCode
                                            )
                                            logisticDonePublisher?.onCompleted()
                                            return
                                        } else {
                                            shippingCourierUiModel.isSelected = true
                                            presenter.setShippingCourierViewModelsState(
                                                shippingDurationUiModel.shippingCourierViewModelList,
                                                shipmentCartItemModel.orderNumber
                                            )
                                            view.renderCourierStateSuccess(
                                                generateCourierItemData(
                                                    shippingCourierUiModel,
                                                    shippingRecommendationData,
                                                    logisticPromo
                                                ),
                                                itemPosition,
                                                isTradeInDropOff,
                                                isForceReloadRates
                                            )
                                            return
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
                            val selectedSpId = getSelectedSpId(shipmentCartItemModel, spId, shippingDurationUiModel)
                            for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                if (isTradeInDropOff || (shippingCourierUiModel.productData.shipperProductId == selectedSpId && !shippingCourierUiModel.serviceData.isUiRatesHidden)) {
                                    if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                        view.renderCourierStateFailed(
                                            itemPosition,
                                            isTradeInDropOff,
                                            false
                                        )
                                        view.logOnErrorLoadCourier(
                                            MessageErrorException(
                                                shippingCourierUiModel.productData.error?.errorMessage
                                            ),
                                            itemPosition,
                                            boPromoCode
                                        )
                                        logisticDonePublisher?.onCompleted()
                                        return
                                    } else {
                                        val courierItemData = generateCourierItemData(
                                            shippingCourierUiModel,
                                            shippingRecommendationData
                                        )
                                        if (shippingCourierUiModel.productData.isUiRatesHidden && shippingCourierUiModel.serviceData.selectedShipperProductId == 0 && courierItemData.logPromoCode.isNullOrEmpty()) {
                                            // courier should only be used with BO, but no BO code found
                                            view.renderCourierStateFailed(
                                                itemPosition, isTradeInDropOff, false
                                            )
                                            view.logOnErrorLoadCourier(
                                                MessageErrorException("rates ui hidden but no promo"),
                                                itemPosition,
                                                boPromoCode
                                            )
                                            logisticDonePublisher?.onCompleted()
                                            return
                                        }
                                        shippingCourierUiModel.isSelected = true
                                        presenter.setShippingCourierViewModelsState(
                                            shippingDurationUiModel.shippingCourierViewModelList,
                                            shipmentCartItemModel.orderNumber
                                        )
                                        view.renderCourierStateSuccess(
                                            courierItemData,
                                            itemPosition,
                                            isTradeInDropOff,
                                            isForceReloadRates
                                        )
                                        return
                                    }
                                }
                            }
                        }
                    }

                    // corner case auto selection if BE default duration failed
                    if (shipmentCartItemModel.isAutoCourierSelection) {
                        val shippingDuration =
                            shippingRecommendationData.shippingDurationUiModels.firstOrNull { it.serviceData.error?.errorId.isNullOrEmpty() && it.serviceData.error?.errorMessage.isNullOrEmpty() }
                        if (shippingDuration != null) {
                            val shippingCourier =
                                shippingDuration.shippingCourierViewModelList.firstOrNull {
                                    it.productData.error?.errorMessage.isNullOrEmpty()
                                }
                            if (shippingCourier != null) {
                                shippingCourier.isSelected = true
                                view.renderCourierStateSuccess(
                                    generateCourierItemData(
                                        shippingCourier, shippingRecommendationData
                                    ),
                                    itemPosition,
                                    isTradeInDropOff,
                                    isForceReloadRates
                                )
                                return
                            }
                        }
                    }
                }
            } else {
                errorReason = "rates empty data"
            }
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff, false)
            view.logOnErrorLoadCourier(MessageErrorException(errorReason), itemPosition,
                boPromoCode
            )
        } else {
            if (shippingRecommendationData?.shippingDurationUiModels != null && shippingRecommendationData.shippingDurationUiModels.isNotEmpty()) {
                for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                    for (productData in shippingDurationUiModel.serviceData.products) {
                        if (productData.shipperId == shipperId && productData.shipperProductId == spId) {
                            view.updateCourierBottomssheetHasData(
                                shippingDurationUiModel.shippingCourierViewModelList,
                                itemPosition,
                                shipmentCartItemModel,
                                shippingRecommendationData.preOrderModel
                            )
                            logisticDonePublisher?.onCompleted()
                            return
                        }
                    }
                }
            }
            view.updateCourierBottomsheetHasNoData(itemPosition, shipmentCartItemModel)
        }
        logisticDonePublisher?.onCompleted()
    }

    private fun getSelectedSpId(
        shipmentCartItemModel: ShipmentCartItemModel,
        spId: Int,
        shippingDurationUiModel: ShippingDurationUiModel
    ): Int {
        val currentServiceId =
            shipmentCartItemModel.selectedShipmentDetailData?.selectedCourier?.serviceId
        return if (currentServiceId != null &&
            currentServiceId > 0 &&
            shippingDurationUiModel.serviceData.serviceId == currentServiceId &&
            shippingDurationUiModel.serviceData.selectedShipperProductId > 0
        ) {
            shippingDurationUiModel.serviceData.selectedShipperProductId
        } else {
            spId
        }
    }

    private fun generateCourierItemData(
        shippingCourierUiModel: ShippingCourierUiModel,
        shippingRecommendationData: ShippingRecommendationData,
        logisticPromo: LogisticPromoUiModel? = null
    ): CourierItemData {
        var courierItemData =
            shippingCourierConverter.convertToCourierItemData(shippingCourierUiModel)

        // Auto apply Promo Stacking Logistic
        var logisticPromoChosen = logisticPromo
        if (shipmentCartItemModel.isDisableChangeCourier) {
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
            courierItemData = shippingCourierConverter.convertToCourierItemData(courierUiModel)
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
            courierItemData.freeShippingMetadata = it.freeShippingMetadata
            courierItemData.benefitClass = it.benefitClass
            courierItemData.shippingSubsidy = it.shippingSubsidy
            courierItemData.boCampaignId = it.boCampaignId
        }
        return courierItemData
    }

    private fun getBoPromoCode(): String {
        if (isBoUnstackEnabled && !isForceReloadRates) {
            return shipmentCartItemModel.boCode
        }
        return ""
    }
}
