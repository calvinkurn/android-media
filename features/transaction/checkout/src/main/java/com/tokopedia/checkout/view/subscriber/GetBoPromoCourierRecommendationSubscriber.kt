package com.tokopedia.checkout.view.subscriber

import com.tokopedia.checkout.view.ShipmentContract
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierConverter
import com.tokopedia.logisticcart.shipping.model.CourierItemData
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.network.exception.MessageErrorException
import rx.Subscriber
import rx.subjects.PublishSubject
import timber.log.Timber

class GetBoPromoCourierRecommendationSubscriber(
    private val view: ShipmentContract.View,
    private val presenter: ShipmentContract.Presenter,
    private val uniqueId: String,
    private val promoCode: String,
    private val shipperId: Int,
    private val spId: Int,
    private val itemPosition: Int,
    private val shippingCourierConverter: ShippingCourierConverter,
    private val shipmentCartItemModel: ShipmentCartItemModel,
    private val isTradeInDropOff: Boolean,
    private val isForceReloadRates: Boolean,
    private val logisticPromoDonePublisher: PublishSubject<Boolean>?
) : Subscriber<ShippingRecommendationData?>() {

    override fun onCompleted() {}
    override fun onError(e: Throwable) {
        Timber.d(e)
        presenter.cancelAutoApplyPromoStackLogistic(itemPosition, promoCode, shipmentCartItemModel)
        presenter.clearOrderPromoCodeFromLastValidateUseRequest(uniqueId, promoCode)
        view.resetCourier(shipmentCartItemModel)
        view.renderCourierStateFailed(itemPosition, isTradeInDropOff, true)
        view.logOnErrorLoadCourier(e, itemPosition)
        logisticPromoDonePublisher?.onCompleted()
    }

    override fun onNext(shippingRecommendationData: ShippingRecommendationData?) {
        if (shippingRecommendationData?.shippingDurationUiModels != null && shippingRecommendationData.shippingDurationUiModels.isNotEmpty() && shippingRecommendationData.listLogisticPromo.isNotEmpty()) {
            val logisticPromo =
                shippingRecommendationData.listLogisticPromo.firstOrNull { it.promoCode == promoCode && !it.disabled }
            if (logisticPromo != null) {
                for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                    if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                        for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                            shippingCourierUiModel.isSelected = false
                        }
                        for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                            if (isTradeInDropOff || (shippingCourierUiModel.productData.shipperProductId == spId && shippingCourierUiModel.productData.shipperId == shipperId)) {
                                if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                    presenter.cancelAutoApplyPromoStackLogistic(
                                        itemPosition,
                                        promoCode,
                                        shipmentCartItemModel
                                    )
                                    presenter.clearOrderPromoCodeFromLastValidateUseRequest(
                                        uniqueId,
                                        promoCode
                                    )
                                    view.resetCourier(shipmentCartItemModel)
                                    view.renderCourierStateFailed(
                                        itemPosition,
                                        isTradeInDropOff,
                                        true
                                    )
                                    view.logOnErrorLoadCourier(
                                        MessageErrorException(
                                            shippingCourierUiModel.productData.error?.errorMessage
                                        ),
                                        itemPosition
                                    )
                                    logisticPromoDonePublisher?.onCompleted()
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
            }
        }
        presenter.cancelAutoApplyPromoStackLogistic(itemPosition, promoCode, shipmentCartItemModel)
        presenter.clearOrderPromoCodeFromLastValidateUseRequest(uniqueId, promoCode)
        view.resetCourier(shipmentCartItemModel)
        view.renderCourierStateFailed(itemPosition, isTradeInDropOff, true)
        view.logOnErrorLoadCourier(MessageErrorException("rates empty data"), itemPosition)
        logisticPromoDonePublisher?.onCompleted()
    }

    private fun generateCourierItemData(
        shippingCourierUiModel: ShippingCourierUiModel,
        shippingRecommendationData: ShippingRecommendationData,
        logisticPromoUiModel: LogisticPromoUiModel
    ): CourierItemData {
        val courierItemData =
            shippingCourierConverter.convertToCourierItemData(shippingCourierUiModel)

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
            freeShippingMetadata = logisticPromoUiModel.freeShippingMetadata
            benefitClass = logisticPromoUiModel.benefitClass
            shippingSubsidy = logisticPromoUiModel.shippingSubsidy
            boCampaignId = logisticPromoUiModel.boCampaignId
        }
        return courierItemData
    }
}
