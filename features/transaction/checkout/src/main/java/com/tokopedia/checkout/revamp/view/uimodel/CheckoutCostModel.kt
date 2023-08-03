package com.tokopedia.checkout.revamp.view.uimodel

import com.tokopedia.checkout.view.uimodel.ShipmentPaymentFeeModel

data class CheckoutCostModel(
    override val cartStringGroup: String = "",
    val totalPrice: Double = 0.0,
    val totalPriceString: String = "-",
    val totalItem: Int = 0,
    val originalItemPrice: Double = 0.0,
    val finalItemPrice: Double = 0.0,
    val totalWeight: Double = 0.0,
    val originalShippingFee: Double = 0.0,
    val finalShippingFee: Double = 0.0,
    val hasSelectAllShipping: Boolean = false,
    val shippingInsuranceFee: Double = 0.0,
    var totalPurchaseProtectionItem: Int = 0,
    var purchaseProtectionFee: Double = 0.0,
    val additionalFee: Double = 0.0,
    val donation: Double = 0.0,
    var listCrossSell: List<CheckoutCrossSellModel> = emptyList(),
    var promoMessage: String? = null,
    var emasPrice: Double = 0.0,
    var tradeInPrice: Double = 0.0,
    var totalPromoStackAmount: Int = 0,
    var totalPromoStackAmountStr: String? = null,
    var totalDiscWithoutCashback: Int = 0,
    var bookingFee: Int = 0,
    var discountLabel: String? = null,
    var discountAmount: Int = 0,
    var isHasDiscountDetails: Boolean = false,
    var shippingDiscountLabel: String? = null,
    var shippingDiscountAmount: Int = 0,
    var productDiscountLabel: String? = null,
    var productDiscountAmount: Int = 0,
    var cashbackLabel: String? = null,
    var cashbackAmount: Int = 0,
    var totalAddOnPrice: Double = 0.0,
    var hasAddOn: Boolean = false,
    var dynamicPlatformFee: ShipmentPaymentFeeModel = ShipmentPaymentFeeModel()
//    var listAddOnSummary: List<ShipmentAddOnSummaryModel> = emptyList()
) : CheckoutItem
