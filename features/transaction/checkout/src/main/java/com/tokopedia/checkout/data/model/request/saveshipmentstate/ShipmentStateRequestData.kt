package com.tokopedia.checkout.data.model.request.saveshipmentstate

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class ShipmentStateRequestData(
        @SuppressLint("Invalid Data Type")
        @SerializedName("address_id")
        var addressId: Int = 0,
        @SerializedName("shop_products")
        var shopProductDataList: List<ShipmentStateShopProductData>? = null
)
