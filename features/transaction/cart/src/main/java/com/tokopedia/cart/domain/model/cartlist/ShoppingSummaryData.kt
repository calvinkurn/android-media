package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShoppingSummaryData(
        var totalWording: String = "",
        var totalValue: Int = 0,
        var qty: String = "",
        var discountTotalWording: String = "",
        var discountValue: Int = 0,
        var paymentTotalWording: String = "",
        var promoWording: String = "",
        var promoValue: Int = 0,
        var sellerCashbackWording: String = "",
        var sellerCashbackValue: Int = 0
) : Parcelable