package com.tokopedia.checkout.domain.model.bmgm

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutBmgmBenefitProductModel(
    val productId: String = "",
    val productName: String = "",
    val imageUrl: String = "",
    val quantity: Int = 0,
    val originalPrice: Double = 0.0,
    val finalPrice: Double = 0.0,
    val weight: Int = 0,
    val weightActual: Int = 0
) : Parcelable
