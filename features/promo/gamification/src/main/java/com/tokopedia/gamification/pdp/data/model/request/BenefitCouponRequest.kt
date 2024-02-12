package com.tokopedia.gamification.pdp.data.model.request

import com.google.gson.annotations.SerializedName

data class BenefitCouponRequest(
    @SerializedName("serviceID")
    val serviceID: String,
    @SerializedName("categoryID")
    val categoryID: Long,
    @SerializedName("categoryIDCoupon")
    val categoryIDCoupon: Long,
    @SerializedName("page")
    val page: Long,
    @SerializedName("limit")
    val limit: Long,
)

