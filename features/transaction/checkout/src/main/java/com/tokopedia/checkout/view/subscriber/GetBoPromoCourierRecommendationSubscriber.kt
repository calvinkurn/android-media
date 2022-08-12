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
import timber.log.Timber

class GetBoPromoCourierRecommendationSubscriber(private val view: ShipmentContract.View,
                                                private val presenter: ShipmentContract.Presenter,
                                                private val promoCode: String,
                                                private val shipperId: Int,
                                                private val spId: Int,
                                                private val itemPosition: Int,
                                                private val shippingCourierConverter: ShippingCourierConverter,
                                                private val shipmentCartItemModel: ShipmentCartItemModel,
                                                private val isInitialLoad: Boolean,
                                                private val isTradeInDropOff: Boolean,
                                                private val isForceReloadRates: Boolean) : Subscriber<ShippingRecommendationData?>() {

    /**
     * STR:
     * 1. user masuk ke checkout page dgn BO sdh dipilih di cart page
     * 2. app dapat bo_code not empty (misal bo_code = "123")
     * 3. rates gagal
     * 4. app cancel auto apply dgn code = "123"
     * 5. app show pilih pengiriman
     * 6. user masuk ke promo page
     * 7. user pilih BO lagi di promo page
     * 8. on result from promo
     * 9. app do apply BO
     * 10. rates gagal
     * 11. app cancel auto apply dgn code = "123"
     * 12. app renderCourierStateFailed
     * 13. app cancel auto apply dgn code = "123" (jd double hit dgn step 11)
     */


    override fun onCompleted() {}
    override fun onError(e: Throwable) {
        Timber.d(e)
        presenter.cancelAutoApplyPromoStackLogistic(itemPosition, promoCode)
        presenter.clearOrderPromoCodeFromLastValidateUseRequest(promoCode)
        view.resetCourier(shipmentCartItemModel)
//        if (isInitialLoad) {
        view.renderCourierStateFailed(itemPosition, isTradeInDropOff)
//        view.onNeedUpdateViewItem(itemPosition)
//        } else {
//            view.updateCourierBottomsheetHasNoData(itemPosition, shipmentCartItemModel)
//        }
        view.logOnErrorLoadCourier(e, itemPosition)
    }

    override fun onNext(shippingRecommendationData: ShippingRecommendationData?) {
//        shipmentCartItemModel.voucherLogisticItemUiModel = shipmentCartItemModel.voucherLogisticItemUiModel?.copy(code = promoCode)
        if (shippingRecommendationData?.shippingDurationUiModels != null && shippingRecommendationData.shippingDurationUiModels.isNotEmpty() && shippingRecommendationData.listLogisticPromo.isNotEmpty()) {
            val logisticPromo = shippingRecommendationData.listLogisticPromo.firstOrNull { it.promoCode == promoCode && !it.disabled }
            if (logisticPromo != null) {
                for (shippingDurationUiModel in shippingRecommendationData.shippingDurationUiModels) {
                    if (shippingDurationUiModel.shippingCourierViewModelList.isNotEmpty()) {
                        for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                            shippingCourierUiModel.isSelected = false
                        }
                        for (shippingCourierUiModel in shippingDurationUiModel.shippingCourierViewModelList) {
                            if (isTradeInDropOff || (shippingCourierUiModel.productData.shipperProductId == spId &&
                                            shippingCourierUiModel.productData.shipperId == shipperId)) {
                                if (!shippingCourierUiModel.productData.error?.errorMessage.isNullOrEmpty()) {
                                    presenter.cancelAutoApplyPromoStackLogistic(itemPosition, promoCode)
                                    presenter.clearOrderPromoCodeFromLastValidateUseRequest(promoCode)
                                    view.resetCourier(shipmentCartItemModel)
                                    view.renderCourierStateFailed(itemPosition, isTradeInDropOff)
                                    view.logOnErrorLoadCourier(MessageErrorException(shippingCourierUiModel.productData.error?.errorMessage), itemPosition)
                                    return
                                } else {
                                    shippingCourierUiModel.isSelected = true
                                    presenter.setShippingCourierViewModelsState(shippingDurationUiModel.shippingCourierViewModelList, shipmentCartItemModel.orderNumber)
                                    view.renderCourierStateSuccess(generateCourierItemData(shippingCourierUiModel, shippingRecommendationData, logisticPromo),
                                            itemPosition, isTradeInDropOff, isForceReloadRates)
                                    return
                                }
                            }
                        }
                    }
                }
            }
        }
        presenter.cancelAutoApplyPromoStackLogistic(itemPosition, promoCode)
        presenter.clearOrderPromoCodeFromLastValidateUseRequest(promoCode)
        view.resetCourier(shipmentCartItemModel)
        view.renderCourierStateFailed(itemPosition, isTradeInDropOff)
        view.logOnErrorLoadCourier(MessageErrorException("rates empty data"), itemPosition)
    }

    private fun generateCourierItemData(shippingCourierUiModel: ShippingCourierUiModel, shippingRecommendationData: ShippingRecommendationData, logisticPromoUiModel: LogisticPromoUiModel): CourierItemData {
        val courierItemData = shippingCourierConverter.convertToCourierItemData(shippingCourierUiModel)

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
        }
        return courierItemData
    }
}