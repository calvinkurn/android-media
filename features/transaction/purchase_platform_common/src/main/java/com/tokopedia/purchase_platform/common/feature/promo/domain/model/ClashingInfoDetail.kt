package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ClashingInfoDetail(

	@field:SerializedName("is_clashed_promos")
	val isClashedPromos: Boolean? = null,

	@field:SerializedName("options")
	val options: Any? = null,

	@field:SerializedName("clash_reason")
	val clashReason: String? = null,

	@field:SerializedName("clash_message")
	val clashMessage: String? = null
)