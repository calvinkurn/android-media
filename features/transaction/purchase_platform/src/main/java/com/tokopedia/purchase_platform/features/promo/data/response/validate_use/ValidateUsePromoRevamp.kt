package com.tokopedia.purchase_platform.features.promo.data.response.validate_use

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ValidateUsePromoRevamp(

	@field:SerializedName("promo")
	val promo: Promo? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("error_code")
	val errorCode: String? = null,

	@field:SerializedName("message")
	val message: List<Any?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)