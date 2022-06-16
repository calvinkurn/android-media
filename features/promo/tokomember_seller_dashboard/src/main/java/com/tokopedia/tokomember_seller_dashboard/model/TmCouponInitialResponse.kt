package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.SerializedName

data class TmCouponInitialResponse(

	@SerializedName("getInitiateVoucherPage")
	val getInitiateVoucherPage: GetInitiateVoucherPage? = null
)

data class DataCouponInitial(

	@SerializedName("img_banner_ig_post")
	val imgBannerIgPost: String? = null,

	@SerializedName("prefix_voucher_code")
	val prefixVoucherCode: String? = null,

	@SerializedName("img_banner_base")
	val imgBannerBase: String? = null,

	@SerializedName("token")
	val token: String? = null,

	@SerializedName("upload_app_url")
	val uploadAppUrl: String? = null,

	@SerializedName("access_token")
	val accessToken: String? = null,

	@SerializedName("img_banner_ig_story")
	val imgBannerIgStory: String? = null,

	@SerializedName("shop_id")
	val shopId: Int? = null,

	@SerializedName("max_product")
	val maxProduct: Int? = null,

	@SerializedName("user_id")
	val userId: Int? = null,

	@SerializedName("img_banner_cover_gm")
	val imgBannerCoverGm: String? = null,

	@SerializedName("img_banner_cover_os")
	val imgBannerCoverOs: String? = null,

	@SerializedName("img_banner_label_gratis_ongkir")
	val imgBannerLabelGratisOngkir: String? = null,

	@SerializedName("is_eligible")
	val isEligible: Int? = null,

	@SerializedName("img_banner_label_cashback")
	val imgBannerLabelCashback: String? = null,

	@SerializedName("img_banner_label_cashback_hingga")
	val imgBannerLabelCashbackHingga: String? = null
)

data class GetInitiateVoucherPage(

	@SerializedName("data")
	val data: DataCouponInitial? = null,

	@SerializedName("header")
	val header: HeaderCouponInitial? = null
)

data class HeaderCouponInitial(

	@SerializedName("reason")
	val reason: String? = null,

	@SerializedName("messages")
	val messages: List<String?>? = null,

	@SerializedName("error_code")
	val errorCode: String? = null,

	@SerializedName("process_time")
	val processTime: Double? = null
)

data class TMCouponInitialRequest(
	val Action: String,
	val TargetBuyer: Int,
	val CouponType: String,
)