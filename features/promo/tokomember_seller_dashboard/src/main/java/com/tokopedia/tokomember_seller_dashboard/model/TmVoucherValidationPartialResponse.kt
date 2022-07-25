package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmVoucherValidationPartialResponse(
	@Expose
	@SerializedName("VoucherValidationPartial")
	val voucherValidationPartial: VoucherValidationPartial? = null
)

data class HeaderPartial(
	@Expose
	@SerializedName("reason")
	val reason: String? = "",
	@Expose
	@SerializedName("messages")
	val messages: List<Any?>? = arrayListOf(),
	@Expose
	@SerializedName("error_code")
	val errorCode: String? = "",
	@Expose
	@SerializedName("process_time")
	val processTime: Double? = 0.0
)

data class VoucherValidationPartial(
	@Expose
	@SerializedName("data")
	val data: DataPartialValidate? = null,
	@Expose
	@SerializedName("header")
	val header: HeaderPartial? = null
)

data class ValidationError(
	@Expose
	@SerializedName("benefit_max")
	val benefitMax: String? = "",
	@Expose
	@SerializedName("image")
	val image: String? = "",
	@Expose
	@SerializedName("code")
	val code: String? = "",
	@Expose
	@SerializedName("min_purchase")
	val minPurchase: String? = "",
	@Expose
	@SerializedName("benefit_percent")
	val benefitPercent: String? = "",
	@Expose
	@SerializedName("hour_end")
	val hourEnd: String? = "",
	@Expose
	@SerializedName("date_end")
	val dateEnd: String? = "",
	@Expose
	@SerializedName("image_square")
	val imageSquare: String? = "",
	@Expose
	@SerializedName("coupon_name")
	val couponName: String? = "",
	@Expose
	@SerializedName("benefit_type")
	val benefitType: String? = "",
	@Expose
	@SerializedName("date_start")
	val dateStart: String? = "",
	@Expose
	@SerializedName("benefit_idr")
	val benefitIdr: String? = "",
	@Expose
	@SerializedName("quota")
	val quota: String? = "",
	@Expose
	@SerializedName("is_public")
	val isPublic: String? = "",
	@Expose
	@SerializedName("coupon_type")
	val couponType: String? = "",
	@Expose
	@SerializedName("hour_start")
	val hourStart: String? = ""
)

data class DataPartialValidate(
	@Expose
	@SerializedName("validation_error")
	val validationError: ValidationError? = null
)
