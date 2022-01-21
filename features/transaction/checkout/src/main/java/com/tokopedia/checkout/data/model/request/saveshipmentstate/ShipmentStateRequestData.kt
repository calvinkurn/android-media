package com.tokopedia.checkout.data.model.request.saveshipmentstate

import com.google.gson.annotations.SerializedName

data class ShipmentStateRequestData(
        @SerializedName("address_id")
        var addressId: Int = 0,
        @SerializedName("shop_products")
        var shopProductDataList: List<ShipmentStateShopProductData>? = null
)