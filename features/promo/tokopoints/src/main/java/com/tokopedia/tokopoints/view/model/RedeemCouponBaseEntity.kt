package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RedeemCouponBaseEntity(
    @Expose
    @SerializedName("hachikoRedeem")
    var hachikoRedeem: RedeemCouponEntity = RedeemCouponEntity()
)
