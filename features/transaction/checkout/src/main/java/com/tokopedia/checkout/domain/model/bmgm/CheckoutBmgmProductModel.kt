package com.tokopedia.checkout.domain.model.bmgm

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutBmgmProductModel(
    val productId: String = "",
    val productName: String = "",
    val imageUrl: String = "",
    val warehouseId: Long = 0L,
    val quantity: Int = 0,
    val priceBeforeBenefit: Double = 0.0,
    val priceAfterBenefit: Double = 0.0,
    val wholesalePrice: Double = 0.0,
    val cartId: String = ""
) : Parcelable
