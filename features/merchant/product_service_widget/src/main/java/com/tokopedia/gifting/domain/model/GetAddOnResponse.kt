package com.tokopedia.gifting.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetAddOnResponse {
    @SerializedName("GetAddOnByProduct")
    @Expose
    var getAddOnByProduct: GetAddOnByProduct = GetAddOnByProduct()
}

class GetAddOnByProduct {
    @SerializedName("error")
    @Expose
    var error: Error = Error()

    @SerializedName("StaticInfo")
    @Expose
    var staticInfo: StaticInfo = StaticInfo()

    @SerializedName("AddOnByProductResponse")
    @Expose
    var addOnByProductResponse: List<AddOnByProductResponse> = emptyList()
}

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
    var addons: List<Addon> = emptyList()
}