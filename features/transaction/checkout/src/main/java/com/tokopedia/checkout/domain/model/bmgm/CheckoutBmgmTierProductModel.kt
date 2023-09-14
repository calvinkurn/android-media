package com.tokopedia.checkout.domain.model.bmgm

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutBmgmTierProductModel(
    val tierId: Long = 0L,
    val tierName: String = "",
    val tierMessage: String = "",
    val tierDiscountText: String = "",
    val tierDiscountAmount: String = "",
    val priceBeforeBenefit: Double = 0.0,
    val priceAfterBenefit: Double = 0.0,
    val listProduct: List<CheckoutBmgmProductModel> = emptyList()
) : Parcelable
