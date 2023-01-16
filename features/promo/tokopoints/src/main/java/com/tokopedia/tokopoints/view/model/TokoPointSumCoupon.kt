package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class TokoPointSumCoupon(
    @SerializedName("sumNewCoupon")
    var sumCoupon: Int = 0,
    @SerializedName("sumNewCouponStr")
    var sumCouponStr: String = "",
)
