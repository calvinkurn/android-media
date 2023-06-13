package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class CouponSwipeUpdateOuter(
    @SerializedName("tokopointsSwipeCoupon")
    var swipeCoupon: CouponSwipeUpdate = CouponSwipeUpdate()
)
