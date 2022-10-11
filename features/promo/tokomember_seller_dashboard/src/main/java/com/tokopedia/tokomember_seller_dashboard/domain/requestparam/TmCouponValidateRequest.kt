package com.tokopedia.tokomember_seller_dashboard.domain.requestparam

import com.google.gson.annotations.SerializedName

data class TmCouponValidateRequest(

	@SerializedName("target_buyer")
	val targetBuyer: Int? = null,

	@SerializedName("benefit_max")
	var benefitMax: Int? = null,

	@SerializedName("image")
	val image: String? = null,

	@SerializedName("code")
	val code: String? = null,

	@SerializedName("min_purchase")
	val minPurchase: Int? = null,

	@SerializedName("minimum_tier_level")
	val minimumTierLevel: Int? = null,

	@SerializedName("benefit_percent")
	val benefitPercent: Int? = null,

	@SerializedName("hour_end")
	val hourEnd: String? = null,

	@SerializedName("date_end")
	val dateEnd: String? = null,

	@SerializedName("source")
	val source: String? = null,

	@SerializedName("image_square")
	val imageSquare: String? = null,

	@SerializedName("coupon_name")
	val couponName: String? = null,

	@SerializedName("benefit_type")
	val benefitType: String? = null,

	@SerializedName("date_start")
	val dateStart: String? = null,

	@SerializedName("benefit_idr")
	var benefitIdr: Int? = null,

	@SerializedName("quota")
	val quota: Int? = null,

	@SerializedName("is_public")
	val isPublic: Int? = null,

	@SerializedName("coupon_type")
	val couponType: String? = null,

	@SerializedName("hour_start")
	val hourStart: String? = null
)
