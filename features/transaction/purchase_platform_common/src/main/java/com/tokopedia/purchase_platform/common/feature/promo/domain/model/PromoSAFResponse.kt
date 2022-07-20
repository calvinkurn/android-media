package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class PromoSAFResponse(
	@field:SerializedName("last_apply")
	val lastApply: LastApply = LastApply(),
	@field:SerializedName("error_default")
	val errorDefault: ErrorDefault = ErrorDefault()
)