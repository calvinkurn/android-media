package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApplyCouponBaseEntity(
    @Expose
    @SerializedName("apply_coupon")
    var applyCoupon: ApplyCouponEntity = ApplyCouponEntity(),
    @Expose
    @SerializedName("errors")
    var errors: ErrorEntity = ErrorEntity(),
)
