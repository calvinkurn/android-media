package com.tokopedia.checkout.data.model.request.saveshipmentstate

import com.google.gson.annotations.SerializedName

data class ShipmentStateProductData(
    @SerializedName("shop_id")
    var shopId: Long = 0,
    @SerializedName("product_id")
    var productId: Long = 0,
    @SerializedName("product_preorder")
    var productPreorder: ShipmentStateProductPreorder = ShipmentStateProductPreorder()
)
