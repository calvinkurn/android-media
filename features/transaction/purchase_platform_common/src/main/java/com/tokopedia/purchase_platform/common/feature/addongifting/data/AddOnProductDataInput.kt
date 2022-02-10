package com.tokopedia.purchase_platform.common.feature.addongifting.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnProductData(
        var bottomSheetTitle: String = "",
        var isTokoCabang: Boolean = false,
        var shopBadgeUrl: String = "",
        var addOnFooterMessages: List<String> = emptyList(),
        var addOnSavedStates: List<AddOnData> = emptyList(),
        var products: List<Product> = emptyList(),
        var unavailableBottomSheetData: UnavailableBottomSheetData = UnavailableBottomSheetData()
) : Parcelable

@Parcelize
data class Product(
        var productId: String = "",
        var productName: String = "",
        var productImageUrl: String = "",
        var productPrice: Long = 0,
        var productQuantity: Int = 0
) : Parcelable

@Parcelize
data class UnavailableBottomSheetData(
        var unavailableProducts: List<Product> = emptyList(),
        var description: String = "",
        var tickerMessage: String = ""
) : Parcelable