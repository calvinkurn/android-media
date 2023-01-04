package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.SerializedName

data class CouponSwipeUpdate(
    @SerializedName("partnerCode")
    var partnerCode: String = "",
    @SerializedName("note")
    var note: String = "",
    @SerializedName("resultStatus")
    var resultStatus: ResultStatusEntity = ResultStatusEntity()
)
