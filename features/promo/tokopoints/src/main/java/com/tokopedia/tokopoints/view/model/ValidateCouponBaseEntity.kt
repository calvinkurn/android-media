package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ValidateCouponBaseEntity(
    @Expose
    @SerializedName("validateRedeem")
    var validateCoupon: ValidateCouponEntity = ValidateCouponEntity()
)
