package com.tokopedia.tokomember_seller_dashboard.domain.requestparam

import com.google.gson.annotations.SerializedName

data class TmCouponValidateRequest(

	@SerializedName("target_buyer")
	val targetBuyer: Int? = 0,

	@SerializedName("benefit_max")
	val benefitMax: Int? = 0,

	@SerializedName("image")
	val image: String? = "",

	@SerializedName("code")
	val code: String? = "",

	@SerializedName("min_purchase")
	val minPurchase: Int? = 0,

	@SerializedName("minimum_tier_level")
	val minimumTierLevel: Int? = 0,

	@SerializedName("benefit_percent")
	val benefitPercent: Int? = 0,

	@SerializedName("hour_end")
	val hourEnd: String? = "",

	@SerializedName("date_end")
	val dateEnd: String? = "",

	@SerializedName("source")
	val source: String? = "",

	@SerializedName("image_square")
	val imageSquare: String? = "",

	@SerializedName("coupon_name")
	val couponName: String? = "",

	@SerializedName("benefit_type")
	val benefitType: String? = "",

	@SerializedName("date_start")
	val dateStart: String? = "",

	@SerializedName("benefit_idr")
	val benefitIdr: Int? = 0,

	@SerializedName("quota")
	val quota: Int? = 0,

	@SerializedName("is_public")
	val isPublic: Int? = 0,

	@SerializedName("coupon_type")
	val couponType: String? = "",

	@SerializedName("hour_start")
	val hourStart: String? = ""
)
