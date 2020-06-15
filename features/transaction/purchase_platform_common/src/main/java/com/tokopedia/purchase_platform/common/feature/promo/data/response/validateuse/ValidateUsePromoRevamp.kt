package com.tokopedia.purchase_platform.common.feature.promo.data.response.validateuse

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ValidateUsePromoRevamp(

	@field:SerializedName("promo")
	val promo: PromoValidateUseResponse = PromoValidateUseResponse(),

	@field:SerializedName("code")
	val code: String = "",

	@field:SerializedName("error_code")
	val errorCode: String = "",

	@field:SerializedName("message")
	val message: List<String> = emptyList(),

	@field:SerializedName("status")
	val status: String = ""
)