package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import javax.annotation.Generated
import com.google.gson.annotations.SerializedName

@Generated("com.robohorse.robopojogenerator")
data class CashbackDetailsItem(

	@field:SerializedName("benefit_type")
	val benefitType: String? = null,

	@field:SerializedName("amount_points")
	val amountPoints: Int? = null,

	@field:SerializedName("amount_idr")
	val amountIdr: Int? = null
)