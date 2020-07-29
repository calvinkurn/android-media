package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class TickerInfo(

	@field:SerializedName("unique_id")
	val uniqueId: String? = null,

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("message")
	val message: String? = null
)