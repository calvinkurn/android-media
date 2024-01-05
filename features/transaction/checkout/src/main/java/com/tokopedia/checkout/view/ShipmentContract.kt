package com.tokopedia.checkout.view

import androidx.lifecycle.LiveData
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.*

/**
 * @author Irfan Khoirul on 24/04/18.
 */
interface ShipmentContract {
    interface AnalyticsActionListener {
        fun sendAnalyticsChoosePaymentMethodFailed(errorMessage: String?)
        fun sendEnhancedEcommerceAnalyticsCheckout(
            stringObjectMap: Map<String, Any>,
            tradeInCustomDimension: Map<String, String>?,
            transactionId: String?,
            userId: String,
            promoFlag: Boolean,
            eventCategory: String,
            eventAction: String,
            eventLabel: String,
            step: String
        )

        fun sendEnhancedEcommerceAnalyticsCrossSellClickPilihPembayaran(
            eventLabel: String,
            userId: String,
            listProducts: List<Any>?
        )

        fun sendAnalyticsOnClickChooseOtherAddressShipment()
        fun sendAnalyticsOnClickTopDonation()
        fun sendAnalyticsOnClickChangeAddress()
        fun sendAnalyticsOnClickCheckBoxDropShipperOption()
        fun sendAnalyticsOnClickCheckBoxInsuranceOption()
        fun sendAnalyticsScreenName(screenName: String)
        fun sendAnalyticsOnClickEditPinPointErrorValidation(message: String)
        fun sendAnalyticsCourierNotComplete()
        fun sendAnalyticsPromoRedState()
        fun sendAnalyticsDropshipperNotComplete()
        fun sendAnalyticsOnClickButtonCloseShipmentRecommendationDuration()
        fun sendAnalyticsOnClickChecklistShipmentRecommendationDuration(duration: String?)
        fun sendAnalyticsOnViewPreselectedCourierShipmentRecommendation(courier: String?)
        fun sendAnalyticsOnClickChangeCourierShipmentRecommendation(shipmentCartItemModel: ShipmentCartItemModel?)
        fun sendAnalyticsOnClickButtonCloseShipmentRecommendationCourier()
        fun sendAnalyticsOnClickChangeDurationShipmentRecommendation()
        fun sendAnalyticsOnViewPreselectedCourierAfterPilihDurasi(shippingProductId: Int)
        fun sendAnalyticsOnDisplayDurationThatContainPromo(
            isCourierPromo: Boolean,
            duration: String?
        )

        fun sendAnalyticsOnDisplayLogisticThatContainPromo(
            isCourierPromo: Boolean,
            shippingProductId: Int
        )

        fun sendAnalyticsOnClickDurationThatContainPromo(
            isCourierPromo: Boolean,
            duration: String?,
            isCod: Boolean,
            shippingPriceMin: String,
            shippingPriceHigh: String
        )

        fun sendAnalyticsOnClickLogisticThatContainPromo(
            isCourierPromo: Boolean,
            shippingProductId: Int,
            isCod: Boolean
        )

        fun sendAnalyticsViewInformationAndWarningTickerInCheckout(tickerId: String)
        fun sendAnalyticsViewPromoAfterAdjustItem(msg: String)
    }
}

@Suppress("UNCHECKED_CAST")
class CheckoutMutableLiveData<T>(initialValue: T) : LiveData<T>(initialValue) {

    override fun getValue(): T = super.getValue() as T

    public override fun setValue(value: T) {
        super.setValue(value)
    }
}
