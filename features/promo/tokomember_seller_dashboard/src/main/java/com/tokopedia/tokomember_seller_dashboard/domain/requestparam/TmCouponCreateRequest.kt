package com.tokopedia.tokomember_seller_dashboard.domain.requestparam

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TmCouponCreateRequest(

	@SerializedName("benefit_max")
	var benefitMax: Int? = null,

	@SerializedName("target_buyer")
	val targetBuyer: Int? = null,

	@SerializedName("image")
	val image: String? = null,

	@SerializedName("code")
	val code: String? = "",

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

	@SerializedName("image_square")
	var imageSquare: String? = null,

	@SerializedName("coupon_name")
	var couponName: String? = "",

	@SerializedName("benefit_type")
	var benefitType: String? = null,

	@SerializedName("date_start")
	var dateStart: String? = null,

	@SerializedName("benefit_idr")
	var benefitIdr: Int? = null,

	@SerializedName("image_portrait")
	var imagePortrait: String? = null,

	@SerializedName("quota")
	var quota: Int? = null,

	@SerializedName("is_public")
	val isPublic: Int? = null,

	@SerializedName("coupon_type")
	val couponType: String? = null,

	@SerializedName("hour_start")
	val hourStart: String? = null,

	@SerializedName("token")
	var token: String? = null,

	@SerializedName("source")
	var source: String? = null
) : Parcelable
