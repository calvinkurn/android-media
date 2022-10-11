package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.SerializedName

data class TmCouponCreateResponseNew(

	@SerializedName("data")
	val data: CouponCreateMultiple? = null
)

data class MerchantPromotionCreateMultipleMV(

	@SerializedName("data")
	val data: DataInner? = null,

	@SerializedName("message")
	val message: String? = null,

	@SerializedName("process_time")
	val processTime: Double? = null,

	@SerializedName("status")
	val status: Int? = null
)

data class MerchantPromotionCreateSingleleMV(

	@SerializedName("data")
	val data: DataInnerSingle? = null,

	@SerializedName("message")
	val message: String? = null,

	@SerializedName("process_time")
	val processTime: Double? = null,

	@SerializedName("status")
	val status: Int? = null
)

data class CouponCreateMultiple(
	@SerializedName("merchantPromotionCreateMultipleMV")
	val merchantPromotionCreateMultipleMV: MerchantPromotionCreateMultipleMV? = null
)


data class CouponCreateSingle(
	@SerializedName("merchantPromotionCreateMV")
	val merchantPromotionCreateMV: MerchantPromotionCreateSingleleMV? = null
)

data class DataInner(

	@SerializedName("voucher_id")
	val voucherId: List<Any?>? = null,

	@SerializedName("redirect_url")
	val redirectUrl: String? = null,

	@SerializedName("status")
	val status: String? = null
)

data class DataInnerSingle(

	@SerializedName("voucher_id")
	val voucherId: Int? = null,

	@SerializedName("redirect_url")
	val redirectUrl: String? = null,

	@SerializedName("status")
	val status: String? = null
)
