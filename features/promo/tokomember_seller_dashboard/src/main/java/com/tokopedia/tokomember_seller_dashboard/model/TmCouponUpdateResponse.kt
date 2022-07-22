package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.SerializedName

data class TmCouponUpdateResponse(
	@SerializedName("merchantPromotionUpdateStatusMV")
	val data: MerchantPromotionUpdateStatusMV? = null
)

data class TmCouponUpdateData(

	@SerializedName("voucher_id")
	val voucherId: String? = null,

	@SerializedName("redirect_url")
	val redirectUrl: String? = null,

	@SerializedName("status")
	val status: String? = null
)


data class MerchantPromotionUpdateStatusMV(

	@SerializedName("data")
	val merchantPromotionUpdateStatusMV: TmCouponUpdateData? = null,

	@SerializedName("message")
	val message: String? = null,

	@SerializedName("process_time")
	val processTime: Double? = null,

	@SerializedName("status")
	val status: Int? = null
)

data class TMUpdateStatusRequest(
	val voucher_id: Int?,
	val voucher_status: String,
	val token: String,
	val source: String,
)