package com.tokopedia.checkout.data.model.response.shipmentaddressform

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ShopShipment(
        @SuppressLint("Invalid Data Type")
        @SerializedName("ship_id")
        val shipId: Int = 0,
        @SerializedName("ship_name")
        val shipName: String = "",
        @SerializedName("ship_code")
        val shipCode: String = "",
        @SerializedName("ship_logo")
        val shipLogo: String = "",
        @SerializedName("ship_prods")
        val shipProds: List<ShipProd> = emptyList(),
        @SerializedName("is_dropship_enabled")
        val isDropshipEnabled: Int = 0
)