package com.tokopedia.shop.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopShipmentData(
        val image: String,
        val name: String,
        val product: List<ShipmentProductData>
): Parcelable {

    @Parcelize
    data class ShipmentProductData(
            val isAvailable: Int,
            val shipProdID: String,
            val name: String,
            val uiHidden: Boolean
    ): Parcelable
}