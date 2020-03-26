package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.PromoCheckoutErrorDefault
import com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply.LastApplyUiModel
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProfileResponse

data class OrderData(
        var cart: OrderCart = OrderCart(),
        var preference: ProfileResponse = ProfileResponse(),
        var promo: OrderPromo = OrderPromo()
)

data class OrderPromo(
        var lastApply: LastApplyUiModel? = null,
        var promoErrorDefault: PromoCheckoutErrorDefault? = null
)

data class Shipment(
        val serviceName: String? = null,
        val serviceId: Int? = null,
        val serviceDuration: String? = null,
        val serviceErrorMessage: String? = null,
        val isServicePickerEnable: Boolean = false,
        val needPinpoint: Boolean = false,
        val shipperName: String? = null,
        val shipperId: Int? = null,
        val shipperProductId: Int? = null,
        val ratesId: String? = null,
        val ut: String? = null,
        val checksum: String? = null,
        val shippingPrice: Int? = null,
        val logisticPromoTickerMessage: String? = null,
        val logisticPromoViewModel: LogisticPromoUiModel? = null, // BBO ?
        val logisticPromoShipping: ShippingCourierUiModel? = null, // BBO ?
        val isApplyLogisticPromo: Boolean = false, // BBO ?
        val shippingRecommendationData: ShippingRecommendationData? = null,
        val insuranceData: InsuranceData? = null,
        val isCheckInsurance: Boolean = false
) {
    fun getRealShipperProductId(): Int {
        return logisticPromoShipping?.productData?.shipperProductId ?: shipperProductId.toZeroIfNull()
    }

    fun getRealShipperId(): Int {
        return logisticPromoShipping?.productData?.shipperId ?: shipperId.toZeroIfNull()
    }
    fun getRealRatesId(): String {
        return logisticPromoShipping?.ratesId ?: ratesId ?: ""
    }
    fun getRealUt(): String {
        return logisticPromoShipping?.productData?.unixTime ?: ut ?: ""
    }
    fun getRealChecksum(): String {
        return logisticPromoShipping?.productData?.checkSum ?: checksum ?: ""
    }
}

data class Payment(
        val image: String? = null,
        val description: String? = null,
        val gatewayCode: String? = null,
        val url: String? = null,
        val gatewayName: String? = null,
        val errorMessage: String? = null
)

data class Address(
        val latitude: String? = null,
        val addressId: Int? = null,
        val addressName: String? = null,
        val provinceName: String? = null,
        val districtName: String? = null,
        val cityName: String? = null,
        val provinceId: Int? = null,
        val phone: String? = null,
        val addressStreet: String? = null,
        val receiverName: String? = null,
        val districtId: Int? = null,
        val postalCode: String? = null,
        val cityId: Int? = null,
        val longitude: String? = null
)

data class Insurance(
        val insuranceData: InsuranceData? = null
)

data class OrderTotal(
        val orderCost: OrderCost = OrderCost(),
        val buttonState: ButtonBayarState = ButtonBayarState.DISABLE,
        val isButtonChoosePayment: Boolean = false,
        val paymentErrorMessage: String? = null
)

data class OrderCost(
//        val totalItem: Int,
        val totalItemPrice: Double = 0.0,
        val totalPrice: Double = 0.0,
//        val totalWeight: Double,
        val shippingFee: Double = 0.0,
        val insuranceFee: Double = 0.0,
        val paymentFee: Double = 0.0,
//        val priorityFee: Double,
//        val totalPurchaseProtectionItem: Int,
//        val purchaseProtectionFee: Double,
//        val additionalFee: Double,
//        val promoPrice: Double,
//        val donation: Double,
//        val promoMessage: String,
//        val emasPrice: Double,
//        val tradeInPrice: Double,
//        val totalPromoStackAmount: Int,
//        val totalPromoStackAmountStr: String,
//        val TotalDiscWithoutCashback: Int,
//        val macroInsurancePrice: Long,
//        val macroInsurancePriceLabel: String,
//        val bookingFee: Int,
//        val discountLabel: String,
//        val discountAmount: Int,
//        val hasDiscountDetails: Boolean,
//        val shippingDiscountLabel: String,
        val shippingDiscountAmount: Int = 0
//        val productDiscountLabel: String,
//        val productDiscountAmount: Int,
//        val cashbackLabel: String,
//        val cashbackAmount: Int
)

enum class ButtonBayarState { NORMAL, LOADING, DISABLE }