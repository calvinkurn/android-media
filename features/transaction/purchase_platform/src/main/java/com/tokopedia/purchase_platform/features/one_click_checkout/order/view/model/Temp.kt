package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.model

import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingRecommendationData
import com.tokopedia.logisticdata.data.entity.ratescourierrecommendation.InsuranceData
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.ProfileResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderPreference

data class OrderData(
        var cart: OrderCart = OrderCart(),
        var preference: ProfileResponse = ProfileResponse()
)

data class OrderPreferenceTemp(
        val id: Int = 0,
        val address: Address = Address(),
        val shipment: Shipment = Shipment(),
        val payment: Payment = Payment(),
        val isLoadingRates: Boolean = false
)

data class Shipment(
        val serviceName: String? = null,
        val serviceId: Int? = null,
        val serviceDuration: String? = null,
        val serviceErrorMessage: String? = null,
        val isServicePickerEnable: Boolean = false,
        val shipperName: String? = null,
        val shipperProductId: Int? = null,
        val shippingPrice: Int? = null,
        val logisticPromoTickerMessage: String? = null,
        val logisticPromoViewModel: LogisticPromoUiModel? = null, // BBO ?
        val shippingRecommendationData: ShippingRecommendationData? = null,
        val insuranceData: InsuranceData? = null,
        val isCheckInsurance: Boolean = false
)

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
        val isButtonChoosePayment: Boolean = false
)

data class OrderCost(
//        val totalItem: Int,
        val totalItemPrice: Double = 0.0,
        val totalPrice: Double = 0.0,
//        val totalWeight: Double,
        val shippingFee: Double = 0.0,
        val insuranceFee: Double = 0.0
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
//        val shippingDiscountAmount: Int,
//        val productDiscountLabel: String,
//        val productDiscountAmount: Int,
//        val cashbackLabel: String,
//        val cashbackAmount: Int
)

enum class ButtonBayarState { NORMAL, LOADING, DISABLE }