package com.tokopedia.tokomember_seller_dashboard.domain.requestparam

import com.google.gson.annotations.SerializedName

data class TmCouponUpdateRequest(

	@SerializedName("benefit_max")
	val benefitMax: Int? = null,

	@SerializedName("target_buyer")
	val targetBuyer: Int? = null,

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

	@SerializedName("token")
	val token: String? = null,

	@SerializedName("benefit_type")
	val benefitType: String? = null,

	@SerializedName("date_start")
	val dateStart: String? = null,

	@SerializedName("benefit_idr")
	val benefitIdr: Int? = null,

	@SerializedName("image_portrait")
	val imagePortrait: String? = null,

	@SerializedName("quota")
	val quota: Int? = null,

	@SerializedName("is_public")
	val isPublic: Int? = null,

	@SerializedName("voucher_id")
	val voucherId: Int? = null,

	@SerializedName("coupon_type")
	val couponType: String? = null,

	@SerializedName("hour_start")
	val hourStart: String? = null,

	@SerializedName("is_lock_to_product")
	val is_lock_to_product: Int? = null,

	@SerializedName("product_ids")
	val product_ids: String? = null,

	@SerializedName("product_ids_csv_url")
	val product_ids_csv_url: String? = null,

	@SerializedName("warehouse_id")
	val warehouse_id: Int? = null
)
