package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokoPointPromosEntity(
    @Expose @SerializedName("catalog")
    var catalogs: CatalogEntity = CatalogEntity(),
    @Expose
    @SerializedName("coupons")
    var coupon: PromoCouponEntity = PromoCouponEntity()
)
