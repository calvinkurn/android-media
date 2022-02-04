package com.tokopedia.purchase_platform.common.feature.addongifting.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnProductData(
        var products: List<Product> = emptyList()
) : Parcelable

@Parcelize
data class Product(
        var productId: String = "",
        var productName: String = "",
        var productImageUrl: String = "",
        var productPrice: Long = 0,
        var productQuantity: Int = 0
) : Parcelable