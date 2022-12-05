package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class CouponSwipeDetail(
    @SerializedName("need_swipe")
    var isNeedSwipe: Boolean = false,
    @SerializedName("text")
    val text: String = "",
    @SerializedName("note")
    var note: String = "",
    @SerializedName("partner_code")
    var partnerCode: String = "",
    @SerializedName("pin")
    val pin: CouponSwipePin = CouponSwipePin(),
)
