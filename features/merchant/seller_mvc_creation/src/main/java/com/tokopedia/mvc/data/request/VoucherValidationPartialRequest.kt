package com.tokopedia.mvc.data.request


import com.google.gson.annotations.SerializedName

data class VoucherValidationPartialRequest(
    @SerializedName("benefit_idr")
    val benefitIdr: Long,
    @SerializedName("benefit_max")
    val benefitMax: Long,
    @SerializedName("benefit_percent")
    val benefitPercent: Int,
    @SerializedName("benefit_type")
    val benefitType: String,
    @SerializedName("coupon_type")
    val couponType: String,
    @SerializedName("is_lock_to_product")
    val isLockToProduct: Int,
    @SerializedName("min_purchase")
    val minPurchase: Long,
    @SerializedName("product_ids")
    val productIds: String,
    @SerializedName("source")
    val source: String = "seller app",
    @SerializedName("target_buyer")
    val targetBuyer: Int = 0
)
