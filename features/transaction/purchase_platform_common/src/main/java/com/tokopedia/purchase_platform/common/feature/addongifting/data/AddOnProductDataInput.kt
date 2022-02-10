package com.tokopedia.purchase_platform.common.feature.addongifting.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnProductData(
        var bottomSheetType: Int = ADD_ON_BOTTOM_SHEET,
        var bottomSheetTitle: String = "",
        var isTokoCabang: Boolean = false,
        var shopBadgeUrl: String = "",
        var addOnFooterMessages: List<String> = emptyList(),
        var addOnSavedStates: List<AddOnData> = emptyList(),
        var products: List<Product> = emptyList(),
        var unavailableBottomSheetData: UnavailableBottomSheetData = UnavailableBottomSheetData()
) : Parcelable {

    companion object {
        const val ADD_ON_BOTTOM_SHEET = 1
        const val ADD_ON_UNAVAILABLE_BOTTOM_SHEET = 2
    }

}

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