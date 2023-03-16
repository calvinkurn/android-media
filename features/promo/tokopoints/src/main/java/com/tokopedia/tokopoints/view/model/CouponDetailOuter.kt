package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class CouponDetailOuter(
    @SerializedName("detail")
    var detail: CouponValueEntity = CouponValueEntity()
)
