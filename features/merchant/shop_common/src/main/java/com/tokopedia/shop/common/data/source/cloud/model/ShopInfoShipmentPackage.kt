package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoShipmentPackage {
    @SerializedName("product_name")
    @Expose
    var productName: String? = null

    @SerializedName("shipping_id")
    @Expose
    var shippingId: String? = null
}