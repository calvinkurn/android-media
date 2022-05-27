package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.SerializedName

data class TmUpdateCouponQuotaResponse(

	@SerializedName("data")
	val data: TmUpdateCouponQuotaDataExt? = null
)

data class TmUpdateCouponQuotaDataExt(
	@SerializedName("merchantPromotionUpdateMVQuota")
	val merchantPromotionUpdateMVQuota: MerchantPromotionUpdateMVQuota? = null,
)

data class MerchantPromotionUpdateMVQuota(

	@SerializedName("data")
	val data: TmUpdateCouponQuotaData? = null,

	@SerializedName("message")
	val message: String? = null,

	@SerializedName("process_time")
	val processTime: Double? = null,

	@SerializedName("status")
	val status: Int? = null
)

data class TmUpdateCouponQuotaData(

	@SerializedName("voucher_id")
	val voucherId: String? = null,

	@SerializedName("redirect_url")
	val redirectUrl: String? = null,

	@SerializedName("status")
	val status: String? = null
)

data class TMUpdateQuotaRequest(
	val voucher_id: Int?,
	val quota: Int,
	val token: String,
	val source: String,
)