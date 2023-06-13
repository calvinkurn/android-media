package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApplyCouponEntity(
    @Expose
    @SerializedName("Success")
    private var isSuccess: Boolean = false
)
