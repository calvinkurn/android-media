package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.checkout.view.uimodel.ShipmentAddOnSummaryModel
import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel
import com.tokopedia.checkoutpayment.domain.GoCicilInstallmentOption
import com.tokopedia.checkoutpayment.domain.PaymentFeeDetail
import com.tokopedia.checkoutpayment.view.OrderPaymentFee

data class CheckoutCostModel(
    override val cartStringGroup: String = "",
    val totalPrice: Double = 0.0,
    val totalPriceString: String = "-",
    val totalPriceWithAllPaymentFees: Double = 0.0,
    val totalPriceWithInternalPaymentFees: Double = 0.0,
    val totalItem: Int = 0,
    val originalItemPrice: Double = 0.0,
    val finalItemPrice: Double = 0.0,
    val totalWeight: Double = 0.0,
    val originalShippingFee: Double = 0.0,
    val finalShippingFee: Double = 0.0,
    val hasSelectAllShipping: Boolean = false,
    val shippingInsuranceFee: Double = 0.0,
    val hasInsurance: Boolean = false,
    var totalPurchaseProtectionItem: Int = 0,
    var purchaseProtectionFee: Double = 0.0,
    val additionalFee: Double = 0.0,
    val donation: Double = 0.0,
    var listCrossSell: List<CheckoutCrossSellModel> = emptyList(),
    var promoMessage: String? = null,
    var emasPrice: Double = 0.0,
    var tradeInPrice: Double = 0.0,
    var bookingFee: Int = 0,
    var discountLabel: String = "",
    var discountAmount: Int = 0,
    var isHasDiscountDetails: Boolean = false,
    var shippingDiscountLabel: String = "",
    var shippingDiscountAmount: Int = 0,
    var productDiscountLabel: String = "",
    var productDiscountAmount: Int = 0,
    var cashbackLabel: String = "",
    var cashbackAmount: Int = 0,
    var totalAddOnPrice: Double = 0.0,
    var hasAddOn: Boolean = false,
    var dynamicPlatformFee: ShipmentPaymentFeeModel = ShipmentPaymentFeeModel(),
    var listAddOnSummary: List<ShipmentAddOnSummaryModel> = emptyList(),
    val totalOtherFee: Double = 0.0,
    var isExpandOtherFee: Boolean = false,
    val dynamicPaymentFees: List<OrderPaymentFee>? = emptyList(),
    val originalPaymentFees: List<PaymentFeeDetail> = emptyList(),
    val usePaymentFees: Boolean = false,
    val isInstallment: Boolean = false,
    val installmentFee: Double = 0.0,
    val installmentDetail: GoCicilInstallmentOption? = null,
    var isExpandPaymentFee: Boolean = false,
    val useNewWording: Boolean = false
) : CheckoutItem {

    val finalPaymentFee: Double
        get() {
            return if (usePaymentFees) {
                (dynamicPaymentFees?.sumOf { it.fee } ?: 0.0) + originalPaymentFees.sumOf { it.amount }
            } else if (dynamicPlatformFee.fee <= 0) {
                0.0
            } else {
                dynamicPlatformFee.fee
            }
        }

    val totalDiscounts: Double
        get() {
            return (productDiscountAmount + shippingDiscountAmount + discountAmount).toDouble()
        }

    val totalProductAndShippingPrice: Double
        get() {
            return originalItemPrice + originalShippingFee
        }
}
