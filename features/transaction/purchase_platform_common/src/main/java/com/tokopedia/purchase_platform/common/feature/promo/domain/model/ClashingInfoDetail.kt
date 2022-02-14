package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class ClashingInfoDetail(
	@field:SerializedName("is_clashed_promos")
	val isClashedPromos: Boolean = false,
	@field:SerializedName("clash_reason")
	val clashReason: String = "",
	@field:SerializedName("clash_message")
	val clashMessage: String = ""
)