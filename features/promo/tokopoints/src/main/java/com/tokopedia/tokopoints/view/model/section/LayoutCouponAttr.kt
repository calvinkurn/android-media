package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokopoints.view.model.CouponValueEntity

data class LayoutCouponAttr(
    @SerializedName("couponList")
    @Expose
    var couponList: MutableList<CouponValueEntity> = mutableListOf()
)
