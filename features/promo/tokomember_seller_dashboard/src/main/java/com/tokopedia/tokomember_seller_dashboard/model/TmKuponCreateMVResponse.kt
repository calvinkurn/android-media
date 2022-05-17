package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmKuponCreateMVResponse(
	@Expose
	@SerializedName("merchantPromotionCreateMV")
	val merchantPromotionCreateMV: MerchantPromotionCreateMV? = null
)

data class DataCreateMv(
	@Expose
	@SerializedName("voucher_id")
	val voucherId: Int? = null,
	@Expose
	@SerializedName("redirect_url")
	val redirectUrl: String? = null,
	@Expose
	@SerializedName("status")
	val status: String? = null
)

data class MerchantPromotionCreateMV(
	@Expose
	@SerializedName("data")
	val data: DataCreateMv? = null,
	@Expose
	@SerializedName("message")
	val message: String? = null,
	@Expose
	@SerializedName("process_time")
	val processTime: Double? = null,
	@Expose
	@SerializedName("status")
	val status: Int? = null
)
