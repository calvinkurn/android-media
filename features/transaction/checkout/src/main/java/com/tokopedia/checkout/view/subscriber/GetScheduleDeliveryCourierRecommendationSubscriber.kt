package com.tokopedia.checkout.view.subscriber

import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.network.exception.MessageErrorException
import rx.Subscriber
import timber.log.Timber

/**
 * Created by victor on 25/10/22
 */

class GetScheduleDeliveryCourierRecommendationSubscriber(
    private val view: ShipmentContract.View,
    private val presenter: ShipmentContract.Presenter,
    private val shipperId: Int,
    private val spId: Int,
    private val itemPosition: Int,
    private val shippingCourierConverter: ShippingCourierConverter,
    private val shipmentCartItemModel: ShipmentCartItemModel,
    private val isInitialLoad: Boolean,
    private val isForceReloadRates: Boolean,
    private val isBoUnstackEnabled: Boolean
) : Subscriber<ShippingRecommendationData?>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        Timber.d(e)
        if (isInitialLoad) {
            view.renderCourierStateFailed(itemPosition, false, false)
        } else {
            view.updateCourierBottomsheetHasNoData(itemPosition, shipmentCartItemModel)
        }
        view.logOnErrorLoadCourier(e, itemPosition)
    }

    override fun onNext(shippingRecommendationData: ShippingRecommendationData?) {
        if (isInitialLoad || isForceReloadRates) {
            if (shippingRecommendationData?.shippingDurationUiModels != null && shippingRecommendationData.shippingDurationUiModels.isNotEmpty() && shippingRecommendationData.scheduleDeliveryData != null) {
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
                                                false,
                                                false
                                            )
                                            view.logOnErrorLoadCourier(
                                                MessageErrorException(
                                                    shippingCourierUiModel.productData.error?.errorMessage
                                                ),
                                                itemPosition
                                            )
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
                                                false,
                                                isForceReloadRates
                                            )
                                            return
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                        if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                            for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                shippingCourierUiModel.isSelected = false
                            }
                            val selectedSpId =
                                if (shippingDurationUiModel.serviceData.selectedShipperProductId > 0) {
                                    shippingDurationUiModel.serviceData.selectedShipperProductId
                                } else {
                                    spId
                                }
                            for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                                if (shippingCourierUiModel.productData.shipperProductId == selectedSpId && !shippingCourierUiModel.serviceData.isUiRatesHidden) {
                                    if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                        view.renderCourierStateFailed(
                                            itemPosition,
                                            false,
                                            false
                                        )
                                        view.logOnErrorLoadCourier(
                                            MessageErrorException(
                                                shippingCourierUiModel.productData.error?.errorMessage
                                            ),
                                            itemPosition
                                        )
                                        return
                                    } else {
                                        val courierItemData = generateCourierItemData(
                                            shippingCourierUiModel,
                                            shippingRecommendationData
                                        )
                                        if (shippingCourierUiModel.productData.isUiRatesHidden && shippingCourierUiModel.serviceData.selectedShipperProductId == 0 && courierItemData.logPromoCode.isNullOrEmpty()) {
                                            // courier should only be used with BO, but no BO code found
                                            view.renderCourierStateFailed(
                                                itemPosition,
                                                false,
                                                false
                                            )
                                            view.logOnErrorLoadCourier(
                                                MessageErrorException("rates ui hidden but no promo"),
                                                itemPosition
                                            )
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
                                            false,
                                            isForceReloadRates
                                        )
                                        return
                                    }
                                }
                            }
                        }
                    }

                    // corner case auto selection if BE default duration failed
                    if (shipmentCartItemModel.isAutoCourierSelection || shipmentCartItemModel.isDisableChangeCourier) {
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
                                        shippingCourier,
                                        shippingRecommendationData
                                    ),
                                    itemPosition,
                                    false,
                                    isForceReloadRates
                                )
                                return
                            }
                        }
                    }
                }
            }
            view.renderCourierStateFailed(itemPosition, false, false)
            view.logOnErrorLoadCourier(MessageErrorException("rates empty data"), itemPosition)
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
                            return
                        }
                    }
                }
            }
            view.updateCourierBottomsheetHasNoData(itemPosition, shipmentCartItemModel)
        }
    }

    private fun generateCourierItemData(
        shippingCourierUiModel: ShippingCourierUiModel,
        shippingRecommendationData: ShippingRecommendationData,
        logisticPromo: LogisticPromoUiModel? = null
    ): CourierItemData {
        var courierItemData =
            shippingCourierConverter.convertToCourierItemData(shippingCourierUiModel, shippingRecommendationData, shipmentCartItemModel)

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
            courierItemData = shippingCourierConverter.convertToCourierItemData(courierUiModel, shippingRecommendationData, shipmentCartItemModel)
        }

        if (courierItemData.scheduleDeliveryUiModel != null) {
            val isScheduleDeliverySelected = courierItemData.scheduleDeliveryUiModel?.isSelected
            if (isScheduleDeliverySelected == true &&
                (
                    courierItemData.scheduleDeliveryUiModel?.timeslotId != shipmentCartItemModel.timeslotId ||
                    courierItemData.scheduleDeliveryUiModel?.scheduleDate != shipmentCartItemModel.scheduleDate
                )
            ) {
                shipmentCartItemModel.scheduleDate =
                    courierItemData.scheduleDeliveryUiModel?.scheduleDate ?: ""
                shipmentCartItemModel.timeslotId =
                    courierItemData.scheduleDeliveryUiModel?.timeslotId ?: 0
                shipmentCartItemModel.validationMetadata =
                    courierItemData.scheduleDeliveryUiModel?.deliveryProduct?.validationMetadata
                        ?: ""
            }
            else if (isScheduleDeliverySelected == false) {
                shipmentCartItemModel.scheduleDate = ""
                shipmentCartItemModel.timeslotId = 0L
                shipmentCartItemModel.validationMetadata = ""
            }
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
}
