package com.tokopedia.checkout.view.subscriber

import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.network.exception.MessageErrorException
import rx.Subscriber
import timber.log.Timber

class GetCourierRecommendationSubscriber(private val view: ShipmentContract.View,
                                         private val presenter: ShipmentContract.Presenter,
                                         private val shipperId: Int,
                                         private val spId: Int,
                                         private val itemPosition: Int,
                                         private val shippingCourierConverter: ShippingCourierConverter,
                                         private val shipmentCartItemModel: ShipmentCartItemModel,
                                         private val isInitialLoad: Boolean,
                                         private val isTradeInDropOff: Boolean,
                                         private val isForceReloadRates: Boolean) : Subscriber<ShippingRecommendationData?>() {
    override fun onCompleted() {}
    override fun onError(e: Throwable) {
        Timber.d(e)
        if (isInitialLoad) {
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff)
        } else {
            view.updateCourierBottomsheetHasNoData(itemPosition, shipmentCartItemModel)
        }
        view.logOnErrorLoadCourier(e, itemPosition)
    }

    override fun onNext(shippingRecommendationData: ShippingRecommendationData?) {
        val shippingDurationViewModels = shippingRecommendationData?.shippingDurationViewModels
        if (isInitialLoad || isForceReloadRates) {
            if (shippingDurationViewModels != null && shippingDurationViewModels.isNotEmpty()) {
                for (shippingDurationUiModel in shippingDurationViewModels) {
                    val shippingCourierViewModelList = shippingDurationUiModel.shippingCourierViewModelList
                    if (shippingCourierViewModelList != null &&
                            shippingCourierViewModelList.isNotEmpty()) {
                        for (shippingCourierUiModel in shippingCourierViewModelList) {
                            shippingCourierUiModel.isSelected = false
                        }
                        for (shippingCourierUiModel in shippingCourierViewModelList) {
                            if (isTradeInDropOff || shippingCourierUiModel.productData.shipperProductId == spId &&
                                    shippingCourierUiModel.productData.shipperId == shipperId) {
                                if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                    view.renderCourierStateFailed(itemPosition, isTradeInDropOff)
                                    view.logOnErrorLoadCourier(MessageErrorException(shippingCourierUiModel.productData.error?.errorMessage), itemPosition)
                                    return
                                } else {
                                    shippingCourierUiModel.isSelected = true
                                    presenter.setShippingCourierViewModelsState(shippingCourierViewModelList, shipmentCartItemModel.orderNumber)
                                    view.renderCourierStateSuccess(generateCourierItemData(shippingCourierUiModel, shippingRecommendationData),
                                            itemPosition, isTradeInDropOff, isForceReloadRates)
                                    return
                                }
                            }
                        }
                    }
                }

                // corner case auto selection if BE default duration failed
                if (shipmentCartItemModel.isAutoCourierSelection) {
                    val shippingDuration = shippingDurationViewModels.firstOrNull { it.serviceData.error?.errorId.isNullOrEmpty() && it.serviceData.error?.errorMessage.isNullOrEmpty() }
                    if (shippingDuration != null) {
                        val shippingCourier = shippingDuration.shippingCourierViewModelList?.firstOrNull {
                            it.productData.error?.errorMessage.isNullOrEmpty()
                        }
                        if (shippingCourier != null) {
                            shippingCourier.isSelected = true
                            view.renderCourierStateSuccess(generateCourierItemData(shippingCourier, shippingRecommendationData),
                                    itemPosition, isTradeInDropOff, isForceReloadRates)
                            return
                        }
                    }
                }
            }
            view.renderCourierStateFailed(itemPosition, isTradeInDropOff)
            view.logOnErrorLoadCourier(MessageErrorException("rates empty data"), itemPosition)
        } else {
            if (shippingDurationViewModels != null && shippingDurationViewModels.isNotEmpty()) {
                for (shippingDurationUiModel in shippingDurationViewModels) {
                    for (productData in shippingDurationUiModel.serviceData.products) {
                        if (productData.shipperId == shipperId && productData.shipperProductId == spId) {
                            view.updateCourierBottomssheetHasData(
                                    shippingDurationUiModel.shippingCourierViewModelList,
                                    itemPosition, shipmentCartItemModel, shippingRecommendationData.preOrderModel
                            )
                            return
                        }
                    }
                }
            }
            view.updateCourierBottomsheetHasNoData(itemPosition, shipmentCartItemModel)
        }
    }

    private fun generateCourierItemData(shippingCourierUiModel: ShippingCourierUiModel, shippingRecommendationData: ShippingRecommendationData): CourierItemData {
        val courierItemData = shippingCourierConverter.convertToCourierItemData(shippingCourierUiModel)
        shippingRecommendationData.logisticPromo?.let {
            val disableMsg = it.disableText
            courierItemData.logPromoMsg = disableMsg
            courierItemData.logPromoDesc = it.description

            // Auto apply Promo Stacking Logistic
            if (((it.shipperId == shipperId && it.shipperProductId == spId) || shipmentCartItemModel.isAutoCourierSelection)
                    && it.promoCode.isNotEmpty() && !it.disabled) {
                courierItemData.logPromoCode = it.promoCode
                courierItemData.discountedRate = it.discountedRate
                courierItemData.shippingRate = it.shippingRate
                courierItemData.benefitAmount = it.benefitAmount
                courierItemData.promoTitle = it.title
                courierItemData.isHideShipperName = it.hideShipperName
                courierItemData.shipperName = it.shipperName
                courierItemData.etaText = it.etaData.textEta
                courierItemData.etaErrorCode = it.etaData.errorCode
            }
        }
        return courierItemData
    }
}