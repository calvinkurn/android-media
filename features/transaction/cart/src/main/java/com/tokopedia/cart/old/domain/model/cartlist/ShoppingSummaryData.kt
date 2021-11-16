package com.tokopedia.cart.old.domain.model.cartlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShoppingSummaryData(
        var qty: String = "",
        var totalWording: String = "",
        var totalValue: Int = 0,
        var discountTotalWording: String = "",
        var discountValue: Int = 0,
        var paymentTotalWording: String = "",
        var paymentTotal: Int = 0,
        var promoWording: String = "",
        var promoValue: Int = 0,
        var sellerCashbackWording: String = "",
        var sellerCashbackValue: Int = 0
) : Parcelable