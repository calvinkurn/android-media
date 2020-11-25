package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class PromoSAFResponse(

	@field:SerializedName("last_apply")
	val lastApply: LastApply? = null,

	@field:SerializedName("error_default")
	val errorDefault: ErrorDefault? = null
)