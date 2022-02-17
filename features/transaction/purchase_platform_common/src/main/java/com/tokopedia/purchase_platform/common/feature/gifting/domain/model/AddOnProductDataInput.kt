package com.tokopedia.purchase_platform.common.feature.gifting.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnProductData(
        var source: String = "",
        var bottomSheetType: Int = 0,
        var bottomSheetTitle: String = "",
        var availableBottomSheetData: AvailableBottomSheetData = AvailableBottomSheetData(),
        var unavailableBottomSheetData: UnavailableBottomSheetData = UnavailableBottomSheetData()
) : Parcelable {

    companion object {
        const val ADD_ON_BOTTOM_SHEET = 1
        const val ADD_ON_UNAVAILABLE_BOTTOM_SHEET = 2

        const val SOURCE_NORMAL_CHECKOUT = "normal"
        const val SOURCE_ONE_CLICK_SHIPMENT = "ocs"
        const val SOURCE_ONE_CLICK_CHECKOUT = "occ"
    }

}

@Parcelize
data class Product(
        var cartId: String = "",
        var productId: String = "",
        var productName: String = "",
        var productImageUrl: String = "",
        var productPrice: Long = 0,
        var productQuantity: Int = 0
) : Parcelable

@Parcelize
data class AvailableBottomSheetData(
        var isTokoCabang: Boolean = false,
        var addOnInfoWording: AddOnWordingData = AddOnWordingData(),
        var addOnSavedStates: List<AddOnData> = emptyList(),
        var products: List<Product> = emptyList(),
        var cartString: String = "",
        var warehouseId: String = "",
        var shopName: String = ""
) : Parcelable

@Parcelize
data class UnavailableBottomSheetData(
        var unavailableProducts: List<Product> = emptyList(),
        var description: String = "",
        var tickerMessage: String = ""
) : Parcelable