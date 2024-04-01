package com.tokopedia.shopdiscount.subsidy.model.response

import com.google.gson.annotations.SerializedName

data class SubsidyInfoResponse(
    @SerializedName("cta_program_link")
    val ctaProgramLink: String = "",
    @SerializedName("subsidy_type")
    val subsidyType: Int = 0,
    @SerializedName("discounted_price")
    val discountedPrice: Double = 0.0,
    @SerializedName("discounted_percentage")
    val discountedPercentage: Int = 0,
    @SerializedName("remaining_quota")
    val remainingQuota: Int = 0,
    @SerializedName("quota_subsidy")
    val quotaSubsidy: Int = 0,
    @SerializedName("subsidy_date_start")
    val subsidyDateStart: String = "",
    @SerializedName("subsidy_date_stop")
    val subsidyDateEnd: String = "",
    @SerializedName("max_order")
    val maxOrder: Int = 0,
    @SerializedName("seller_discount_price")
    val sellerDiscountPrice: Double = 0.0,
    @SerializedName("seller_discount_percentage")
    val sellerDiscountPercentage: Int = 0,
)
