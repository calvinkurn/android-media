package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class CouponSwipePin(
    @SerializedName("need_pin")
    var isPinRequire: Boolean = false,
    @SerializedName("text")
    var text: String = ""
)
