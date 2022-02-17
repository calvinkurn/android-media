package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddOnByProductResponse {
    @SerializedName("ProductID")
    @Expose
    var productID: String? = null

    @SerializedName("WarehouseID")
    @Expose
    var warehouseID: String? = null

    @SerializedName("AddOnLevel")
    @Expose
    var addOnLevel: String? = null

    @SerializedName("CouponText")
    @Expose
    var couponText: String? = null

    @SerializedName("Addons")
    @Expose
    var addons: List<Addon>? = null
}