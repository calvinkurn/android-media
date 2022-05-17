package com.tokopedia.tokomember_seller_dashboard.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TmVoucherValidationPartialResponse(
	@Expose
	@SerializedName("VoucherValidationPartial")
	val voucherValidationPartial: VoucherValidationPartial? = null
)

data class Header(
	@Expose
	@SerializedName("reason")
	val reason: String? = null,
	@Expose
	@SerializedName("messages")
	val messages: List<Any?>? = null,
	@Expose
	@SerializedName("error_code")
	val errorCode: String? = null,
	@Expose
	@SerializedName("process_time")
	val processTime: Double? = null
)

data class VoucherValidationPartial(
	@Expose
	@SerializedName("data")
	val data: DataPartialValidate? = null,
	@Expose
	@SerializedName("header")
	val header: Header? = null
)

data class ValidationError(
	@Expose
	@SerializedName("benefit_max")
	val benefitMax: String? = null,
	@Expose
	@SerializedName("image")
	val image: String? = null,
	@Expose
	@SerializedName("code")
	val code: String? = null,
	@Expose
	@SerializedName("min_purchase")
	val minPurchase: String? = null,
	@Expose
	@SerializedName("benefit_percent")
	val benefitPercent: String? = null,
	@Expose
	@SerializedName("hour_end")
	val hourEnd: String? = null,
	@Expose
	@SerializedName("date_end")
	val dateEnd: String? = null,
	@Expose
	@SerializedName("image_square")
	val imageSquare: String? = null,
	@Expose
	@SerializedName("coupon_name")
	val couponName: String? = null,
	@Expose
	@SerializedName("benefit_type")
	val benefitType: String? = null,
	@Expose
	@SerializedName("date_start")
	val dateStart: String? = null,
	@Expose
	@SerializedName("benefit_idr")
	val benefitIdr: String? = null,
	@Expose
	@SerializedName("quota")
	val quota: String? = null,
	@Expose
	@SerializedName("is_public")
	val isPublic: String? = null,
	@Expose
	@SerializedName("coupon_type")
	val couponType: String? = null,
	@Expose
	@SerializedName("hour_start")
	val hourStart: String? = null
)

data class DataPartialValidate(
	@Expose
	@SerializedName("validation_error")
	val validationError: ValidationError? = null
)
