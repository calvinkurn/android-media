package com.tokopedia.promocheckout.common.domain.model.promostacking.response

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class ClashingInfoDetail(

	@field:SerializedName("is_clashed_promos")
	val isClashedPromos: Boolean? = null,

	@field:SerializedName("clash_reason")
	val clashReason: String? = null,

	@field:SerializedName("clash_message")
	val clashMessage: String? = null,

	@field:SerializedName("option")
	val option: List<Any?>? = null
)