package com.tokopedia.checkout.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShipmentCostModel(
        var totalItem: Int = 0,
        var totalItemPrice: Double = 0.0,
        var totalPrice: Double = 0.0,
        var totalWeight: Double = 0.0,
        var shippingFee: Double = 0.0,
        var insuranceFee: Double = 0.0,
        var priorityFee: Double = 0.0,
        var totalPurchaseProtectionItem: Int = 0,
        var purchaseProtectionFee: Double = 0.0,
        var additionalFee: Double = 0.0,
        var promoPrice: Double = 0.0,
        var donation: Double = 0.0,
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
        var cashbackAmount: Int = 0
) : Parcelable