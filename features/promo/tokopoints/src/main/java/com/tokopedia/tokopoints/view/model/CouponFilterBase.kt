package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class CouponFilterBase(
    @SerializedName("filter")
    var filter: CouponFilterOuter = CouponFilterOuter()
)
